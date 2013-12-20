package Audio;

import java.io.File;
import java.util.HashMap;


public class Audio {
	
	private static BackgroundMusic music = null;
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	
	public static void playMusic(String musicName) {
		try{
			musicName = "src/Music/" + musicName + ".mp3";
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
	
	public static void initSounds(String[] soundNames){
		try{
			for(String s: soundNames){
				String soundPath = "src/Music/" + s + ".wav";
				Sound sound = new Sound(new File(soundPath));
				sounds.put(s, sound);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void playSound(String soundName) {
		sounds.get(soundName).run();
	}
	
	public static void setVolume(String soundName, float gain){
		if(gain > 6){
			gain = 6;
		}
		else if(gain < -80){
			gain = -80;
		}
		sounds.get(soundName).setVolume(gain);
	}
}
