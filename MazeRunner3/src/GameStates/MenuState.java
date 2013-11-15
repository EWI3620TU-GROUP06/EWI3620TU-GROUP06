package GameStates;

import javax.media.opengl.GLAutoDrawable;

import Main.Game;
import MainGame.MainMenu;

public class MenuState extends GameState /*implements MouseListener*/ {
	private MainMenu mm;
	public MenuState(gStateMan gsm){
		this.gsm = gsm;
	}
	@Override
	
	public void update(Game game){
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

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}
}