package GameStates;

import java.util.ArrayList;

import javax.media.opengl.GLAutoDrawable;

import Main.Game;

public class gStateMan {
	
	private ArrayList<GameState> gameStates;
	private int currentState;
	public static final int MENUSTATE = 0;
	public static final int PLAYSTATE = 1;
	
	public gStateMan(Game game){
		gameStates = new ArrayList<GameState>();
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new PlayState(this));
		update(game);
	}
	
	public void setState(int s,GLAutoDrawable drawable){
		this.currentState = s;
		this.gameStates.get(currentState).init(drawable);
	}
	
	public void update(Game game){
		gameStates.get(currentState).update(game);
	}
	
	public void draw(GLAutoDrawable drawable){
		gameStates.get(currentState).draw(drawable);
	}
	
	public void keyPressed(int k){
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k){
		gameStates.get(currentState).keyReleased(k);
	}

	public void init(GLAutoDrawable drawable) {
		gameStates.get(currentState).init(drawable);
		
	}
}
