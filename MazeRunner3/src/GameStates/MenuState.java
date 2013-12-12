package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Audio.Audio;
import Main.Game;
import MainGame.MainMenu;

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
}
