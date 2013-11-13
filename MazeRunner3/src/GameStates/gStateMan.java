package GameStates;

import java.util.ArrayList;

public class gStateMan {
	
	private ArrayList<GameState> gameStates;
	private int currentState;
	public static final int MENUSTATE = 0;
	public static final int PLAYSTATE = 1;
	
	public gStateMan(){
		
		gameStates = new ArrayList<GameState>();
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
	}
	
	public void setState(int s){
		this.currentState = s;
		this.gameStates.get(currentState).init();
	}
	
	public void update(){
		gameStates.get(currentState).update();
	}
	
	public void draw(java.awt.Graphics2D g){
		gameStates.get(currentState).draw(g);
	}
	
	public void keyPressed(int k){
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k){
		gameStates.get(currentState).keyReleased(k);
	}
}
