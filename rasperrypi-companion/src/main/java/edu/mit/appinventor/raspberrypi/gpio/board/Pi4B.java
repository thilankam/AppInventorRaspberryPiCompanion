package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.J8Header;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Models the RaspberryPi 4 Model B.
 */
public class Pi4B extends AbstractBoard implements Board {

  public Pi4B() {
    mPinHeader = new J8Header();
    mModel = RaspberrryPiModel.Pi4B;
  }

}
