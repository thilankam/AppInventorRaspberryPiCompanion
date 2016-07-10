package edu.mit.appinventor.raspberrypi.gpio.exception;

/**
 * Exception to throw when an unsupported operation is called.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class UnSupportedException extends Exception {

  private static final long serialVersionUID = 1L;

  public UnSupportedException(String pError) {
    super(pError);
  }

}
