package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Audio.Audio;
import Main.Game;
import MainGame.OptionsMenu;

public class OptionsState extends GameState {
	
	private OptionsMenu om;
	private Game game;
	private gStateMan gsm;
	
	public OptionsState(gStateMan gsm,Game game){
		this.gsm = gsm;
		this.game = game;
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		om.init(drawable);
	}

	@Override
	public void update() {
		om = new OptionsMenu(game, this);
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		om.display(drawable);
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
		om.reshape(drawable, x, y, width, height);
	}
	
	public GLCanvas getCanvas(){
		return om.getCanvas();
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
}
