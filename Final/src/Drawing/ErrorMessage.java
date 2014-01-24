package Drawing;

import javax.swing.JOptionPane;

public class ErrorMessage implements Runnable {
	
	private String message;
	
	public ErrorMessage(String message)
	{
		this.message = message;
	}
	
	public static void show(String message)
	{
		new Thread(new ErrorMessage(message)).start();
	}

	public void run() {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
