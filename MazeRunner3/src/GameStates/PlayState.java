package GameStates;

import javax.media.opengl.GLCanvas;

import Audio.Audio;
import Main.Game;
import MainGame.MazeRunner;

/**
 * in the playstate we set up the game in this class the actual maze runner is created.  
 */
public class PlayState extends GameState {
	
	private gStateMan gsm;
	private MazeRunner mz;
	private Game game;
	private boolean finished;
	private boolean paused;
	private int difficulty = 0;
	private static int level = 1;
	private static int totalscore = 0;
	
	public PlayState(gStateMan gsm, Game game){
		this.gsm = gsm;
		this.game = game;
		this.paused = false;
		this.finished = false;
	}

	public void update(){
		mz = new MazeRunner(game,this);
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
		Audio.playMusic("ingame");
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
	@Override
	public void setScore(int scr) {
		if(scr == 0){
			totalscore = 0;
		}
		else{
			totalscore = totalscore + scr;
		}
		
	}

	@Override
	public int getScore() {
		return totalscore;
	}
}
