package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Main.Game;
import MainGame.MainMenu;

public class MenuState extends GameState {
	private MainMenu mm;
	private Game game;
	public MenuState(gStateMan gsm,Game game){
		this.gsm = gsm;
		this.game = game;
	}
	@Override
	
	public void update(){
		mm = new MainMenu(game, this);
	}
	
	public void init(GLAutoDrawable drawable) {
		mm.init(drawable);
		System.out.println("init gedaan mainmenu");
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		mm.display(drawable);
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
		mm.reshape(drawable, x, y, width, height);
	}
	
	public GLCanvas getCanvas(){
		return mm.getCanvas();
	}
	
	public gStateMan getGSM(){
		return this.gsm;
	}
	@Override
	public boolean getPaused() {
		return false;
	}
	@Override
	public void setPaused() {
		//do nothing
	}
	
	public void unPause(){
		//do nothing
	}
}
