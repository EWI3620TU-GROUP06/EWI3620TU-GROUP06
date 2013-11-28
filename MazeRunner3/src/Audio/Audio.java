package Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {
	
	private static BackgroundMusic music = null;
	
	public static void playMusic(String musicName) {
		try{
			stopMusic();
			musicName = "src/Music/" + musicName + ".wav";
			music = new BackgroundMusic(new File(musicName));	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void stopMusic(){
		if(music != null)
			music.stop();
	}

	public static void playSound(String soundName) {
		try{
			soundName = "src/Music/" + soundName + ".wav";
			new Sound(new File(soundName));	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
