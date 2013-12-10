package Audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class BackgroundMusic implements Runnable{
	
	Player player = null;
	
	public BackgroundMusic(File clipFile)
	{
		try {
			FileInputStream fis     = new FileInputStream(clipFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);			
		} 
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void run()
	{
		try{
			player.play();
		}
		catch (Exception e) { 
			System.out.println(e); 
		}
	}
	
	public void stop()
	{
		player.close();
	}

}
