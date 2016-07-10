package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.P1Header;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Models the RaspberryPi 1 Model A. See: http://pi4j.com/pins/model-a-rev2.html
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class Pi1A extends AbstractBoard implements Board {

  public Pi1A() {
    mPinHeader = new P1Header();
    mModel = RaspberrryPiModel.Pi1A;
  }
  
}
