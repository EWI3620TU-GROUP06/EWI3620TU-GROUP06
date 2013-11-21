package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

public abstract class GameState {
	protected gStateMan gsm;
	public abstract void init(GLAutoDrawable drawable);
	public abstract void update();
	public abstract void draw(GLAutoDrawable drawable);
	public abstract void reshape(GLAutoDrawable drawable, int x, int y, int width, int height);
	public abstract GLCanvas getCanvas();
	public abstract gStateMan getGSM();
	public abstract boolean getPaused();
	public abstract void setPaused() throws InterruptedException;
	public abstract void unPause();
}
