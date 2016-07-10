package edu.mit.appinventor.raspberrypi.gpio.board.pinout;

import com.pi4j.io.gpio.Pin;

/**
 * Models the pin header for different RaspberryPi devices.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public interface Header {

  public String getName();

  public int getPins();

  public Pin getGpioPin(int pHeaderPin);

  public int getHeaderPin(Pin pGpioPin);

}
