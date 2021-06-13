package application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;

public class Gui {
	public Gui gui = this;
	Singleton singleton = Singleton.getInstance();
	public JFrame frame;
	private JTextField txtIP;
	private JTextField txtPort;
	private boolean encryptedCon = false;
	public Thread t;
	private String temp = "";
	JTextPane textPane;
	JPanel graphPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
					window.setInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String[] topics = { "Temperature", "Pressure", "Humidity", "Accelleration", "Gyrodata", "Magdata" };
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		frame = new JFrame();
		frame.setSize(screenSize);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// Connection frame which will hide after clicking Beam Me Up Scotty
		final JPanel connect = new JPanel();
		connect.setSize(screenSize);
		connect.setLayout(null);
		frame.getContentPane().add(connect);

		JLabel lblBroker = new JLabel("Broker-IP:");
		lblBroker.setFont(new Font("Arial", Font.BOLD, 12));
		lblBroker.setBounds(screenSize.width / 3, 30, 63, 25);
		connect.add(lblBroker);

		JLabel lblPort = new JLabel("Broker-Port:");
		lblPort.setFont(new Font("Arial", Font.BOLD, 12));
		lblPort.setBounds(screenSize.width / 3, 70, 82, 25);
		connect.add(lblPort);

		txtIP = new JTextField("test.mosquitto.org");
		txtIP.setFont(new Font("Arial", Font.BOLD, 12));
		txtIP.setBounds((screenSize.width / 2) - 80, 30, 140, 25);
		connect.add(txtIP);

		txtPort = new JTextField("1883");
		txtPort.setFont(new Font("Arial", Font.BOLD, 12));
		txtPort.setBounds((screenSize.width / 2) - 80, 70, 140, 25);
		connect.add(txtPort);

		// whole pane for displaying all informations
		final JPanel datapane = new JPanel();
		datapane.setSize(screenSize);
		datapane.setVisible(false);
		datapane.setLayout(null);

		// Button to establish connection with the broker just class the method
		// connection in App.java
		JButton btnCon = new JButton("Beam Me Up Scotty");
		btnCon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t = new Thread() {
					public void run() {
						singleton.connect(txtIP.getText(), txtPort.getText(), encryptedCon);
						connect.setVisible(false);
						datapane.setVisible(true);

					}
				};
				t.start();
			}
		});
		btnCon.setFont(new Font("Arial", Font.BOLD, 12));
		btnCon.setBounds((screenSize.width / 2) - 200, 400, 408, 23);
		connect.add(btnCon);

		// Toggle Button -> if u want encrypted connection or not will just change the
		// boolean encryptedCon
		final JToggleButton tb = new JToggleButton("not encrypted connection");
		tb.setFont(new Font("Arial", Font.BOLD, 12));
		tb.setBounds((screenSize.width / 2) - 85, 200, 181, 23);
		connect.add(tb);

		tb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton tBtn = (JToggleButton) e.getSource();
				// Toggles the boolean
				if (tBtn.isSelected()) {
					tb.setText("encrypted connection");
					txtPort.setText("8883");
					encryptedCon = true;
				} else {
					tb.setText("not encrypted connection");
					txtPort.setText("1883");
					encryptedCon = false;
				}
			}
		});

		// create array of radiobutton
		// subscribe per click and vice versa
		final ButtonGroup btnGroup = new ButtonGroup();
		JRadioButton[] jRBtn = new JRadioButton[6];
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String topic = ((JRadioButton) e.getSource()).getText();
				if (topic.equals(temp)) {
					btnGroup.clearSelection();
					singleton.unsubscribe(topic);
					temp = "";
				} else {
					if (!temp.isEmpty())
						singleton.unsubscribe(temp);
					temp = topic;
					singleton.subscribe(topic);

				}
			}
		};

		// whole radiobutton scroll pane
		JPanel subscriberPanel = new JPanel();
		subscriberPanel.setBounds(screenSize.width - 255, 0, 255, 460);
		subscriberPanel.setLayout(new GridLayout(7, 1));
		datapane.add(subscriberPanel);

		for (int i = 0; i < jRBtn.length; i++) {
			// initialize btn and add it to pane
			subscriberPanel.add(jRBtn[i] = new JRadioButton(topics[i]));
			jRBtn[i].addActionListener(listener);
			btnGroup.add(jRBtn[i]);
		}

		frame.getContentPane().add(datapane);

		// Disconnect Button in Connect Panel
		JButton btnQuit = new JButton("Disconnect");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(t != null)
					t.stop();
			}
		});
		btnQuit.setBounds(158, 400, 89, 23);
		connect.add(btnQuit);

		graphPanel = new JPanel();
		graphPanel.setBounds(0, 0, screenSize.width - 255, 460);
		graphPanel.setLayout(null);
		datapane.add(graphPanel);


		JPanel textPanel = new JPanel();
		textPanel.setBackground(Color.ORANGE);
		textPanel.setBounds(0, screenSize.height-360, screenSize.width, 300);
		textPanel.setLayout(null);
		datapane.add(textPanel);

		textPane = new JTextPane();
		textPane.setFont(new Font("Arial", Font.BOLD, 12));
		textPane.setBounds(0,0,screenSize.width, 300);
		textPane.setVisible(true);
		textPanel.add(textPane);
		

	}

	void setInstance() {
		singleton.gui = this.gui;
	}
}