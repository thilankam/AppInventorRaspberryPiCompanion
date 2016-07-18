package edu.mit.appinventor.raspberrypi.handlers;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import edu.mit.appinventor.raspberrypi.PinRegistry;
import edu.mit.appinventor.raspberrypi.TopicRegistry;
import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.BoardFactory;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.Header;
import edu.mit.appinventor.raspberrypi.util.PropertyFileReader;
import edu.mit.appinventor.raspberrypi.util.PropertyUtil;
import edu.mit.appinventor.raspberrypi.util.PropertyFileReader.Values;
import edu.mit.mqtt.raspberrypi.HeaderPin;
import edu.mit.mqtt.raspberrypi.Messages;
import edu.mit.mqtt.raspberrypi.model.device.PinDirection;
import edu.mit.mqtt.raspberrypi.model.device.PinProperty;
import edu.mit.mqtt.raspberrypi.model.device.PinValue;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;
import edu.mit.mqtt.raspberrypi.model.messaging.Topic;

/**
 * This is the InputPinHandler class which listens to any changes in the Gpio
 * digital pins, and broadcasts a message for AppInventor to pickup.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com).
 *
 */
public class InputPinHandler implements PinHandler, GpioPinListenerDigital {

  MqttClient mClient;

  Header mHeader;

  private final Logger LOGGER = LoggerFactory.getLogger(InputPinHandler.class);

  public InputPinHandler() {
    MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(true);
    try {
      mClient = new MqttClient(PropertyUtil.getMqttAddress(), MqttClient.generateClientId(), new MemoryPersistence());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Connecting to broker " + PropertyUtil.getMqttAddress() + "...");
      }
      mClient.connect(connOpts);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Connected to broker " + PropertyUtil.getMqttAddress() + ".");
      }
    } catch (MqttException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  @Override
  public void handle(HeaderPin pHeaderPin) {

    Board board = BoardFactory.getBoard(PropertyUtil.getRaspberryPiModel());
    mHeader = board.getPinHeader();
    Pin pin = mHeader.getGpioPin(pHeaderPin.number);
    String label = pHeaderPin.label;

    // create gpio controller
    final GpioController gpio = GpioFactory.getInstance();
    GpioPinDigitalInput gpioPin;
    if (PinRegistry.getInstance().exists(pin)) {
      gpioPin = (GpioPinDigitalInput) PinRegistry.getInstance().get(pin);
    } else {
      gpioPin = gpio.provisionDigitalInputPin(pin, label);
      gpioPin.addListener(this);
      PinRegistry.getInstance().add(pin, gpioPin);
    }
  }

  @Override
  public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent pEvent) {
    int headerPinNumber = mHeader.getHeaderPin(pEvent.getPin().getPin());
    PinState state = pEvent.getState();
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
    myPin.raspberryPiModel = RaspberrryPiModel
        .fromString(PropertyFileReader.getInstance().getProperty(PropertyFileReader.Values.MODEL));

    String content = Messages.constructPinMessage(myPin);

    try {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Publishing message: " + content + "...");
      }
      MqttMessage message = new MqttMessage(content.getBytes());
      message.setQos(Integer.parseInt(PropertyFileReader.getInstance().getProperty(Values.QOS)));
      mClient.publish(TopicRegistry.getInstance().getInternalTopic(), message);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Message: " + content + " published.");
      }
    } catch (MqttException me) {
      StringBuilder errorSb = new StringBuilder();
      errorSb.append("reason " + me.getReasonCode());
      errorSb.append("\nmsg " + me.getMessage());
      errorSb.append("\nloc " + me.getLocalizedMessage());
      errorSb.append("\ncause " + me.getCause());
      errorSb.append("\nexcep " + me);
      LOGGER.error(errorSb.toString());
    }
  }

}
