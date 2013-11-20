package GameStates;

import javax.media.opengl.GLAutoDrawable;

import Main.Game;
import MainGame.MazeRunner;
import MainGame.UserInput;

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
		System.out.println("init gedaan mazerunner");

	}

	public void update(){
		mz = new MazeRunner(game,this);
		System.out.println("mazerunnen is geactiveerd");
	}

	public UserInput getInput(){
		return this.gsm.getInput();
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		mz.display(drawable);
		//System.out.println("drawgedaan mazerunner");
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
		mz.reshape(drawable,x,y,width,height);
	}
}
