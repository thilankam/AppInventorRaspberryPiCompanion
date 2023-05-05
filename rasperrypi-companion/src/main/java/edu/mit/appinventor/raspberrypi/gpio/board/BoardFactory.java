package edu.mit.appinventor.raspberrypi.gpio.board;

import edu.mit.appinventor.raspberrypi.gpio.Board;
import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * This is the BoardFactory class. In this class, the Raspberry Pi board models
 * are generated.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class BoardFactory {

  public static Board getBoard(RaspberrryPiModel pRaspberrryPiModel) {
    if (pRaspberrryPiModel.equals(RaspberrryPiModel.Pi1A)) {
      return new Pi1A();
    }
    if (pRaspberrryPiModel.equals(RaspberrryPiModel.Pi1APlus)) {
      return new Pi1APlus();
    }
    if (pRaspberrryPiModel.equals(RaspberrryPiModel.Pi1B)) {
      return new Pi1B();
    }
    if (pRaspberrryPiModel.equals(RaspberrryPiModel.Pi1BPlus)) {
      return new Pi1BPlus();
    }
    if (pRaspberrryPiModel.equals(RaspberrryPiModel.Pi1Bv2)) {
      return new Pi1Bv2();
    }
    if (pRaspberrryPiModel.equals(RaspberrryPiModel.Pi2B)) {
      return new Pi2B();
    }
    if (pRaspberrryPiModel.equals(RaspberrryPiModel.Pi3B)) {
      return new Pi3B();
    }
    if (pRaspberrryPiModel.equals(RaspberrryPiModel.Pi4B)) {
      return new Pi4B();
    }
    return null;
  }

}
