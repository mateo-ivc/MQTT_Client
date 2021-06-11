package application;

import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {
	private MqttClient client;
	Singleton singleton = Singleton.getInstance();
	private OnMessageCallback myCallback;

	public App connect(String ip, String port, boolean encrypted) {
		String broker;
		if (encrypted == true)
			broker = "ssl://" + ip + ":" + port;
		else
			broker = "tcp://" + ip + ":" + port;

		String clientId = "emqx_test";
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			client = new MqttClient(broker, clientId, persistence);

			// Callback
			myCallback = new OnMessageCallback();
			client.setCallback(myCallback);

			// MQTT connection option
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setUserName("emqx_test");
			connOpts.setPassword("emqx_test_password".toCharArray());

			// retain session
			connOpts.setCleanSession(true);

			// establish a connection
			System.out.println("Connecting to broker: " + broker);
			client.connect(connOpts);

			System.out.println("Connected");

		} catch (MqttException me) {

			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			JOptionPane.showMessageDialog(singleton.getFrame(), "Couldn't connect pls try again");
			singleton.abortCon();

		}
		return this;

	}

	MqttClient getClient() {
		return client;
	}

	OnMessageCallback getCallback() {
		return myCallback;
	}

}