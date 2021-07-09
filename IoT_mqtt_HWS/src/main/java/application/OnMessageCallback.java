package application;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import utils.Message;
import utils.Revert;

public class OnMessageCallback implements MqttCallback {
	Singleton singleton = Singleton.getInstance();
	ArrayList<Message> list = new ArrayList<Message>();
	ArrayList<Message> reverted = new ArrayList<Message>();

	public void connectionLost(Throwable cause) {
		// After the connection is lost, it usually reconnects here
		singleton.gui.frame.dispose();
		singleton.gui.frame = singleton.gui.firstFrame();
		singleton.abortCon();
		singleton.gui.initialize();
		singleton.gui.encryptedCon = false;
		System.out.println("disconnect，you can reconnect: " + cause);
		JOptionPane.showMessageDialog(singleton.getFrame(), "Whops, something went wrong check your connection.",
				"Error", JOptionPane.ERROR_MESSAGE);

	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {

		// convert MqttMessage to string
		String content = new String(message.getPayload());
		System.out.println(content);
		try {
			// create json object
			JSONObject js = new JSONObject(content);
			// adding message to ArrayList
			list.add(new Message(topic, content));

			// revert list
			reverted = new Revert().revert(list);
			if (list.size() == 11) {
				// update list if we get more then 10 messages
				list.remove(0);
			}
			singleton.displayText(reverted);

			// add data for the chart
			singleton.data.addData(js, topic);
			singleton.chart.lineChart(topic);
		} catch (Exception e) {
			System.out.println("No Json formatt");
		}

	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("deliveryComplete---------" + token.isComplete());
	}

}