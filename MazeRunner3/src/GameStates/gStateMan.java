package GameStates;

import java.util.ArrayList;

import javax.media.opengl.GLAutoDrawable;

import Listening.UserInput;
import Main.Game;

public class gStateMan {
	
	private ArrayList<GameState> gameStates;
	private int currentState;
	public static final int MENUSTATE = 0;
	public static final int PLAYSTATE = 1;
	public static final int EDITSTATE = 2;
	public static final int HIGHSCORESTATE = 3;
	private Game game;
	private UserInput input;
	
	public gStateMan(Game game){
		this.game = game;
		gameStates = new ArrayList<GameState>();
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this, game));
		gameStates.add(new PlayState(this, game));
		gameStates.add(new EditState(this, game));
		gameStates.add(new HighscoreState(this, game));
		input = new UserInput(this);
		gameStates.get(currentState).playMusic();
		update();
	}
	
	public void setState(int s){
		game.remove(gameStates.get(currentState).getCanvas());
		this.currentState = s;
		update();
		input.reset();
		gameStates.get(currentState).unPause();
		gameStates.get(currentState).unOptPause();
		gameStates.get(currentState).playMusic();
		game.validate();
	}
	
	public void setPauseState(){
		gameStates.get(currentState).setPaused();
	}
	
	public void setUnPauseState(){
		gameStates.get(currentState).unPause();
	}
	
	public int getCurState(){
		return currentState;
	}
	
	public void update(){
		gameStates.get(currentState).update();
	}
	
	public void draw(GLAutoDrawable drawable){
		gameStates.get(currentState).draw(drawable);
	}
	
	public void init(GLAutoDrawable drawable) {
		gameStates.get(currentState).init(drawable);
		
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
		gameStates.get(currentState).reshape(drawable,x,y,width,height);
	}
	
	public Game getGame(){
		return this.game;
	}
	
	public GameState getState(int i){
		return gameStates.get(i);		
	}
	
	public UserInput getInput(){
		return input;
	}
}
