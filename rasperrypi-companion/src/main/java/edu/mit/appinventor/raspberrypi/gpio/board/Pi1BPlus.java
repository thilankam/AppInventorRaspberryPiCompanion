package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.J8Header;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Models the RaspberryPi 1 Model B+. See:
 * http://pi4j.com/pins/model-b-plus.html
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class Pi1BPlus extends AbstractBoard implements Board {

  public Pi1BPlus() {
    mPinHeader = new J8Header();
    mModel = RaspberrryPiModel.Pi1BPlus;
  }

}
