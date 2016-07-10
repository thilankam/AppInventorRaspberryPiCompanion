package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.J8Header;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Models the RaspberryPi 2 Model B. See:
 * http://pi4j.com/pins/model-2b-rev1.html
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class Pi2B extends AbstractBoard implements Board {

  public Pi2B() {
    mPinHeader = new J8Header();
    mModel = RaspberrryPiModel.Pi2B;
  }

}
