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
		gameStates.add(new MenuState(this,game));
		gameStates.add(new PlayState(this,game));
		update();
	}
	
	public void setState(int s){
		this.currentState = s;
		update();
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
}
