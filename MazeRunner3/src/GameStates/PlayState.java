package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Audio.Audio;
import Main.Game;
import MainGame.MazeRunner;

public class PlayState extends GameState {
	
	private gStateMan gsm;
	private MazeRunner mz;
	private Game game;
	private boolean finished;
	private boolean paused;
	
	public PlayState(gStateMan gsm, Game game){
		this.gsm = gsm;
		this.game = game;
		this.paused = false;
		this.finished = false;
	}
	@Override
	public void init(GLAutoDrawable drawable) {
		mz.init(drawable);
	}

	public void update(){
		mz = new MazeRunner(game,this);
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		mz.display(drawable);
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
		mz.reshape(drawable,x,y,width,height);
	}
	
	public GLCanvas getCanvas(){
		return mz.getCanvas();
	}
	
	public gStateMan getGSM(){
		return this.gsm;
	}
	
	public boolean getPaused(){
		return paused;
	}
	
	public void setPaused(){
		this.paused = true;
		mz.Pause();
	}
	
	public void unPause(){
		this.paused = false;
		mz.unPause();
	}
	
	public void playMusic()
	{
		Audio.playMusic("menu");
	}
	@Override
	public void setOptPaused() {
		mz.OptPause();
	}
	@Override
	public void unOptPause() {
		mz.unOptPause();
	}
	@Override
	public void setFinished(boolean finished) {
		this.finished = finished;
		
	}
	@Override
	public boolean getFinished() {
		return finished;
		
	}
}
