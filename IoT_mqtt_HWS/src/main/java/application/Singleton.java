package application;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.jfree.data.time.Second;

import data.DataProcessing;
import gui.DrawChart;
import gui.Gui;
import utils.Message;

public class Singleton {
	public Gui gui;
	App app;
	public MqttClient client;
	OnMessageCallback callBack;
	public DrawChart chart;
	Second time = new Second();
	public DataProcessing data;

	private static Singleton instance = null;

	private Singleton() {

	}

	public static synchronized Singleton getInstance() {
		if (Singleton.instance == null) {
			Singleton.instance = new Singleton();
		}
		return Singleton.instance;
	}

	public void connect(String ip, String port, boolean encryptedCon) {
		app = new App().connect(ip, port, encryptedCon);
		client = app.getClient();
		callBack = app.getCallback();
	}

	public void subscribe(String topic) {
		try {
			client.subscribe(topic);
			System.out.println("subscribed to: " + topic);

			if (chart == null)
				chart = new DrawChart();

			if (data == null)
				data = new DataProcessing();
			chart.lineChart(topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void unsubscribe(String topic) {
		try {
			client.unsubscribe(topic);
			System.out.println("unsubscribed to: " + topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void quitConnection() {
		try {
			client.disconnect();
			client.close();
			abortCon();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public void abortCon() {
		gui.connThread.stop();
	}

	JFrame getFrame() {
		return gui.frame;
	}

	void displayText(ArrayList<Message> list) {
		StyledDocument doc = (StyledDocument) gui.textPane.getDocument();
		gui.textPane.setText("");
		try {
			for (Message value : list) {

				doc.insertString(0, value.getTopic() + ": " + value.getMessage() + "\n \n", null);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}
