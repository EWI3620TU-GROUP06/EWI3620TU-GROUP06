package MainGame;

import com.sun.opengl.util.Animator;

import javax.media.opengl.*;

import com.sun.opengl.util.texture.Texture;

import Drawing.TextBox;
import Drawing.TextBoxManager;
import Drawing.DrawingUtil;
import GameStates.GameState;
import Listening.Command;
import Listening.EditorCommand;
import Listening.LoadCommand;
import Listening.PlayCommand;
import Listening.QuitCommand;
import Listening.UserInput;
import Main.Game;

public class MainMenu implements GLEventListener {
	
	private int screenWidth, screenHeight;				// Screen size to handle reshaping
	private Game game;
	private GLCanvas canvas;
	private Texture backgroundTexture;
	private int titleScale = 10;
	private int textScale = 18;
	private GameState state;
	private TextBoxManager clkbxman;
	private UserInput input;
	
	public MainMenu(Game game, GameState state) {
		this.game = game;
		this.state = state;
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
		initJOGL();
		initMenuText();
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
		input = state.getGSM().getInput();
		AddListening(input);
		
		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		Animator anim = new Animator( canvas );
		anim.start();
	}
	
	private void initMenuText(){
		//Add the clickboxes for the pauze menu
		this.clkbxman = new TextBoxManager(); //We want 5 click (text) boxes, but the first (title) should not be clickable
		this.clkbxman.setControl(input);
		
		//Title
		clkbxman.AddBox(new TextBox((int)(screenWidth/2),(int)(screenHeight*0.8), //Location of lower-left corner
				screenWidth, screenHeight, //screen size
				titleScale, "Impact", 0, "MadBalls", //TextScale, Font, type (bold/italic etc) and text to draw
				0.9f, 0.4f, 0.4f, 1f, //color in r,g,b,alpha
				false)); // isClickable
		
		//Play button
		clkbxman.AddBox(new TextBox((int)(screenWidth/2),(int)(screenHeight*0.630), //Location of lower-left corner
				screenWidth, screenHeight, //screen size
				textScale, "Arial", 0, "Play", //TextScale (which is a number to divide by!), Font, type (plain/bold/italic etc) and text to draw
				1f, 1f, 1f, 1f, //color in r,g,b, alpha
				true)); // isClickable
		
		Command play = new PlayCommand(this.state.getGSM());
		clkbxman.setCommand(1,play);
		
		//Load button
		clkbxman.AddBox(new TextBox((int)(screenWidth/2),(int)(screenHeight*0.480), //Location of lower-left corner
				screenWidth, screenHeight, //screen size
				textScale, "Arial", 0, "Load", //TextScale (which is a number to divide by!), Font, type (plain/bold/italic etc) and text to draw
				1f, 1f, 1f, 1f, //color in r,g,b,alpha
				true)); // isClickable
		
		Command load = new LoadCommand(this.state.getGSM());
		clkbxman.setCommand(2, load);
		
		//Editor button
		clkbxman.AddBox(new TextBox((int)(screenWidth/2),(int)(screenHeight*0.330), //Location of lower-left corner
				screenWidth, screenHeight, //screen size
				textScale, "Arial", 0, "Editor", //TextScale (which is a number to divide by!), Font, type (plain/bold/italic etc) and text to draw
				1f, 1f, 1f, 1f, // color in r,g,b,alpha
				true)); // isClickable
		
		Command edit = new EditorCommand(this.state.getGSM());
		clkbxman.setCommand(3, edit);
		
		//Quit button
		clkbxman.AddBox(new TextBox((int)(screenWidth/2),(int)(screenHeight*0.180), //Location of lower-left corner
				screenWidth, screenHeight, //screen size
				textScale, "Arial", 0, "Quit", //TextScale (which is a number to divide by!), Font, type (plain/bold/italic etc) and text to draw
				1f, 1f, 1f, 1f, // color in r,g,b,alpha
				true)); // isClickable
		
		Command quit = new QuitCommand();
		clkbxman.setCommand(4,quit);
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		DrawingUtil.orthographicProjection(gl, screenWidth, screenHeight);
		gl.glDisable(GL.GL_DEPTH_TEST);
			
		// Preload the texture we want to use!
		backgroundTexture = DrawingUtil.initTexture(gl, "mainmenu");
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
		DrawingUtil.boxOnScreen(gl, 0, 0, screenWidth, screenHeight); // draw the background plane
		backgroundTexture.disable(); // Disable the background texture again, such that the next object is textureless
		DrawingUtil.drawTrans(gl,0,0,screenWidth,screenHeight,0f,0f,0f,0.4f); // draw an extra greyish thing to increase contrast
		
		this.clkbxman.drawAllText(); // draw the text in the menu
		this.clkbxman.update();
		
		gl.glFlush();
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
		DrawingUtil.orthographicProjection(gl, screenWidth, screenHeight);
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		//To init the drawing elements of overlay menu's/text etc.
		this.clkbxman.reshape(screenWidth, screenHeight);
	}
	
	public GLCanvas getCanvas(){
		return this.canvas;
	}
	
	private void AddListening(UserInput input){
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);
	}
}
