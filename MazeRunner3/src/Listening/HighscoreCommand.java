package Listening;

import GameStates.gStateMan;

public class HighscoreCommand implements Command{
	
	private gStateMan gsm = null;
	
	public HighscoreCommand(gStateMan gsm)
	{
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		gsm.setState(gStateMan.HIGHSCORESTATE);
	}

	
	
}
