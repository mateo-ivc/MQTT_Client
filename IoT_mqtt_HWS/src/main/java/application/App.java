package application;

import java.util.UUID;

import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {

	private MqttClient client;
	Singleton singleton = Singleton.getInstance();
	private OnMessageCallback myCallback;
	static String caFilePath = "homeCerts\\ca.pem";
	static String clientCrtFilePath = "homeCerts\\client.pem";
	static String clientKeyFilePath = "homeCerts\\clientkey.pem";
	static String userName = "guest";
	static String password = "45";
	public App connect(String ip, String port, boolean encrypted) {
		String broker;
		if (encrypted)
			broker = "ssl://" + ip + ":" + port;
		else
			broker = "tcp://" + ip + ":" + port;

		String clientId = " "+ UUID.randomUUID();
		MemoryPersistence persistence = new MemoryPersistence();
		SSLSocketFactory socketFactory = null;

		try {
			client = new MqttClient(broker, clientId, persistence);

			// Callback
			myCallback = new OnMessageCallback();
			client.setCallback(myCallback);

			// MQTT connection option
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setUserName(userName);
			connOpts.setPassword(password.toCharArray());
			connOpts.setKeepAliveInterval(60);
			
			try {
				if (encrypted) {
					// SSL SockerFactory
					
					socketFactory = SocketFactory.getSocketFactory(caFilePath, clientCrtFilePath, clientKeyFilePath, "");
					connOpts.setSocketFactory(socketFactory);
				}
			} catch (Exception e1) {
				System.out.println("whops something isn't working");
				e1.printStackTrace();
			}

			// retain session
			connOpts.setCleanSession(true);

			// establish a connection
			System.out.println("Connecting to broker: " + broker);
			client.connect(connOpts);

			System.out.println("Connected");

		} catch (MqttException me) {

//			System.out.println("reason " + me.getReasonCode());
//			System.out.println("msg " + me.getMessage());
//			System.out.println("loc " + me.getLocalizedMessage());
//			System.out.println("cause " + me.getCause());
//			System.out.println("excep " + me);
			JOptionPane.showMessageDialog(singleton.getFrame(), "Couldn't connect pls try again \n", "Error",
					JOptionPane.ERROR_MESSAGE);

			singleton.abortCon();

		} catch (Exception e) {
			e.printStackTrace();
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
