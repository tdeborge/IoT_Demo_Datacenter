package com.redhat.demo.iot.datacenter.monitor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTProducer {

	MqttClient client;
	MqttConnectOptions options;
	MemoryPersistence persistence;
	
	public MQTTProducer(String brokerURL, String user, String password, String clientID) {
	
		MemoryPersistence persistence = new MemoryPersistence();
		
		System.out.println("Connecting to "+brokerURL);
		
		try {
			client = new MqttClient(brokerURL, clientID, persistence);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.out);
		}
		
		options = new MqttConnectOptions ();
		options.setUserName(user);
		options.setPassword("change12_me".toCharArray());
		
		try {
			client.connect(options);
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.out);
		}
		
	}
	
	public void run(String mqttTopic, String mqttMessage) throws MqttPersistenceException, MqttException {
		
		MqttMessage message = new MqttMessage();
		
		message.setPayload(mqttMessage.getBytes());
		
		System.out.println("Publishing " + message.getPayload());
		
		client.publish(mqttTopic, message);
		
	}
	
	public void close() throws MqttException{
		client.disconnect();
		client.close();
		
	}
}
