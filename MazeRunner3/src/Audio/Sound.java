package Audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound implements Runnable{

	private Thread soundThread;
	private AudioListener listener = null;
	private Clip clip = null;
	
	public Sound(File clipFile)
	{
		AudioInputStream audioInputStream = null;
		try {
			listener = new AudioListener();
			audioInputStream = AudioSystem.getAudioInputStream(clipFile);
			clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);

			audioInputStream.close();
			
			soundThread = new Thread(this);
			soundThread.start();
			
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	public void run()
	{
		clip.setMicrosecondPosition((long) 0);
		clip.start();
	}

}
