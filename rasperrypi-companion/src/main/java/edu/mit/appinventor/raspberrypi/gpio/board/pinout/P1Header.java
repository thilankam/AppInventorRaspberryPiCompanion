package edu.mit.appinventor.raspberrypi.gpio.board.pinout;

import com.google.common.collect.HashBiMap;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Models the pin numbering between the physical header layout and the Pi4J GPIO
 * pin numbering scheme that uses the P1 26 pin header. The two way pin
 * configuration is handled by Guava's BiMap. Here are some of the P1 Pinout
 * (26-pin header) configurations: Raspberry Pi Model A -
 * http://pi4j.com/pins/model-a-rev2.html, Raspberry Pi Model B (Revision 1.0) -
 * http://pi4j.com/pins/model-b-rev1.html, Raspberry Pi Model B (Revision 2.0) -
 * http://pi4j.com/pins/model-b-rev2.html.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class P1Header extends AbstractHeader implements Header {

  public P1Header() {
    pinConfiguration = HashBiMap.create();
    pinConfiguration.put(3, RaspiPin.GPIO_08);
    pinConfiguration.put(5, RaspiPin.GPIO_09);
    pinConfiguration.put(7, RaspiPin.GPIO_07);
    pinConfiguration.put(8, RaspiPin.GPIO_15);
    pinConfiguration.put(10, RaspiPin.GPIO_16);
    pinConfiguration.put(11, RaspiPin.GPIO_00);
    pinConfiguration.put(12, RaspiPin.GPIO_01);
    pinConfiguration.put(13, RaspiPin.GPIO_02);
    pinConfiguration.put(15, RaspiPin.GPIO_03);
    pinConfiguration.put(16, RaspiPin.GPIO_04);
    pinConfiguration.put(18, RaspiPin.GPIO_05);
    pinConfiguration.put(19, RaspiPin.GPIO_12);
    pinConfiguration.put(21, RaspiPin.GPIO_13);
    pinConfiguration.put(22, RaspiPin.GPIO_06);
    pinConfiguration.put(23, RaspiPin.GPIO_14);
    pinConfiguration.put(24, RaspiPin.GPIO_10);
    pinConfiguration.put(26, RaspiPin.GPIO_11);
  }

  @Override
  public String getName() {
    return "P1";
  }

  @Override
  public int getPins() {
    return 26;
  }

}
