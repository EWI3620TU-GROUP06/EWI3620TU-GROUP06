package Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {

	public static void playClip(File clipFile) {
		AudioListener listener = null;
		AudioInputStream audioInputStream = null;
		try {
			listener = new AudioListener();
			audioInputStream = AudioSystem.getAudioInputStream(clipFile);

			Clip clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
			try {
				clip.start();
				listener.waitUntilDone();
			} finally {
				clip.close();
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			try{
				audioInputStream.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
