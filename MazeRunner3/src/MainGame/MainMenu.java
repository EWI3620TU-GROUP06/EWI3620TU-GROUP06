package MainGame;

import java.io.InputStream;
import java.awt.Font;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.j2d.TextRenderer;

import javax.media.opengl.*;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

import GameStates.GameState;
import Main.Game;

public class MainMenu implements GLEventListener {
	
	private int screenWidth, screenHeight;				// Screen size to handle reshaping
	private Game game;
	private GLCanvas canvas;
	private Texture backgroundTexture;
	private TextRenderer renderer;
	private TextRenderer Trenderer;
	private int titleScale = 10;
	private int textScale = 18;
	private GameState state;
	
	public MainMenu(Game game, GameState state) {
		this.game = game;
		this.state = state;
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
		initJOGL();
	}
	
	private void initJOGL()	{
		// First, we set up JOGL. We start with the default settings.
		GLCapabilities caps = new GLCapabilities();
		// Then we make sure that JOGL is hardware accelerated and uses double buffering.
		caps.setDoubleBuffered( true );
		caps.setHardwareAccelerated( true );

		// Now we add the canvas, where OpenGL will actually draw for us. We'll use settings we've just defined. 
		canvas = new GLCanvas( caps );
		game.add( canvas );
		/* We need to add a GLEventListener to interpret OpenGL events for us. Since MazeRunner implements
		 * GLEventListener, this means that we add the necesary init(), display(), displayChanged() and reshape()
		 * methods to this class.
		 * These will be called when we are ready to perform the OpenGL phases of MazeRunner. 
		 */
		canvas.addGLEventListener( this );
		canvas.requestFocus();
		new UserInput(canvas, state.getGSM());
		
		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		Animator anim = new Animator( canvas );
		anim.start();
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
		InputStream stream = getClass().getResourceAsStream("../Textures/mainmenu.jpg");
        TextureData data = TextureIO.newTextureData(stream, false, "jpg");
        this.backgroundTexture = TextureIO.newTexture(data);
        stream.close();
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
        float[] lightColor = {1f, 1f, 1f, 1f};

        // The Ambient light is created here.
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightColor, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lightColor, 0);
        
        // Enable lighting in GL.
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);
        
		
		float[] rgba = {1f, 1f, 1f}; //Sets the material color
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
		backgroundTexture.enable(); // Enable the background texture
		backgroundTexture.bind(); // Bind the background texture to the next object
		drawPlane(gl); // draw the background plane
		backgroundTexture.disable(); // Disable the background texture again, such that the next object is textureless
		
		drawMenu(gl); // draw the menu buttons with text and stuff.
		
		gl.glFlush();
	}
	
	private void drawPlane(GL gl){
		//Draws a background-plane with a texture
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(0, 0);
		gl.glTexCoord2f(1, 1);
		gl.glVertex2f(screenWidth,0);
		gl.glTexCoord2f(1, 0);
		gl.glVertex2f(screenWidth,screenHeight);
		gl.glTexCoord2f(0, 0);
		gl.glVertex2f(0,screenHeight);
		gl.glTexCoord2f(0, 1);
		gl.glEnd();
	}

	private void drawMenu(GL gl){
		//Teken nu het menu over de achtergrond heen
		
		//Draw a nice transparent surface over the background
		drawTrans(gl,0,0,screenWidth,screenHeight,0.1f,0.1f,0.1f,0.4f);
		
		//Draw the epic title
		drawTitle("MadBalls", 0.9f, 0.4f, 0.4f, 1f, (int)(screenWidth*0.315),(int)(screenHeight*0.8));
			
		// De vier menu texts "New game (of play ofzo" "Load level" "options" "quit"
		drawText("Play", 1f, 1f, 1f, 1f,(int)(screenWidth*0.445),
				(int)(screenHeight*0.625));
		
		drawText("Load", 1f, 1f, 1f, 1f,(int)(screenWidth*0.432),
				(int)(screenHeight*0.48));
		
		drawText("Editor", 1f, 1f, 1f, 1f,(int)(screenWidth*0.42),
				(int)(screenHeight*0.33));
	
		drawText("Quit", 1f, 1f, 1f, 1f,(int)(screenWidth*0.442),
				(int)(screenHeight*0.18));
	}
	
	private void drawTrans(GL gl, float x, float y, float width, float height
			,float r, float g, float b, float a){
		//De onderstaande functies
		//zorgen voor de doorzichtigheid van de menu
		//elementen, tesamen met kleur etc.
		
		gl.glColor4f(r,g,b,a);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		
		//draw the actual surface
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x,y);
		gl.glVertex2f(x + width, y);
		gl.glVertex2f(x + width, y + height);
		gl.glVertex2f(x, y + height);
		gl.glEnd();
		
		// Disable alle crap voordat 
		//de volgende flush plaats vindt en 
		//de settings doorgegeven worden aan
		//de achtergrond
		gl.glDisable(GL.GL_COLOR_MATERIAL);
		gl.glDisable(GL.GL_BLEND);
	}
	
	private void drawText(String text, float r, float g, float b, float a, int x, int y){
		//Renderer alvast in init gemaakt, anders wordt ie na elke glFlush() opnieuw gemaakt!
		
		renderer.beginRendering(screenWidth, screenHeight);
		renderer.setColor(r, g, b, a);
		renderer.draw(text, x, y);
		renderer.flush();
		renderer.endRendering();
	}
	
	private void drawTitle(String text, float r, float g, float b, float a, int x, int y){
		//Renderer alvast in init gemaakt, anders wordt ie na elke glFlush() opnieuw gemaakt!
		
		Trenderer.beginRendering(screenWidth, screenHeight);
		Trenderer.setColor(r, g, b, a);
		Trenderer.draw(text, x, y);
		Trenderer.flush();
		Trenderer.endRendering();
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
		this.game.setScreenHeight(screenHeight);
		this.game.setScreenWidth(screenWidth);
		gl.glViewport( 0, 0, screenWidth, screenHeight );
		
		// Set the new projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0,screenWidth,0,screenHeight,-1,1); //2D by making a -1 to 1 z around the z = 0 plane
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		//To render title
		Trenderer = new TextRenderer(new Font("Impact", Font.PLAIN, (screenWidth)/titleScale)); 
		
		//To render texts
		//Set the font type shizzle here
		renderer = new TextRenderer(new Font("Arial", Font.BOLD, (screenWidth)/textScale)); 
	}
	
	public GLCanvas getCanvas(){
		return this.canvas;
	}

}
