package org.unifi.turing.go;

import javax.swing.JOptionPane;

public class Message {
	public static void showErrorMessage(String errorMessage, String errorTitle) {
		JOptionPane.showMessageDialog(null, errorMessage, errorTitle,
				JOptionPane.ERROR_MESSAGE);
	}
	public static void showInfoMessage(String errorMessage, String errorTitle) {
		JOptionPane.showMessageDialog(null, errorMessage, errorTitle,
				JOptionPane.INFORMATION_MESSAGE);
	}
}
