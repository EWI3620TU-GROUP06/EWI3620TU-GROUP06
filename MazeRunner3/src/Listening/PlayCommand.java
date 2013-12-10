package Listening;

import GameStates.gStateMan;
import HighScore.ReadWrite;
import HighScore.SqlReadWrite;

public class PlayCommand implements Command{

	private gStateMan gsm;
	
	public PlayCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	
	@Override
	public void execute() {
		SqlReadWrite.Write();
		this.gsm.setState(gStateMan.PLAYSTATE);
	}

}
