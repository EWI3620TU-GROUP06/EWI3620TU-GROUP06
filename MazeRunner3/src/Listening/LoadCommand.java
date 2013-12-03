package Listening;

import GameObjects.Editor;
import GameStates.gStateMan;
import MainGame.Maze;
import MainGame.MazeRunner;

public class LoadCommand implements Command {
	
	private gStateMan gsm;

	public LoadCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		//Load level to static maze-variable, and GOTO playstate
		Maze maze = Editor.readMaze();
		MazeRunner.setMaze(maze);
		if(maze != null) { // If level loaded, play, else choose some other menu option
			this.gsm.setState(gStateMan.PLAYSTATE);
		}
	}

}
