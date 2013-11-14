package GameStates;

import java.util.ArrayList;

import javax.media.opengl.GLAutoDrawable;

import Main.Game;

public class gStateMan {
	
	private ArrayList<GameState> gameStates;
	private int currentState;
	public static final int MENUSTATE = 1;
	public static final int PLAYSTATE = 0;
	
	public gStateMan(Game game){
		gameStates = new ArrayList<GameState>();
		currentState = PLAYSTATE;
//		gameStates.add(new MenuState(this,game));
		gameStates.add(new PlayState(this,game));
	}
	
	public void setState(int s,GLAutoDrawable drawable){
		this.currentState = s;
		this.gameStates.get(currentState).init(drawable);
	}
	
	public void update(){
		gameStates.get(currentState).update();
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
