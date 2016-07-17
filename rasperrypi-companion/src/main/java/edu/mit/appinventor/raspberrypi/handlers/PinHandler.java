package edu.mit.appinventor.raspberrypi.handlers;

import edu.mit.mqtt.raspberrypi.HeaderPin;

/**
 * PinHandler interface.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com).
 *
 */
public interface PinHandler {

  public void handle(HeaderPin pHeaderPin);

}
