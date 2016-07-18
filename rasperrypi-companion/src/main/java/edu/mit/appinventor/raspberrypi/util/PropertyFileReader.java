package edu.mit.appinventor.raspberrypi.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This is the PropertyFileReader class that reads essential properties from
 * config.properties.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class PropertyFileReader {

  public enum Values {
    MODEL("model"), BROKER_ADDRESS("brokerAddress"), BROKER_PORT("brokerPort"), QOS("qos"), IDENTIFIER_FILE(
        "identifierFile");

    private final String name;

    private Values(String s) {
      name = s;
    }

    public boolean equals(String other) {
      return (other == null) ? false : name.equals(other);
    }

    public String toString() {
      return name;
    }

  }

  private Properties prop;

  private static PropertyFileReader instance = null;

  protected PropertyFileReader() {
    // Exists only to defeat instantiation.
  }

  public static PropertyFileReader getInstance() {
    if (instance == null) {
      instance = new PropertyFileReader();
      try {
        instance.parseProperties();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return instance;
  }

  private void parseProperties() throws IOException {
    prop = new Properties();
    String propFileName = "config.properties";

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

    if (inputStream != null) {
      prop.load(inputStream);
    } else {
      throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
    }

  }

  public String getProperty(Values pProperty) {
    return prop.getProperty(pProperty.toString());
  }

}
