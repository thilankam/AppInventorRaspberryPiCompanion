package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.appinventor.raspberrypi.gpio.board.pinout.Header;
import edu.mit.appinventor.raspberrypi.gpio.exception.NotAvailableException;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

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
