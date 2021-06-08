package application;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Singleton {
	MqttClient client;

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

	void quitConnection() {
		try {
			client.disconnect();
			client.close();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
