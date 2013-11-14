package MainGame;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import Main.Game;

public class MainMenu implements GLEventListener {
	
	private Game game;
	private int screenWidth, screenHeight;				// Screen size to handle reshaping
	
	public MainMenu(Game game) {
		this.game = game;
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
		initMenu();
	}
	
	private void initMenu(){
		
	}
	
	/*
	 * OpenGL GLEventListener Methods down from here
	*/
	
	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0,screenWidth,0,screenHeight,-1,1);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}

}
