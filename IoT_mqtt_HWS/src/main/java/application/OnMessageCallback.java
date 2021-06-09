package application;

import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class OnMessageCallback implements MqttCallback {
	HashMap<String, String> list = new HashMap<String, String>();

	public void connectionLost(Throwable cause) {
		// After the connection is lost, it usually reconnects here
		System.out.println("disconnect，you can reconnect");
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Received Message! Topic: " + topic + " | Message: " + new String(message.getPayload()));
		list.put(topic, new String(message.getPayload()));

	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("deliveryComplete---------" + token.isComplete());
	}
}