package edu.mit.appinventor.raspberrypi.gpio.board.pinout;

import com.google.common.collect.BiMap;
import com.pi4j.io.gpio.Pin;

/**
 * Abstract class to model the pin header.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public abstract class AbstractHeader implements Header {

  protected BiMap<Integer, Pin> pinConfiguration;

  @Override
  public Pin getGpioPin(int pHeaderPin) {
    return pinConfiguration.get(pHeaderPin);
  }

  @Override
  public int getHeaderPin(Pin pGpioPin) {
    return pinConfiguration.inverse().get(pGpioPin);
  }

}
