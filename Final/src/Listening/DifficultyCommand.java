package Listening;

import Drawing.TextBoxManager;
import GameStates.gStateMan;

public class DifficultyCommand implements Command {
	
	private gStateMan gsm;
	private TextBoxManager res;
	
	public DifficultyCommand(gStateMan gsm, TextBoxManager res){
		this.gsm = gsm;
		this.res = res;
	}
	
	@Override
	public void execute() {
		int diff = gsm.getState(gStateMan.PLAYSTATE).getDiffNumber();
		if(diff < 2){
			gsm.getState(gStateMan.PLAYSTATE).setDifficulty(diff + 1);
		}
		else{
			gsm.getState(gStateMan.PLAYSTATE).setDifficulty(0);
		}
		res.setChangableText(gsm.getState(gStateMan.PLAYSTATE).getDifficulty());
	}
}
