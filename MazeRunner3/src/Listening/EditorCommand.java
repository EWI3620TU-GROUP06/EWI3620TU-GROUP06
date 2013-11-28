package Listening;

import GameStates.gStateMan;

public class EditorCommand implements Command {

	private gStateMan gsm;
	
	public EditorCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		this.gsm.setState(2);
	}

}
