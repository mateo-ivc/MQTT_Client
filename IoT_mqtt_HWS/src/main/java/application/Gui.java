package application;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;

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
		frame.getContentPane().add(connect);
		connect.setLayout(null);

		JLabel lblBroker = new JLabel("Broker-IP:");
		lblBroker.setFont(new Font("Arial", Font.BOLD, 12));
		lblBroker.setBounds(screenSize.width / 3, 30, 63, 25);
		connect.add(lblBroker);

		JLabel lblPort = new JLabel("Broker-Port:");
		lblPort.setFont(new Font("Arial", Font.BOLD, 12));
		lblPort.setBounds(screenSize.width / 3, 70, 82, 25);
		connect.add(lblPort);

		txtIP = new JTextField("127.0.0.1");
		txtIP.setFont(new Font("Arial", Font.BOLD, 12));
		txtIP.setBounds((screenSize.width / 2) - 80, 30, 140, 25);
		connect.add(txtIP);

		txtPort = new JTextField("1883");
		txtPort.setFont(new Font("Arial", Font.BOLD, 12));
		txtPort.setBounds((screenSize.width / 2) - 80, 70, 140, 25);
		connect.add(txtPort);

		// whole radiobutton pane
		final JPanel datapane = new JPanel();
		datapane.setSize(screenSize);
		datapane.setVisible(false);
		datapane.setLayout(null);

		// Button to establish connection with the broker just class the method
		// connection in App.java
		JButton btnCon = new JButton("Beam Me Up Scotty");
		// btnCon.addActionListener(l);
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
		final ButtonGroup btnGroup = new ButtonGroup();
		JRadioButton[] jRBtn = new JRadioButton[6];
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String topic = ((JRadioButton) e.getSource()).getText();
				if (topic.equals(temp)) {
					btnGroup.clearSelection();
					singleton.unsubscribe(topic);
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
		subscriberPanel.setBounds(screenSize.width - 270, 0, 255, screenSize.height / 2);
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
				t.stop();
			}
		});
		btnQuit.setBounds(158, 400, 89, 23);
		connect.add(btnQuit);

		JPanel graphPanel = new JPanel();
		graphPanel.setBounds(10, 0, 1597, 540);
		datapane.add(graphPanel);
		graphPanel.setLayout(new CardLayout(0, 0));

		JPanel panel = new JPanel();
		graphPanel.add(panel);

		JPanel panelCo = new JPanel();
		panelCo.setVisible(false);
		graphPanel.add(panelCo);

		JPanel textPanel = new JPanel();
		textPanel.setBackground(Color.ORANGE);
		textPanel.setBounds(10, 551, 1900, 457);
		datapane.add(textPanel);
		textPanel.setLayout(null);

		textPane = new JTextPane();
		textPane.setBounds(0, 5, 578, 322);
		textPane.setFont(new Font("Arial", Font.BOLD, 12));
		textPanel.add(textPane);

	}

	void setInstance() {
		singleton.gui = this.gui;
	}
}