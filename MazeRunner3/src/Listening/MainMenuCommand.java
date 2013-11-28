package Listening;

import GameStates.gStateMan;

public class MainMenuCommand implements Command {

	private gStateMan gsm;
	
	public MainMenuCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		this.gsm.setState(0);
	}
}
