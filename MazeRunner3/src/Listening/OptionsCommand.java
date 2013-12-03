package Listening;

import GameStates.gStateMan;

public class OptionsCommand implements Command {

	private gStateMan gsm;
	
	public OptionsCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	@Override
	public void execute() {
		if(this.gsm.getCurState() == gStateMan.MENUSTATE){
			this.gsm.setState(gStateMan.OPTSTATE);
		}
		else{
			this.gsm.getState(this.gsm.getCurState()).setOptPaused();
		}
	}
}
