package edu.mit.appinventor.raspberrypi;

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

  MqttClient mClient;

  private ExecutorService executorService;

  private final Logger LOGGER = LoggerFactory.getLogger(Server.class);

  private static final int THREAD_POOL_SIZE = 10;

  public Server() {

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

  private void subscribeToInternalTopic() {
    String topic = Topic.INTERNAL.toString();
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

  public static void main(String[] args) {
    Server server = new Server();
    server.startServer();
  }

  private void startServer() {
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
