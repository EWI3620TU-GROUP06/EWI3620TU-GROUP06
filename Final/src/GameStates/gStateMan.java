package GameStates;

import java.util.ArrayList;

import Listening.UserInput;
import Main.Game;

/**
 * the gStateMan class is used to switch between gamestates.
 *
 */
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
	
	/**
	 * the method setState is used to switch between states this happens for instance the when you die in the
	 * game the state is directly switched to the higscore state
	 * @param s		the integer is to make clear to which state the switch is made
	 */
	public void setState(int s){
		gameStates.get(currentState).stopMusic();
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
