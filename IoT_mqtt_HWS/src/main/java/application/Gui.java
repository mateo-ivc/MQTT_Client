package application;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class Gui {
	Singleton singleton = Singleton.getInstance();
	private JFrame frame;
	private JTextField txtIP;
	private JTextField txtPort;
	private boolean encryptedCon = false;
	private Thread t;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
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

		txtIP = new JTextField();
		txtIP.setFont(new Font("Arial", Font.BOLD, 12));
		txtIP.setBounds((screenSize.width / 2) - 80, 30, 140, 25);
		connect.add(txtIP);

		txtPort = new JTextField("1883");
		txtPort.setFont(new Font("Arial", Font.BOLD, 12));
		txtPort.setBounds((screenSize.width / 2) - 80, 70, 140, 25);
		connect.add(txtPort);
		Listener l = new Listener();

		// whole radiobutton scroll pane
		JPanel subscribePane = new JPanel();
		// create scroll pane and add it to a normal pane
		final JScrollPane scrollPane = new JScrollPane(subscribePane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(screenSize.width - 225, 0, 255, screenSize.height / 2);
		scrollPane.setVisible(false);
		// create array of radiobutton
		ButtonGroup btnGroup = new ButtonGroup();
		JRadioButton[] jRBtn = new JRadioButton[6];
		for (int i = 0; i < jRBtn.length; i++) {
			// initialize btn and add it to pane
			subscribePane.add(jRBtn[i] = new JRadioButton(topics[i]));
			jRBtn[i].addActionListener(l);
			btnGroup.add(jRBtn[i]);

		}

		subscribePane.setLayout(new GridLayout(jRBtn.length, 0, 0, 0));
		// scrollPane.setBounds(1131, 0, 225, 439);
		frame.getContentPane().add(scrollPane);

		// Button to establish connection with the broker just class the method
		// connection in App.java
		JButton btnCon = new JButton("Beam Me Up Scotty");
		// btnCon.addActionListener(l);
		btnCon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("hi");
				t = new Thread() {
					public void run() {
						System.out.println("hi213");
						singleton.connect(txtIP.getText(), txtPort.getText(), encryptedCon);
						connect.setVisible(false);
						scrollPane.setVisible(true);

					}
				};
				t.start();
				System.out.println("hi2");
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

		JButton btnQuit = new JButton("Disconnect");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.stop();
			}
		});
		btnQuit.setBounds(158, 400, 89, 23);
		connect.add(btnQuit);

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
	}
}