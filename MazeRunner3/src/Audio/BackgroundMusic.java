package Audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import Drawing.ErrorMessage;
import javazoom.jl.player.Player;
/**
 * the class background music is used to play the music in the background of the game.
 *
 */
public class BackgroundMusic implements Runnable{

	private Player player = null;
	private static Thread musicThread = null;
	private boolean loop = true;
	private File musicFile;

	public BackgroundMusic(File file)
	{
		try {
			musicFile = file;
			musicThread = new Thread(this);
			musicThread.start();
		} 
		catch(Exception e){
			ErrorMessage.show("Exception while loading music " + file.getName() + ".\n" + e.toString());
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
			ErrorMessage.show("Exception while running music " + musicFile.getName() + ".\n" + e.toString());
		}
	}

	public void stop()
	{
		if(player != null)
		{
			loop = false;
			player.close();
			musicThread.interrupt();
		}
	}
}
