package edu.mit.appinventor.raspberrypi.util;

import edu.mit.mqtt.raspberrypi.model.device.RaspberrryPiModel;

/**
 * Some static utility methods related to the property file config.properties.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
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
  
  public static String getIdentifierFileName() {
    return PropertyFileReader.getInstance().getProperty(PropertyFileReader.Values.IDENTIFIER_FILE);
  }

}
