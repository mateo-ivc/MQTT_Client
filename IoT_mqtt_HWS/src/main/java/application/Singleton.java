package application;

import javax.swing.JFrame;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Singleton {
	Gui gui;
	MqttClient client;
	OnMessageCallback callBack;

	private static Singleton instance = null;

	private Singleton() {
		
	}

	public static synchronized Singleton getInstance() {
		if (Singleton.instance == null) {
			Singleton.instance = new Singleton();
		}
		return Singleton.instance;
	}

	void connect(String ip, String port, boolean encryptedCon) {
		client = new App().connect(ip, port, encryptedCon);
	}

	void subscribe(String topic) {
		try {
			client.subscribe(topic);
			System.out.println("subscribed to: " + topic);

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	void unsubscribe(String topic) {
		try {
			client.unsubscribe(topic);
			System.out.println("unsubscribed to: " + topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	void quitConnection() {
		try {
			client.disconnect();
			client.close();
			gui.t.stop();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	void abortCon() {
		gui.t.stop();
	}

	JFrame getFrame() {
		return gui.frame;
	}
	void getMessageCallback() {
		
	}
	void displayText() {
		
	}

}
