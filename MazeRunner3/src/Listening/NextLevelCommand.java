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
//		this.gsm.getGame().remove(this.gsm.getState(this.gsm.getCurState()).getCanvas());
		Maze maze = Maze.read(new File("src/Levels/test.mz"));
		MazeRunner.setMaze(maze);
//		gsm.getInput().reset();
		this.gsm.setState(gStateMan.PLAYSTATE);
//		this.gsm.getState(this.gsm.getCurState()).setFinished(false);
//		new MazeRunner(this.gsm.getGame(), this.gsm.getState(this.gsm.getCurState()));	
//		this.gsm.getGame().validate();
	}

}
