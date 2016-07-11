package edu.mit.appinventor.raspberrypi.gpio;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import edu.mit.appinventor.raspberrypi.gpio.board.BoardFactory;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.Header;
import edu.mit.appinventor.raspberrypi.gpio.exception.UnSupportedException;
import edu.mit.mqtt.raspberrypi.HeaderPin;
import edu.mit.mqtt.raspberrypi.Messages;
import edu.mit.mqtt.raspberrypi.model.device.PinDirection;
import edu.mit.mqtt.raspberrypi.model.device.PinProperty;
import edu.mit.mqtt.raspberrypi.model.device.PinValue;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;
import edu.mit.mqtt.raspberrypi.model.messaging.Topic;

public class Companion implements MqttCallback, GpioPinListenerDigital {

  MqttClient mClient;
  private final int defaultQos = 2;

  // create gpio controller
  final GpioController gpio = GpioFactory.getInstance();

  GpioPinDigitalOutput mOutputPin;
  GpioPinDigitalInput mInputPin;

  // TODO is it correct to reset the board/header every time a message sent?
  // The board/header is actually needed when a sensor input is sent to App
  // Inventor.
  Board mBoard;
  Header mHeader;

  public Companion() {
    System.out.println("Creating the MQTT client.");
    try {

      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      mClient = new MqttClient("tcp://192.168.0.9:1883", MqttClient.generateClientId(), new MemoryPersistence());
      System.out.println("Connecting to broker...");
      mClient.connect(connOpts);
      mClient.setCallback(this);
      System.out.println("Connected.");
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    System.out.println("Running the companion...");
    new Companion().doDemo();
  }

  public void messageArrived(String pTopic, MqttMessage pMessage) throws Exception {
    System.out.println("Message received from topic: " + pTopic + "\tMessage: " + pMessage.toString());
    if (pTopic.equals(Topic.INTERNAL.toString())) {

      HeaderPin pin = Messages.deconstrctPinMessage(new String(pMessage.getPayload()));

      mBoard = BoardFactory.getBoard(pin.raspberryPiModel);
      mHeader = mBoard.getPinHeader();

      Pin gpioPin = mHeader.getGpioPin(pin.number);

      PinDirection pinDirection = pin.direction;

      String label = pin.label;

      if (pin.property.equals(PinProperty.PIN_STATE)) {

	if (pinDirection.equals(PinDirection.OUT)) {
	  System.out.println("out");
	  mOutputPin = gpio.provisionDigitalOutputPin(gpioPin, label);
	  // set shutdown state for this pin TODO: should there be a config
	  // option
	  // from AppInventor for this?
	  mOutputPin.setShutdownOptions(true, PinState.LOW);
	  System.out.println(mOutputPin);
	  System.out.println("pin_state");
	  if (pin.value.equals(PinValue.HIGH)) {
	    System.out.println("high");
	    mOutputPin.high();
	  } else if (pin.value.equals(PinValue.LOW)) {
	    System.out.println("low");
	    mOutputPin.low();
	  }

	} else {
	  throw new UnSupportedException("Not supported yet.");
	}
      } else if (pin.property.equals(PinProperty.REGISTER)) {
	mInputPin = gpio.provisionDigitalInputPin(gpioPin, label);
      }

    } else {
      System.out.println(pTopic + " " + new String(pMessage.getPayload()));
    }

  }

  public void connectionLost(Throwable cause) {
  }

  public void deliveryComplete(IMqttDeliveryToken token) {
  }

  public void doDemo() {
    try {
      String topic = Topic.INTERNAL.toString();
      System.out.println("Subscribing to topic " + topic);
      mClient.subscribe(topic);
      System.out.println("Subscribed to topic " + topic);

      // We’ll now idle here sleeping, but your app can be busy
      // working here instead
      while (true) {
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException e) {
	  // stop all GPIO activity/threads by shutting down the GPIO controller
	  // (this method will forcefully shutdown all GPIO monitoring threads
	  // and scheduled tasks)
	  gpio.shutdown();
	}
      }
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
    int headerPinNumber = mHeader.getHeaderPin(event.getPin().getPin());
    PinState state = event.getState();
    sendMessage(headerPinNumber, state);
  }

  private void sendMessage(int pHeaderPinNumber, PinState pState) {

    HeaderPin myPin = new HeaderPin();
    myPin.number = pHeaderPinNumber;
    myPin.property = PinProperty.PIN_STATE;
    if (pState.isHigh()) {
      myPin.value = PinValue.HIGH;
    } else if (pState.isLow()) {
      myPin.value = PinValue.LOW;
    }
    myPin.direction = PinDirection.IN;
    myPin.raspberryPiModel = mBoard.getModel();

    String content = Messages.constructPinMessage(myPin);

    try {
      System.out.println("Publishing message: " + content);
      MqttMessage message = new MqttMessage(content.getBytes());
      message.setQos(defaultQos);
      mClient.publish(Topic.INTERNAL.toString(), message);
      System.out.println("Message published");
    } catch (MqttException me) {
      System.out.println("reason " + me.getReasonCode());
      System.out.println("msg " + me.getMessage());
      System.out.println("loc " + me.getLocalizedMessage());
      System.out.println("cause " + me.getCause());
      System.out.println("excep " + me);
      me.printStackTrace();
    }
  }

  private static void test() {
    int pinNumber = 13;
    HeaderPin myPin = new HeaderPin();
    myPin.number = pinNumber;
    myPin.label = "LED";
    myPin.direction = PinDirection.OUT;
    myPin.property = PinProperty.PIN_STATE;
    myPin.value = PinValue.HIGH;
    myPin.raspberryPiModel = RaspberrryPiModel.Pi2B;
    String message = Messages.constructPinMessage(myPin);

    MqttMessage mqttMessage = new MqttMessage();
    mqttMessage.setPayload(message.getBytes());

    try {
      new Companion().messageArrived(Topic.INTERNAL.toString(), mqttMessage);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}