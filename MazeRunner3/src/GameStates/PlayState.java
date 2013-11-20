package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Main.Game;
import MainGame.MazeRunner;

public class PlayState extends GameState {
	
	private MazeRunner mz;
	private Game game;
	
	public PlayState(gStateMan gsm, Game game){
		this.gsm = gsm;
		this.game = game;
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
}
