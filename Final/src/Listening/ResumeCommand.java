package Listening;

import GameStates.gStateMan;

public class ResumeCommand implements Command {

	private gStateMan gsm;
	
	public ResumeCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	@Override
	public void execute() {
		this.gsm.getState(this.gsm.getCurState()).unPause();
	}

}
