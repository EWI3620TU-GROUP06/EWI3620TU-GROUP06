package GameStates;

import javax.media.opengl.GLAutoDrawable;

import Main.Game;
import MainGame.MazeRunner;

public class PlayState extends GameState {
	private MazeRunner mz;
	public PlayState(gStateMan g, Game game){
		mz = new MazeRunner(game);
		System.out.println("mazerunnen is geactiveerd");
	}
	@Override
	public void init(GLAutoDrawable drawable) {
		mz.init(drawable);
		System.out.println("init gedaan");

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		mz.display(drawable);
		System.out.println("drawgedaan");

	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

}
