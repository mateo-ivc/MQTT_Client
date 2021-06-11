package application;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;

public class OnMessageCallback implements MqttCallback {
	Singleton singleton = Singleton.getInstance();
	ArrayList<Message> list = new ArrayList<Message>();
	double start = 0;

	String topic;
	String[] parts;

	public void connectionLost(Throwable cause) {
		// After the connection is lost, it usually reconnects here
		System.out.println("disconnect，you can reconnect: " + cause);
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		this.topic = topic;
		String content = new String(message.getPayload());
		content = content.replaceAll("'", "");
		System.out.println("Received Message! Topic: " + topic + " | Message: " + new String(message.getPayload()));
		list.add(new Message(topic, content));

		parts = content.split(":");
		if (list.size() == 11) {
			list.remove(0);
		}
		singleton.displayText();

		singleton.chart.setX(LocalDateTime.now().getSecond());
		singleton.chart.setY(Double.parseDouble(parts[1]));
		singleton.chart.setDataSet(topic);

		// singleton.chart.setDataSet(topic, time.now().getSecond(),
		// Double.parseDouble(parts[1]));
		// singleton.chart.lineChart(topic, time.now().getSecond(),
		// Double.parseDouble(parts[1]));

	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("deliveryComplete---------" + token.isComplete());
	}

	void setData() {

	}

}