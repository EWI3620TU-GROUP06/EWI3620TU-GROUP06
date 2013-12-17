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
	private int difficulty = 0;
	private static int level = 1;
	
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
	
	public void stopMusic(){
		Audio.stopMusic();
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
	
	public String getDifficulty(){
		String res = " ";
		if(difficulty == 0){
			res = "Easy";
		}
		else if(difficulty == 1){
			res = "Medium";
		}
		else if(difficulty == 2){
			res = "Hard";
		}
		return res;
	}
	
	public int getDiffNumber(){
		return difficulty;
	}
	
	public void setDifficulty(int diff){
		difficulty = diff;
	}
	@Override
	public int getLevel() {
		int res = level;
		return res;
	}
	@Override
	public void setLevel(int lvl) {
		level = lvl;		
	}
}
