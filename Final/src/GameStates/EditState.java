package GameStates;

import Audio.Audio;

import javax.media.opengl.GLCanvas;

import Main.Game;
import MainGame.MazeEditor;

public class EditState extends GameState {

	private MazeEditor me;
	private Game game;
	private boolean paused;
	private gStateMan gsm;
	
	public EditState(gStateMan gsm, Game game){
		this.gsm = gsm;
		this.game = game;
		this.paused = false;
	}
	


	@Override
	public void update() {
		me = new MazeEditor(game, this);
	}

	@Override
	public GLCanvas getCanvas() {
		return me.getCanvas();
	}

	@Override
	public gStateMan getGSM() {
		return this.gsm;
	}
	
	public boolean getPaused(){
		return this.paused;
	}

	public void setPaused(){
		this.paused = true;
		me.Pause();
	}
	
	public void unPause(){
		this.paused = false;
		me.unPause();
	}
	
	public void playMusic()
	{
		//Editing does not require awesome-o soundtrack
		//Audio.playMusic("ingame");
	}
	
	public void stopMusic(){
		Audio.stopMusic();
	}

	@Override
	public void setOptPaused() {
		me.OptPause();
	}

	@Override
	public void unOptPause() {
		me.unOptPause();
	}

	@Override
	public void setFinished(boolean finished) {
		//do nothing
	}

	@Override
	public boolean getFinished() {
		return false;	
	}

	@Override
	public String getDifficulty() {
		// Should do nothing
		return "";
	}

	@Override
	public void setDifficulty(int diff) {
		//do nothing
	}

	@Override
	public int getDiffNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLevel() {
		// nothing
		return 0;
	}

	@Override
	public void setLevel(int lvl) {
		// TODO nothing
	
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
