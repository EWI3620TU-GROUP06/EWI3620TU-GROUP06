package Listening;

import GameStates.gStateMan;
import HighScore.SqlReadWrite;

public class DeleteHighscoresCommand implements Command {
	
	private gStateMan gsm = null;
	
	public DeleteHighscoresCommand(gStateMan gsm){
		this.gsm = gsm;
	}
	

	@Override
	public void execute() {
		SqlReadWrite.DeleteHigHscores();
		SqlReadWrite.Read();
		gsm.setState(gStateMan.HIGHSCORESTATE);
		
		
	}

}
