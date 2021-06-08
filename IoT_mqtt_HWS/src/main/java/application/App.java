package application;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {

	public void connect(String ip, String port, boolean encrypted) {
		String broker;
		if (encrypted == true)
			broker = "ssl://" + ip + ":" + port;
		else
			broker = "tcp://" + ip + ":" + port;

		System.out.println(broker);
		String clientId = "emqx_test";
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			MqttClient client = new MqttClient(broker, clientId, persistence);

			// MQTT connection option
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setUserName("emqx_test");
			connOpts.setPassword("emqx_test_password".toCharArray());
			// retain session
			connOpts.setCleanSession(true);

			OnMessageCallback myCallback = new OnMessageCallback();
			client.setCallback(myCallback);
			// establish a connection
			System.out.println("Connecting to broker: " + broker);
			client.connect(connOpts);
			System.out.println("Connected");

			// Subscription
			Gui gui = new Gui();
			//client.subscribe(gui.text);

			client.disconnect();
			System.out.println("Disconnected");
			client.close();
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}
}