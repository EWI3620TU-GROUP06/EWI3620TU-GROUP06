package Audio;

import java.io.File;


public class Audio {
	
	private static BackgroundMusic music = null;
	private static String previousSound = "";
	private static Sound s;
	
	public static void playMusic(String musicName) {
		try{
//			musicName = "src/Music/" + musicName + ".mp3";
//			music = new BackgroundMusic(new File(musicName));
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
		if(!soundName.equals(previousSound)){
			try{
				soundName = "src/Music/" + soundName + ".wav";
				s = new Sound(new File(soundName));
				previousSound = soundName;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			s.run();
		}
	}
}
