package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.Header;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.P1Header;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.P5Header;
import edu.mit.appinventor.raspberrypi.gpio.exception.NotAvailableException;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Models the RaspberryPi 1 Model B revision 2. See: http://pi4j.com/pins/model-b-rev2.html
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class Pi1Bv2 extends AbstractBoard implements Board {

  public Pi1Bv2() {
    mPinHeader = new P1Header();
    mModel = RaspberrryPiModel.Pi1Bv2;
  }

  @Override
  public Header getExtendedPinHeader() throws NotAvailableException {
    return new P5Header();
  }

}
