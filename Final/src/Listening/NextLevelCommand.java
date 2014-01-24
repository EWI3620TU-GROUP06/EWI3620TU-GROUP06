package Listening;

import java.io.File;

import GameStates.gStateMan;
import LevelHandling.Level;
import MainGame.MazeRunner;

public class NextLevelCommand implements Command {

	private gStateMan gsm;
	
	// TODO gStateMan uit constructor weghalen en vervangen zodat state niet verandert
	public NextLevelCommand(gStateMan gsm){
		this.gsm = gsm;
		//do nothing
	}
	@Override
	public void execute() {
		int lvl = this.gsm.getState(this.gsm.getCurState()).getLevel() + 1;
		if(lvl > MazeRunner.getFinalLevel())
			lvl = 1;
		this.gsm.getState(this.gsm.getCurState()).setLevel(lvl);
		Level level = Level.readLevel(new File("src/Levels/level" + lvl + ".mz"));
		MazeRunner.setLevel(level);
		this.gsm.setState(gStateMan.PLAYSTATE);
	}

}
