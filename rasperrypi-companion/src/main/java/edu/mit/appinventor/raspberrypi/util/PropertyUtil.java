package edu.mit.appinventor.raspberrypi.util;

import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

public class PropertyUtil {
  
  public static String getMqttAddress() {
    StringBuilder mqttAddress = new StringBuilder();
    mqttAddress.append("tcp://");
    mqttAddress.append(PropertyFileReader.getInstance().getProperty(PropertyFileReader.Values.BROKER_ADDRESS));
    mqttAddress.append(":");
    mqttAddress.append(PropertyFileReader.getInstance().getProperty(PropertyFileReader.Values.BROKER_PORT));
    return mqttAddress.toString();
  }

  public static RaspberrryPiModel getRaspberryPiModel() {
    return RaspberrryPiModel.fromString(PropertyFileReader.getInstance().getProperty(PropertyFileReader.Values.MODEL));
  }

}
