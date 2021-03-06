package GameStates;

import javax.media.opengl.GLCanvas;

import Audio.Audio;
import Main.Game;
import MainGame.HighscoreMenu;

/**
 * the highscore state is switched on when you are done playing or when you die, when you switch to this state 
 * the higscore menu is drawn.
 *
 */

public class HighscoreState extends GameState {

	private HighscoreMenu hm;
	private Game game;
	private gStateMan gsm;
	
	public HighscoreState(gStateMan gsm,Game game){
		this.gsm = gsm;
		this.game = game;
	}
	@Override
	
	public void update(){
		hm = new HighscoreMenu(game, this);
	}
	
	public GLCanvas getCanvas(){
		return hm.getCanvas();
	}
	
	public gStateMan getGSM(){
		return this.gsm;
	}
	@Override
	public boolean getPaused() {
		return false;
	}
	@Override
	public void setPaused() {
		//do nothing
	}
	
	public void unPause(){
		//do nothing
	}
	
	public void playMusic()
	{
		Audio.playMusic("menu");
	}
	
	public void stopMusic(){
		Audio.stopMusic();
	}
	
	@Override
	public void setOptPaused() {
		// do nothing
	}
	@Override
	public void unOptPause() {
		// do nothing
	}
	@Override
	public void setFinished(boolean finished) {
		// do nothing	
	}
	@Override
	public boolean getFinished() {
		return false;
	}
	@Override
	public String getDifficulty() {
		// do nothing
		return "";
	}
	@Override
	public void setDifficulty(int diff) {
		// do less than nothing
		
	}
	@Override
	public int getDiffNumber() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getLevel() {
		// TODO nothing
		return 0;
	}
	@Override
	public void setLevel(int lvl) {
		// nothing
		
	}
	@Override
	public void setScore(int scr) {
		// TODO noting
		
	}

	@Override
	public int getScore() {
		// TODO noting
		return 0;
	}

}
