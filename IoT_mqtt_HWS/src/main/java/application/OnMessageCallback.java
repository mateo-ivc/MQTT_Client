package application;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Json.Json;

public class OnMessageCallback implements MqttCallback {
	Singleton singleton = Singleton.getInstance();
	ArrayList<Message> list = new ArrayList<Message>();
	double start = 0;
	String topic;
	String content;

	public void connectionLost(Throwable cause) {
		// After the connection is lost, it usually reconnects here
		System.out.println("disconnect，you can reconnect: " + cause);
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {

		this.topic = topic;

		// convert MqttMessage to string
		content = new String(message.getPayload());
		System.out.println(content);
		content = content.replace("acc ", "");
		content = content.replace("gyr ", "");
		content = content.replace("mag ", "");
		// Warning | have to re-write this block depending how we receive the message
		System.out.println("Received Message! Topic: " + topic + " | Message: " + content);
		// adding message to ArrayList
		list.add(new Message(topic, content));

		if (list.size() == 11) {
			// update list if we get more then 10 messages
			list.remove(0);
		}
		singleton.displayText();
		setData();
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("deliveryComplete---------" + token.isComplete());
	}

	void setData() {
		Gson gson = new Gson();
		Json json = gson.fromJson(content, Json.class);
		// update x and y coordinates of the chart
		singleton.chart.setTime(LocalDateTime.now().getSecond());
		switch (topic) {
		case "Temperature":
			singleton.chart.setX(Double.parseDouble(json.getTemp()));
			break;
		case "Pressure":
			singleton.chart.setX(Double.parseDouble(json.getPress()));
			break;
		case "Humidity":
			singleton.chart.setX(Double.parseDouble(json.getHumidity()));
			break;
		case "Accelleration":
			singleton.chart.setX(Double.parseDouble(json.getX()));
			singleton.chart.setY(Double.parseDouble(json.getY()));
			singleton.chart.setZ(Double.parseDouble(json.getZ()));
			System.out.println("----------");
			System.out.println(Double.parseDouble(json.getX()));
			System.out.println(Double.parseDouble(json.getY()));
			System.out.println(Double.parseDouble(json.getZ()));
			System.out.println("----------");
			break;
		case "Gyrodata":
			singleton.chart.setX(Double.parseDouble(json.getX()));
			singleton.chart.setY(Double.parseDouble(json.getY()));
			singleton.chart.setZ(Double.parseDouble(json.getZ()));
			break;
		case "Magdate":
			singleton.chart.setX(Double.parseDouble(json.getX()));
			singleton.chart.setY(Double.parseDouble(json.getY()));
			singleton.chart.setZ(Double.parseDouble(json.getZ()));
			break;
		default:
			break;
		}
		// displaying chart in the gui
		System.out.println("hi");
		singleton.chart.setDataSet(topic);
	}

}