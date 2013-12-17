package Listening;

import java.io.File;

import GameStates.gStateMan;
import MainGame.Maze;
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
		Maze maze = Maze.read(new File("src/Levels/level1" + ".mz"));
		MazeRunner.setMaze(maze);
		this.gsm.setState(gStateMan.PLAYSTATE);
	}

}
