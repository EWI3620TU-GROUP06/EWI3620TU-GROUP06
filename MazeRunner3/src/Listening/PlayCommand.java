package Listening;

import GameStates.gStateMan;

public class PlayCommand implements Command{

	private gStateMan gsm;
	
	public PlayCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		this.gsm.getState(gStateMan.PLAYSTATE).setScore(0);
		this.gsm.getState(gStateMan.PLAYSTATE).setLevel(1);
		this.gsm.setState(gStateMan.PLAYSTATE);
	}

}
