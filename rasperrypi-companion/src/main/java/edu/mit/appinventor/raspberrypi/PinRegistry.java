package edu.mit.appinventor.raspberrypi;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;

/**
 * PinRegistery is a singleton that stores the GpioPin and the corresponding
 * provisioned Gpio input or output pin.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class PinRegistry {

  private static PinRegistry instance = null;

  private static Map<Pin, Object> pinCache;

  // create gpio controller
  private final GpioController gpioController = GpioFactory.getInstance();

  protected PinRegistry() {
    // Exists only to defeat instantiation.
  }

  public static PinRegistry getInstance() {
    if (instance == null) {
      instance = new PinRegistry();
      pinCache = new HashMap<Pin, Object>();
    }
    return instance;
  }

  public void add(Pin pPin, Object pVal) {
    pinCache.put(pPin, pVal);
  }

  public boolean exists(Pin pPin) {
    return pinCache.containsKey(pPin);
  }

  public void remove(Pin pPin) {
    pinCache.remove(pPin);
  }

  public Object get(Pin pPin) {
    return pinCache.get(pPin);
  }
  
  public GpioController getGpioController(){
    return gpioController;
  }
}
