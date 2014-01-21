package Drawing;

import javax.swing.JOptionPane;

public class ErrorMessage {
	
	public static void show(String message)
	{
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
