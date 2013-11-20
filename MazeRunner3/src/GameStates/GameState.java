package GameStates;

import javax.media.opengl.GLAutoDrawable;

import MainGame.UserInput;

public abstract class GameState {
	protected gStateMan gsm;
	public abstract void init(GLAutoDrawable drawable);
	public abstract void update();
	public abstract void draw(GLAutoDrawable drawable);
	public abstract void reshape(GLAutoDrawable drawable, int x, int y, int width, int height);
	public abstract UserInput getInput();
}
