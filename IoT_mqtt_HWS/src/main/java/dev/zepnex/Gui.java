package dev.zepnex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class Gui {

	private JFrame frame;
	private JTextField txtIP;
	private JTextField txtPort;
	boolean encryptedCon = false;

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
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame = new JFrame();
		frame.setSize(screenSize);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Connection frame which will hide after clicking Beam Me Up Scotty
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
		
		
		//Second pane to use if u want to display all the info
		final JPanel panel = new JPanel();
		panel.setSize(screenSize);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		
		// Button to establish connection with the broker just class the method connection in App.java

		JButton btnCon = new JButton("Beam Me Up Scotty");
		btnCon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect.setVisible(false);
				panel.setBackground(Color.black);
				new App().connect(txtIP.getText(), txtPort.getText(), encryptedCon);

			}
		});
		btnCon.setFont(new Font("Arial", Font.BOLD, 12));
		btnCon.setBounds((screenSize.width / 2) - 200, 400, 408, 23);
		connect.add(btnCon);

		//Toggle Button -> if u want encrypted connection or not will just change the boolean encryptedCon
		final JToggleButton tb = new JToggleButton("not encrypted connection");
		tb.setFont(new Font("Arial", Font.BOLD, 12));
		tb.setBounds((screenSize.width / 2) - 85, 200, 181, 23);
		connect.add(tb);

		tb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton tBtn = (JToggleButton) e.getSource();
				//Toggles the boolean
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

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}
}
