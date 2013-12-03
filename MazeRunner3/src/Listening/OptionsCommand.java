package Listening;

import GameStates.gStateMan;

public class OptionsCommand implements Command {

	private gStateMan gsm;
	
	public OptionsCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	@Override
	public void execute() {
		this.gsm.setState(gStateMan.OPTSTATE);
	}
}
