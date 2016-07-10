package edu.mit.appinventor.raspberrypi.gpio;

import edu.mit.appinventor.raspberrypi.gpio.board.pinout.Header;
import edu.mit.appinventor.raspberrypi.gpio.exception.NotAvailableException;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Interface to convert the pin numbering of different RaspberryPi models.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public interface Board {

  public RaspberrryPiModel getModel();
  
  public Header getPinHeader();
  
  public Header getExtendedPinHeader() throws NotAvailableException;

}
