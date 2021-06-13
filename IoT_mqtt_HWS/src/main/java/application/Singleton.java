package application;

import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Singleton {
	Gui gui;
	App app;
	MqttClient client;
	OnMessageCallback callBack;
	DrawChart chart;
	private boolean start = false;

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
		app = new App().connect(ip, port, encryptedCon);
		client = app.getClient();
		callBack = app.getCallback();
	}

	void subscribe(String topic) {
		try {
			client.subscribe(topic);
			System.out.println("subscribed to: " + topic);
			if (chart == null)
				chart = new DrawChart();
			if (!start) {
				chart.lineChart(topic);
				start = true;
			} else {
				chart.lineChart(topic);
			}
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

	void displayText() {
		StyledDocument doc = (StyledDocument) gui.textPane.getDocument();
		gui.textPane.setText("");
		try {
			for (Message value : callBack.list) {
				doc.insertString(0, value.getTopic() + ": " + value.getMessage() + "\n \n", null);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}
