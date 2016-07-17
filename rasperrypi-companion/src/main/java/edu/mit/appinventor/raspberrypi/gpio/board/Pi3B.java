package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.J8Header;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Models the RaspberryPi 3 Model B.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class Pi3B extends AbstractBoard implements Board {

  public Pi3B() {
    mPinHeader = new J8Header();
    mModel = RaspberrryPiModel.Pi3B;
  }

}
