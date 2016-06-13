package edu.mit.appinventor.raspberrypi.gpio;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class SensorInput implements GpioPinListenerDigital{

  final GpioController gpio = GpioFactory.getInstance();

  final GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, "myButton");

  public void doDemo(){
    button.addListener(this);
    while (true) {
      try {
	Thread.sleep(1000);
      } catch (InterruptedException e) {
	gpio.shutdown();
      }
    }

  }
  public static void main(String[] args) {
    SensorInput mySwitchButton = new SensorInput(); 
    mySwitchButton.doDemo();
  }

  @Override
  public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
    if (event.getState().isHigh()) {
      sendMessage();
    }
  }
  
  private void sendMessage() {
    
    String topic        = "temperature";
    String content      = "25";
    int qos             = 2;
    String broker       = "tcp://192.168.0.9:1883";
    String clientId     = "JavaSample";
    MemoryPersistence persistence = new MemoryPersistence();

    try {
      
        MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println("Connecting to broker: "+broker);
        sampleClient.connect(connOpts);
        System.out.println("Connected");
        System.out.println("Publishing message: "+content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        sampleClient.publish(topic, message);
        System.out.println("Message published");
        sampleClient.disconnect();
        System.out.println("Disconnected");
        System.exit(0);
    } catch(MqttException me) {
        System.out.println("reason "+me.getReasonCode());
        System.out.println("msg "+me.getMessage());
        System.out.println("loc "+me.getLocalizedMessage());
        System.out.println("cause "+me.getCause());
        System.out.println("excep "+me);
        me.printStackTrace();
    }
  }
  

}
