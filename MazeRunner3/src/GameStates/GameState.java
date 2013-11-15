package GameStates;

import javax.media.opengl.GLAutoDrawable;
import Main.*;

public abstract class GameState {
	protected gStateMan gsm;
	public abstract void init(GLAutoDrawable drawable);
	public abstract void update(Game game);
	public abstract void draw(GLAutoDrawable drawable);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
}
