package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.P1Header;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Models the RaspberryPi 1 Model B (revision 1). See: http://pi4j.com/pins/model-b-rev1.html,
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class Pi1B extends AbstractBoard implements Board {

  public Pi1B() {
    mPinHeader = new P1Header();
    mModel = RaspberrryPiModel.Pi1B;
  }

}
