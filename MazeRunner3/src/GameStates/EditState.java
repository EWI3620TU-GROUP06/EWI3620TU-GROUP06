package GameStates;

import java.io.File;

import Audio.Audio;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Main.Game;
import MainGame.MazeEditor;

public class EditState extends GameState {

	private MazeEditor me;
	private Game game;
	private boolean paused;
	
	public EditState(gStateMan gsm, Game game){
		this.gsm = gsm;
		this.game = game;
		this.paused = false;
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		me.init(drawable);
	}

	@Override
	public void update() {
		me = new MazeEditor(game, this);
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		me.display(drawable);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		me.reshape(drawable,x, y, width, height);
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
		Audio.playMusic("test2");
	}
}
