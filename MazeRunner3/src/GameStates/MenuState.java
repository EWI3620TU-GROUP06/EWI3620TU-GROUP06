package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Audio.Audio;
import Main.Game;
import MainGame.MainMenu;

/**
 * the menustate is the fist state in the game and is state you constantly return when you want switch from playing 
 * the game and editing levels.
 *
 */

public class MenuState extends GameState {
	
	private MainMenu mm;
	private Game game;
	private gStateMan gsm;
	
	public MenuState(gStateMan gsm,Game game){
		this.gsm = gsm;
		this.game = game;
	}
	@Override
	
	public void update(){
		mm = new MainMenu(game, this);
	}
	
	public void init(GLAutoDrawable drawable) {
		mm.init(drawable);
		System.out.println("init gedaan mainmenu");
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		mm.display(drawable);
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
		mm.reshape(drawable, x, y, width, height);
	}
	
	public GLCanvas getCanvas(){
		return mm.getCanvas();
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
		mm.setOptionsMenu();
	}
	@Override
	public void unOptPause() {
		mm.setMainMenu();
	}
	@Override
	public void setFinished(boolean finished) {
		// do nothing
	}
	@Override
	public boolean getFinished() {
		return false;
		// do nothing
	}
	@Override
	public String getDifficulty() {
		// do not a fucking thing
		return "";
	}
	@Override
	public void setDifficulty(int diff) {
		// nothingness
		
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
