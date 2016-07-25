package edu.mit.appinventor.raspberrypi;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.appinventor.raspberrypi.handlers.InputPinHandler;
import edu.mit.appinventor.raspberrypi.handlers.OutputPinHandler;
import edu.mit.mqtt.raspberrypi.HeaderPin;
import edu.mit.mqtt.raspberrypi.Messages;
import edu.mit.mqtt.raspberrypi.model.device.PinDirection;
import edu.mit.mqtt.raspberrypi.model.device.PinProperty;

/**
 * ListenerService that listens for any incoming messages and delegates those
 * messages to the appropriate handler.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class ListenerService implements Runnable {

  private final Logger LOGGER = LoggerFactory.getLogger(ListenerService.class);

  private String mTopic;
  private MqttMessage mMessage;

  public ListenerService(String pTopic, MqttMessage pMessage) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Starting ListenerService thread.");
    }
    mTopic = pTopic;
    mMessage = pMessage;

  }

  @Override
  public void run() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Message received from topic: " + mTopic + "\tMessage: " + mMessage.toString());
    }

    if (mTopic.equals(TopicRegistry.getInstance().getInternalTopic())) {

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Internal topic found.");
      }

      HeaderPin pin = Messages.deconstrctPinMessage(new String(mMessage.getPayload()));

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("pin = " + pin);
      }

      PinDirection pinDirection = pin.direction;

      if (pin.property.equals(PinProperty.PIN_STATE)) {

        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Pin has PIN_STATE property.");
        }

        if (pinDirection.equals(PinDirection.OUT)) {

          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pin direction is OUT.");
          }

          OutputPinHandler outputPinHandler = new OutputPinHandler();
          outputPinHandler.handle(pin);
        } else {
          LOGGER.error(pin.property + " not supported yet!");
        }
      } else if (pin.property.equals(PinProperty.REGISTER)) {
        InputPinHandler inputPinHandler = new InputPinHandler();
        inputPinHandler.handle(pin);
      }

    } else {
      LOGGER.error(mTopic + " " + new String(mMessage.getPayload()) + " received. Not taking any action.");
    }

  }
}
