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
}
