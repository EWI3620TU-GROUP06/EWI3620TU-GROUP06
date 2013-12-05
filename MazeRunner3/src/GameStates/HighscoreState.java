package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Audio.Audio;
import Main.Game;
import MainGame.HighscoreMenu;
import MainGame.MainMenu;

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
	
	public void init(GLAutoDrawable drawable) {
		hm.init(drawable);
		System.out.println("init gedaan mainmenu");
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		hm.display(drawable);
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
		hm.reshape(drawable, x, y, width, height);
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
		Audio.playMusic("test");
	}
	@Override
	public void setOptPaused() {
		// do nothing
	}
	@Override
	public void unOptPause() {
		// do nothing
	}

}
