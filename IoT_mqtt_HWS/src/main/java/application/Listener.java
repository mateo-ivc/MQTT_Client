package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JRadioButton;

public class Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JRadioButton) {
			String text = ((JRadioButton) e.getSource()).getText();

			System.out.println(text);
		}else if(e.getSource() instanceof JButton) {
			System.out.println("hi");
		}
	}
}
