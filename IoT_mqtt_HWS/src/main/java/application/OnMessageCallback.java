package application;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class OnMessageCallback implements MqttCallback {
	ArrayList<Message> list = new ArrayList<Message>();

	public void connectionLost(Throwable cause) {
		// After the connection is lost, it usually reconnects here
		System.out.println("disconnect，you can reconnect");
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Received Message! Topic: " + topic + " | Message: " + new String(message.getPayload()));
		
		list.add(new Message(topic, new String(message.getPayload())));
		if (list.size() == 11) {
			list.remove(0);
			for (Message value : list) {
				System.out.println(value.getTopic() + " " + value.getMessage());

			}
		}

		

	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("deliveryComplete---------" + token.isComplete());
	}
	
}