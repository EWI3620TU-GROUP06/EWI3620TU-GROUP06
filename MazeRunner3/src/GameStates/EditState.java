package GameStates;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;

import Main.Game;
import MainGame.MazeEditor;

public class EditState extends GameState {

	private MazeEditor me;
	private Game game;
	
	public EditState(gStateMan gsm, Game game){
		this.gsm = gsm;
		this.game = game;
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		me.init(drawable);
	}

	@Override
	public void update() {
		me = new MazeEditor(game, this);
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		me.display(drawable);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		me.reshape(drawable,x, y, width, height);
	}

	@Override
	public GLCanvas getCanvas() {
		return me.getCanvas();
	}

	@Override
	public gStateMan getGSM() {
		return this.gsm;
	}

}
