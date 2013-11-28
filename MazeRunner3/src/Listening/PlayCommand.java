package Listening;

import GameStates.gStateMan;

public class PlayCommand implements Command{

	private gStateMan gsm;
	
	public PlayCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		this.gsm.setState(1);
	}

}
