package Listening;

import GameStates.gStateMan;
import HighScore.ReadWrite;

public class PlayCommand implements Command{

	private gStateMan gsm;
	
	public PlayCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		this.gsm.setState(gStateMan.PLAYSTATE);
	}

}
