package edu.mit.appinventor.raspberrypi;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.Pi1A;
import edu.mit.appinventor.raspberrypi.gpio.board.Pi2B;
import junit.framework.TestCase;

public class BoardTest extends TestCase {

  public void testPinSetting() throws Exception {

    Board myPi1ABoard = new Pi1A();
    Pin pi1APin40 = myPi1ABoard.getPinHeader().getGpioPin(40);
    assertEquals(null, pi1APin40);
    System.out.println(pi1APin40);

    Board myPi2BBoard = new Pi2B();
    Pin pi2BPin40 = myPi2BBoard.getPinHeader().getGpioPin(40);
    assertEquals(RaspiPin.GPIO_29, pi2BPin40);
    System.out.println(pi2BPin40);

  }

}
