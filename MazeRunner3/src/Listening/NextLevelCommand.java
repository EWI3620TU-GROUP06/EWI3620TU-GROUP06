package Listening;

import GameStates.gStateMan;


public class NextLevelCommand implements Command {

	private gStateMan gsm;
	
	// TODO gStateMan uit constructor weghalen en vervangen zodat state niet verandert
	public NextLevelCommand(gStateMan gsm){
		this.gsm = gsm;
		//do nothing
	}
	@Override
	public void execute() {
		this.gsm.setState(gStateMan.PLAYSTATE);
//		MazeRunner.setMaze(mz);
		
		
	}

}
