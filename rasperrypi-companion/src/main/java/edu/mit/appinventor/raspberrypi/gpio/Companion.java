package edu.mit.appinventor.raspberrypi.gpio;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

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

public class Companion implements MqttCallback {

  MqttClient mClient;

  // create gpio controller
  final GpioController gpio = GpioFactory.getInstance();

  GpioPinDigitalOutput indicator;
  GpioPinDigitalInput sensor;

  public void messageArrived(String pTopic, MqttMessage pMessage) throws Exception {
    System.out.println("Message received from topic: " + pTopic + "\tMessage: " + pMessage.toString());
    if (pTopic.equals(Topic.INTERNAL.toString())) {

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();

      HeaderPin pin = gson.fromJson(new String(pMessage.getPayload()), HeaderPin.class);

      Board board = BoardFactory.getBoard(pin.raspberryPiModel);

      Header header = board.getPinHeader();

      Pin gpioPin = header.getGpioPin(pin.number);

      PinDirection pinDirection = pin.direction;

      String label = pin.label;

      if (pinDirection.equals(PinDirection.OUT)) {
	System.out.println("out");
	indicator = gpio.provisionDigitalOutputPin(gpioPin, label);
	// set shutdown state for this pin TODO: should there be a config option
	// from AppInventor for this?
	indicator.setShutdownOptions(true, PinState.LOW);
	System.out.println(indicator);
	if (pin.property.equals(PinProperty.PIN_STATE)) {
	  System.out.println("pin_state");
	  if (pin.value.equals(PinValue.HIGH)) {
	    System.out.println("high");
	    indicator.high();
	  } else if (pin.value.equals(PinValue.LOW)) {
	    System.out.println("low");
	    indicator.low();
	  }
	}

      } else {
	// sensor = gpio.provisionDigitalInputPin(gpioPin, label);
	throw new UnSupportedException("Not supported yet.");
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
      System.out.println("Creating the MQTT client.");
      mClient = new MqttClient("tcp://192.168.0.9:1883", MqttClient.generateClientId());
      mClient.connect();
      mClient.setCallback(this);
      System.out.println("Created the MQTT client.");

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

  public static void main(String[] args) {
    System.out.println("Running the companion...");
    new Companion().doDemo();

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