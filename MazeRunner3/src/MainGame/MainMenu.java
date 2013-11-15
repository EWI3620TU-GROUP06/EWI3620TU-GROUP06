package MainGame;

import java.io.InputStream;

import javax.media.opengl.*;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

import Main.Game;

public class MainMenu implements GLEventListener {
	
	private int screenWidth, screenHeight;				// Screen size to handle reshaping
	private Texture backgroundTexture;
	
	public MainMenu(Game game) {
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0,screenWidth,0,screenHeight,-1,1); //2D by making a -1 to 1 z around the z = 0 plane
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		// Preload the texture we want to use!
		try{
		InputStream stream = getClass().getResourceAsStream("mainmenu.jpg");
        TextureData data = TextureIO.newTextureData(stream, false, "jpg");
        this.backgroundTexture = TextureIO.newTexture(data);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
		
	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		// set the 'clear screen color' not really necessary but useful to make all the clear colors the same!
		gl.glClearColor(1f,1f,1f,1);
		
		//The ambient color is white light
        float[] lightColorAmbient = {1f, 1f, 1f, 1f};

        // The Ambient light is created here.
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightColorAmbient, 0);
        
        // Enable lighting in GL.
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);
		
		float[] rgba = {1f, 0f, 0f}; //Sets the material color
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
		backgroundTexture.enable();
		backgroundTexture.bind();
		drawPlane(gl); // draw the background plane
		
		drawMenu(gl); // draw the menu buttons with text and stuff.
		gl.glFlush();
	}
	
	private void drawPlane(GL gl){
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(0, 0);
		gl.glVertex2f(screenWidth,0);
		gl.glVertex2f(screenWidth,screenHeight);
		gl.glVertex2f(0,screenHeight);
		gl.glEnd();
	}

	private void drawMenu(GL gl){
		
	}
	
	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// Hopefully not needed
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();
		
		// Setting the new screen size and adjusting the viewport.
		screenWidth = width;
		screenHeight = height;
		gl.glViewport( 0, 0, screenWidth, screenHeight );
		
		// Set the new projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0,screenWidth,0,screenHeight,-1,1); //2D by making a -1 to 1 z around the z = 0 plane
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glDisable(GL.GL_DEPTH_TEST);
	}

}
