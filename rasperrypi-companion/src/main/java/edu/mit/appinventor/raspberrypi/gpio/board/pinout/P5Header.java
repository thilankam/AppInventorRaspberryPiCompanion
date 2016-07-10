package edu.mit.appinventor.raspberrypi.gpio.board.pinout;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Models the pin numbering between the physical header layout and the Pi4J GPIO
 * pin numbering scheme that uses the P5 6 pin header. The two way pin
 * configuration is handled by Guava's BiMap. This is uses as an expansion for
 * Raspberry Pi Model B (Revision 2.0) - http://pi4j.com/pins/model-b-rev2.html.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class P5Header extends AbstractHeader implements Header {

  protected BiMap<Integer, Pin> pinConfiguration;

  public P5Header() {
    pinConfiguration = HashBiMap.create();
    pinConfiguration.put(3, RaspiPin.GPIO_17);
    pinConfiguration.put(4, RaspiPin.GPIO_18);
    pinConfiguration.put(6, RaspiPin.GPIO_20);
    pinConfiguration.put(5, RaspiPin.GPIO_19);

  }

  @Override
  public String getName() {
    return "P5";
  }

  @Override
  public int getPins() {
    return 8;
  }

}
