package Audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class BackgroundMusic implements Runnable {
	
	private Thread musicThread;
	private Clip clip = null;
	
	public BackgroundMusic(File clipFile)
	{
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(clipFile);

			clip = AudioSystem.getClip();
			clip.open(audioInputStream);

			audioInputStream.close();
			if(musicThread == null)
			musicThread = new Thread(this);

			musicThread.start();
			
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	public void run()
	{
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop()
	{
		clip.stop();
	}

}
