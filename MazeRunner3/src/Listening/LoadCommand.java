package Listening;

import GameObjects.Editor;
import GameStates.gStateMan;
import MainGame.Level;
import MainGame.MazeRunner;

public class LoadCommand implements Command {
	
	private gStateMan gsm;

	public LoadCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		//Load level to static maze-variable, and GOTO playstate
		Level level = Editor.readLevel();
		MazeRunner.setLevel(level);
		this.gsm.getState(gStateMan.PLAYSTATE).setScore(0);
		this.gsm.getState(gStateMan.PLAYSTATE).setLevel(0);
		if(level != null) { // If level loaded, play, else choose some other menu option
			this.gsm.setState(gStateMan.PLAYSTATE);
		}
	}

}
