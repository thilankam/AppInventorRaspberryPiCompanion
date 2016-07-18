package edu.mit.appinventor.raspberrypi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import edu.mit.appinventor.raspberrypi.util.PropertyUtil;
import edu.mit.mqtt.raspberrypi.model.messaging.Topic;

/**
 * This is the Raspberry Pi Server class and it implements the MqttCallback
 * interface. The server listens for any incoming MQTT messages, and upon
 * receiving a message starts a new ListenerSevice thread. The Executor is
 * initialized with a default THREAD_POOL_SIZE of 10 .
 * 
 * @author Thilanka Munasinghe (thilankawillbe@gmail.com)
 *
 */
public class Server implements MqttCallback {

  private String mIdentifier;
  
  private String mAbsoluteFilePath;

  MqttClient mClient;

  private ExecutorService executorService;

  private final Logger LOGGER = LoggerFactory.getLogger(Server.class);

  private static final int THREAD_POOL_SIZE = 10;

  public Server() {

    initializeIdentifier();
    displayIdentifier();

    final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("RaspberryPiServer-%d").setDaemon(true)
        .build();

    executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE, threadFactory);

    try {

      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      mClient = new MqttClient(PropertyUtil.getMqttAddress(), MqttClient.generateClientId(), new MemoryPersistence());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Connecting to broker " + PropertyUtil.getMqttAddress() + "...");
      }
      mClient.connect(connOpts);
      mClient.setCallback(this);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Connected to broker " + PropertyUtil.getMqttAddress() + ".");
      }
    } catch (MqttException e) {
      e.printStackTrace();
    }

    subscribeToInternalTopic();

  }

  private void initializeIdentifier() {
    String identifierFileName = PropertyUtil.getIdentifierFileName();
    File identifierFile = new File(identifierFileName);
    mAbsoluteFilePath = identifierFile.getAbsolutePath();
    if (identifierFile.exists()){
      FileReader fileReader;
      try {
        fileReader = new FileReader(identifierFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        mIdentifier = bufferedReader.readLine();
        bufferedReader.close();
      } catch (IOException e) {
        LOGGER.error("Error reading the identifier file!", e);
      }
    } else {
      mIdentifier = UUID.randomUUID().toString();
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Creating the identifier file " + identifierFile.getAbsolutePath() + " to store " + mIdentifier);
      }
      try {
        identifierFile.createNewFile();
        FileWriter fileWriter = new FileWriter(identifierFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(mIdentifier);
        bufferedWriter.close();
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Identifier file " + identifierFile.getAbsolutePath() + " to store " + mIdentifier + " created.");
        }
      } catch (IOException e) {
        LOGGER.error("Error creating the identifier file!", e);
      }
      
    }
  }

  private void subscribeToInternalTopic() {
    
    String topic = createInternalTopic();
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Subscribing to topic " + topic + "...");
    }
    try {
      mClient.subscribe(topic);
    } catch (MqttException e) {
      LOGGER.error(e.getMessage(), e);
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Subscribed to topic " + topic + ".");
    }
  }

  private String createInternalTopic() {
    StringBuilder topicBuilder = new StringBuilder();
    topicBuilder.append(Topic.INTERNAL.toString());
    topicBuilder.append(mIdentifier);
    String topic = topicBuilder.toString();
    TopicRegistry.getInstance().setInternalTopic(topic);
    return topic;
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.idle();
  }

  private void displayIdentifier() {
    
    String welcome = "RaspberryPiCompanion Server for  App Inventor starting...";
    String instruction1 = "Please copy and paste the following string for the 'identifier' parameter ";
    String instruction2 =  "in the 'Initialize' method block of your RaspberryPiServer component:";
    String instruction3 =  "(If you wish to reset this identifier, delete the cached identifier file available at: ";
    String instruction4 =  "and restart the companion application.) ";

    int maxLength = mIdentifier.toString().length() > welcome.length() ? mIdentifier.toString().length()
        : welcome.length();
    maxLength = instruction1.length() > maxLength ? instruction1.length() : maxLength;
    maxLength = instruction2.length() > maxLength ? instruction2.length() : maxLength;
    maxLength = instruction3.length() > maxLength ? instruction3.length() : maxLength;
    maxLength = instruction4.length() > maxLength ? instruction4.length() : maxLength;
    maxLength = mAbsoluteFilePath.length() > maxLength ? mAbsoluteFilePath.length() : maxLength;

    for (int i = 0; i < maxLength + 2; i++) {
      System.out.print("*");
    }
    System.out.println();
    System.out.println(welcome);
    System.out.println();
    System.out.println(instruction1);
    System.out.println(instruction2);
    System.out.println();
    System.out.println(mIdentifier);
    System.out.println();
    System.out.println(instruction3);
    System.out.println(mAbsoluteFilePath);
    System.out.println(instruction4);
    for (int i = 0; i < maxLength + 2; i++) {
      System.out.print("*");
    }
    System.out.println();

  }

  private void idle() {

    // We’ll now idle here sleeping waiting for any messages received.
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        shutdown();
      }
    }
  }

  @Override
  public void connectionLost(Throwable pThrowable) {
    shutdown();
  }

  private void shutdown() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Shutting down the server...");
    }
    executorService.shutdown();
    try {
      final boolean done = executorService.awaitTermination(1, TimeUnit.MINUTES);
      if (LOGGER.isDebugEnabled() && done) {
        LOGGER.debug("Shut down the server.");
      }
    } catch (InterruptedException e) {
      LOGGER.debug("Server shutdown interrupted.");
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken pToken) {
    // TODO Auto-generated method stub
  }

  @Override
  public void messageArrived(String pTopic, MqttMessage pMessage) throws Exception {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Message arrived. Topic:" + pTopic + "\tMessage:" + pMessage
          + "\nSubmitting the message to a new listener thread.");
    }
    ListenerService listenerService = new ListenerService(pTopic, pMessage);
    executorService.submit(listenerService);
  }

}
