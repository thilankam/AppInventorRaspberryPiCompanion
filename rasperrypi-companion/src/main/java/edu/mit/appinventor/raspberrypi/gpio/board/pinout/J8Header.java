package edu.mit.appinventor.raspberrypi.gpio.board.pinout;

import com.pi4j.io.gpio.RaspiPin;

/**
 * Models the pin numbering between the physical header layout and the Pi4J GPIO
 * pin numbering scheme that uses the J8 40 pin header. The two way pin
 * configuration is handled by Guava's BiMap. Here are some of the J8 Pinout
 * (40-pin header) configurations: Raspberry Pi Model A+ -
 * http://pi4j.com/pins/model-a-plus.html, Raspberry Pi Model B+ -
 * http://pi4j.com/pins/model-b-plus.html, Raspberry Pi 2 Model B -
 * http://pi4j.com/pins/model-2b-rev1.html.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class J8Header extends P1Header implements Header {

  public J8Header() {
    pinConfiguration.put(29, RaspiPin.GPIO_21);
    pinConfiguration.put(31, RaspiPin.GPIO_22);
    pinConfiguration.put(32, RaspiPin.GPIO_26);
    pinConfiguration.put(33, RaspiPin.GPIO_23);
    pinConfiguration.put(35, RaspiPin.GPIO_24);
    pinConfiguration.put(36, RaspiPin.GPIO_27);
    pinConfiguration.put(37, RaspiPin.GPIO_25);
    pinConfiguration.put(38, RaspiPin.GPIO_28);
    pinConfiguration.put(40, RaspiPin.GPIO_29);
  }

  @Override
  public String getName() {
    return "J8";
  }

  @Override
  public int getPins() {
    return 40;
  }

}
