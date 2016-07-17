package edu.mit.appinventor.raspberrypi.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

import edu.mit.appinventor.raspberrypi.PinRegistry;
import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.BoardFactory;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.Header;
import edu.mit.mqtt.raspberrypi.HeaderPin;
import edu.mit.mqtt.raspberrypi.model.device.PinValue;

/**
 * This is the OutputPinHandler class that takes any incoming messages and
 * changes the state of the pins appropriately.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com).
 *
 */
public class OutputPinHandler implements PinHandler {

  private final Logger LOGGER = LoggerFactory.getLogger(OutputPinHandler.class);

  public OutputPinHandler() {

  }

  public void handle(HeaderPin pHeaderPin) {

    Board board = BoardFactory.getBoard(pHeaderPin.raspberryPiModel);
    Header header = board.getPinHeader();
    Pin pin = header.getGpioPin(pHeaderPin.number);
    String label = pHeaderPin.label;

    // create gpio controller
    final GpioController gpio = GpioFactory.getInstance();

    GpioPinDigitalOutput outputPin;
    if (PinRegistry.getInstance().exists(pin)) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Pin exists in cache.");
      }
      // TODO: error handling, in case the pin was provisioned something other
      // than GpioPinDigitalOutput
      outputPin = (GpioPinDigitalOutput) PinRegistry.getInstance().get(pin);
    } else {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Pin does not exist in cache.");
      }
      outputPin = gpio.provisionDigitalOutputPin(pin, label);
      PinRegistry.getInstance().add(pin, outputPin);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Setting GPIO pin " + pHeaderPin.number + " to " + pHeaderPin.value + ".");
    }

    if (pHeaderPin.value.equals(PinValue.HIGH)) {
      outputPin.high();
    } else if (pHeaderPin.value.equals(PinValue.LOW)) {
      outputPin.low();
    } else {
      LOGGER.error("Unknown pin value: " + pHeaderPin.value);
    }

  }
}
