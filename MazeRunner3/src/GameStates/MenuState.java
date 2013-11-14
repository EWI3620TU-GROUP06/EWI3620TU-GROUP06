package GameStates;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.media.opengl.GLAutoDrawable;

import Main.Game;
import MainGame.MainMenu;
import MainGame.MazeRunner;

public class MenuState extends GameState /*implements MouseListener*/ {
	private MainMenu mm;
	public MenuState(gStateMan gsm, Game game){
		mm = new MainMenu(game);
		System.out.println("Mainmenu is geactiveerd");
	}
	@Override
	public void init(GLAutoDrawable drawable) {
		mm.init(drawable);
		System.out.println("init gedaan");

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		mm.display(drawable);
		System.out.println("drawgedaan");

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