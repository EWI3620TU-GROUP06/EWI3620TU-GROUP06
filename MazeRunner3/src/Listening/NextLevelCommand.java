package Listening;

import java.io.File;

import GameStates.gStateMan;
import MainGame.Maze;
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
		int level = this.gsm.getState(this.gsm.getCurState()).getLevel() + 1;
		if(level > 3)
			level = 1;
		this.gsm.getState(this.gsm.getCurState()).setLevel(level);
		Maze maze = Maze.read(new File("src/Levels/level" + level + ".mz"));
		MazeRunner.setMaze(maze);
		this.gsm.setState(gStateMan.PLAYSTATE);
	}

}
