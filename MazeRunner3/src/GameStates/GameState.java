package GameStates;

import javax.media.opengl.GLCanvas;

/**
 * GameState is the abstract class in this package all the methods described below are inhereted in the other 
 * classes in this package
 *
 */

public abstract class GameState {
	protected gStateMan gsm;
	public abstract void update();
	public abstract GLCanvas getCanvas();
	public abstract gStateMan getGSM();
	public abstract boolean getPaused();
	public abstract void setPaused();
	public abstract void unPause();
	public abstract void setOptPaused();
	public abstract void unOptPause();
	public abstract void playMusic();
	public abstract void stopMusic();
	public abstract void setFinished(boolean finished);
	public abstract boolean getFinished();
	public abstract String getDifficulty();
	public abstract void setDifficulty(int diff);
	public abstract int getDiffNumber();
	public abstract int getLevel();
	public abstract void setLevel(int lvl);
	public abstract void setScore(int scr);
	public abstract int getScore();
}
