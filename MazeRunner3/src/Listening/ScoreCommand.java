package Listening;

import Drawing.TextBoxManager;
import GameStates.PlayState;
import GameStates.gStateMan;
import HighScore.Score;
import HighScore.SqlReadWrite;

public class ScoreCommand implements Command{
	private TextBoxManager txtbxman;
	private gStateMan gsm;

	public ScoreCommand(TextBoxManager txtbxman, gStateMan gsm)
	{
		this.gsm = gsm;
		this.txtbxman = txtbxman;
	}
	
	@Override
	public void execute() {
		txtbxman.setConfirm();
		String name = txtbxman.getText();
		System.out.println(name);
		if(name != null){
			PlayState playState = (PlayState) gsm.getState(gsm.getCurState());
			SqlReadWrite.Write(new Score(name, playState.getScore()));
			gsm.setState(gStateMan.HIGHSCORESTATE);
		}
	}
	
	

}
