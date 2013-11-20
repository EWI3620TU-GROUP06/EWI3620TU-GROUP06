package GameStates;

import javax.media.opengl.GLAutoDrawable;

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
		mm = new MainMenu(game);
		System.out.println("Mainmenu is geactiveerd");
	}
	
	public void init(GLAutoDrawable drawable) {
		mm.init(drawable);
		System.out.println("init gedaan mainmenu");
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		mm.display(drawable);
		//System.out.println("drawgedaan mainmenu");
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
		mm.reshape(drawable, x, y, width, height);
	}
}