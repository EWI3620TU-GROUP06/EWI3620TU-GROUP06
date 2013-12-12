package Audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class BackgroundMusic implements Runnable{

	private Player player = null;
	private static Thread musicThread = null;
	private boolean loop = true;
	private File musicFile;

	public BackgroundMusic(File file)
	{
		try {
			musicFile = file;
			if(musicThread == null){
				musicThread = new Thread(this);
				musicThread.start();
			}
		} 
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public void run()
	{
		try{
			while(loop){
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(musicFile));
				player = new Player(in);
				player.play();
			}
		}
		catch (Exception e) { 
			e.printStackTrace();
		}
	}

	public void stop()
	{
		if(player != null)
		{
			player.close();
			musicThread.interrupt();
		}
	}
}
