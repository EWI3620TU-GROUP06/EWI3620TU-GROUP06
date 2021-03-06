package Audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import Drawing.ErrorMessage;

/**
 * the sound is used to play the several short sounds in the game like the finish sound.
 *
 */

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
			ErrorMessage.show("Exception while playing sound " + clipFile.getName() + ".\n" + e.toString());
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
