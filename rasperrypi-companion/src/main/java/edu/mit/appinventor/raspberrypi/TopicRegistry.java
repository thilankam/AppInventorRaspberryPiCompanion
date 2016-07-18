package edu.mit.appinventor.raspberrypi;

/**
 * TopicRegistery is a singleton that stores the topics with the device id that
 * are being communicated between the RaspberryPi device and AppInventor.
 * Currently this class only stores the device specific internal topic.
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class TopicRegistry {

  private static TopicRegistry instance = null;

  private static String topic = null;

  protected TopicRegistry() {
    // Exists only to defeat instantiation.
  }

  public static TopicRegistry getInstance() {
    if (instance == null) {
      instance = new TopicRegistry();
    }
    return instance;
  }

  public void setInternalTopic(String pTopic) {
    topic = pTopic;
  }

  public String getInternalTopic() {
    return topic;
  }

}
