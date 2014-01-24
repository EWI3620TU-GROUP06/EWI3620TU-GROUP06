package Listening;

import GameStates.gStateMan;

public class FullScreenCommand implements Command {

	private gStateMan gsm;
	
	public FullScreenCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		this.gsm.getGame().toggleFullScreen();
	}
}
