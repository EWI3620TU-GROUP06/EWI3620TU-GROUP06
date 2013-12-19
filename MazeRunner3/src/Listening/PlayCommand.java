package Listening;

import java.io.File;

import GameStates.gStateMan;
import MainGame.Level;
import MainGame.MazeRunner;

public class PlayCommand implements Command{

	private gStateMan gsm;
	
	public PlayCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		this.gsm.getState(gStateMan.PLAYSTATE).setScore(0);
		this.gsm.getState(gStateMan.PLAYSTATE).setLevel(1);
		Level level = Level.readLevel(new File("src/Levels/level1" + ".mz"));
		MazeRunner.setLevel(level);
		this.gsm.setState(gStateMan.PLAYSTATE);
	}

}
