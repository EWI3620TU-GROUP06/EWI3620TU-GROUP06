package Audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound implements Runnable{

	private AudioListener listener = null;
	private Clip clip = null;
	private FloatControl volume;
	
	public Sound(File clipFile)
	{
		AudioInputStream audioInputStream = null;
		try {
			listener = new AudioListener();
			audioInputStream = AudioSystem.getAudioInputStream(clipFile);
			clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
			volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			audioInputStream.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setVolume(float gain){
		volume.setValue(gain);
	}

	public void run()
	{
		clip.setMicrosecondPosition((long) 0);
		clip.start();
	}

}
