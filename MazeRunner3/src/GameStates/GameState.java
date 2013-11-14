package GameStates;

import javax.media.opengl.GLAutoDrawable;

public abstract class GameState {
	protected gStateMan gsm;
	public abstract void init(GLAutoDrawable drawable);
	public abstract void update();
	public abstract void draw(GLAutoDrawable drawable);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
}
