package edu.mit.appinventor.raspberrypi.gpio.exception;

/**
 * Exception to throw when a feature not available in the RaspberryPi device is
 * called.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class NotAvailableException extends Exception {

  private static final long serialVersionUID = 1L;

  public NotAvailableException(String pError) {
    super(pError);
  }

}
