package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.Header;
import edu.mit.appinventor.raspberrypi.gpio.exception.NotAvailableException;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * This is the AbstactBoard class. It implements the Board.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class AbstractBoard implements Board {

  protected Header mPinHeader;
  protected RaspberrryPiModel mModel;

  @Override
  public RaspberrryPiModel getModel() {
    return mModel;
  }

  @Override
  public Header getPinHeader() {
    return mPinHeader;
  }

  @Override
  public Header getExtendedPinHeader() throws NotAvailableException {
    throw new NotAvailableException(getModel().name() + " does not have extended pin headers!");
  }

}
