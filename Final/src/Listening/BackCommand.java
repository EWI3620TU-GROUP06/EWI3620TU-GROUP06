package Listening;

import GameStates.gStateMan;

public class BackCommand implements Command {

	private gStateMan gsm;
	
	public BackCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		this.gsm.getState(this.gsm.getCurState()).unOptPause();
	}

}
