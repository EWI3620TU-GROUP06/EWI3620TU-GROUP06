package MainGame;

import com.sun.opengl.util.Animator;

import javax.media.opengl.*;

import com.sun.opengl.util.texture.Texture;

import Audio.Audio;
import Drawing.TextBoxManager;
import Drawing.DrawingUtil;
import GameStates.GameState;
import Listening.UserInput;
import Main.Game;

/**
 * the mainMenu class sets up the main menu and its functionalities the mainmenu is one of the four states used in
 * the main game.
 *
 */
public class MainMenu implements GLEventListener {
	
	private int screenWidth, screenHeight;				// Screen size to handle reshaping
	private Game game;
	private GLCanvas canvas;
	private Texture backgroundTexture;
	private GameState state;
	private TextBoxManager clkbxman;
	private TextBoxManager optclkbxman;
	private UserInput input;
	private boolean optmenu;
	
	public MainMenu(Game game, GameState state) {
		this.game = game;
		this.state = state;
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
		initJOGL();
		initMenuText();
	}
	/**
	 * the init jogl method sets up the opengl environment and canvas in wich this menu is drawn
	 */
	private void initJOGL()	{
		// First, we set up JOGL. We start with the default settings.
		GLCapabilities caps = new GLCapabilities();
		// Then we make sure that JOGL is hardware accelerated and uses double buffering.
		caps.setDoubleBuffered( true );
		caps.setHardwareAccelerated( true );

		// Now we add the canvas, where OpenGL will actually draw for us. We'll use settings we've just defined. 
		canvas = new GLCanvas( caps );
		game.add( canvas );
		canvas.addGLEventListener( this );
		canvas.requestFocus();
		input = state.getGSM().getInput();
		AddListening(input);
		
		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		Animator anim = new Animator(canvas);
		anim.start();
	}
	
	/**
	* the initMenuText method creates the clickboxes for the main menu and the options menu these clickboxes are described in the
	* drawing package.
	*/
	private void initMenuText(){
		
		String[] commands = {"Play", "Load", "Highscores", "Options", "Editor", "Quit"};
		String[] optcommands = {"Toggle Fullscreen", "Difficulty:", "Back"};
		//Add the clickboxes for the pauze menu
		this.clkbxman = TextBoxManager.createMenu(screenWidth, screenHeight, "MadBalls", commands, this.state.getGSM()); 
		this.optclkbxman = TextBoxManager.createOptionsMenu(screenWidth, screenHeight, "Options", optcommands, this.state.getGSM());
		this.clkbxman.setControl(input);
		this.optclkbxman.setControl(input);
		
		String[] sounds = new String[]{"wallcollide","balldrop","ballcollide","test","test2"};
		Audio.initSounds(sounds);
	}
	
	/**
	 * the init method sets up the drawing mode for the othographicProjection and initializes the background
	 * texture used.
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		DrawingUtil.orthographicProjection(gl, screenWidth, screenHeight);
		gl.glDisable(GL.GL_DEPTH_TEST);
			
		// Preload the texture we want to use!
		backgroundTexture = DrawingUtil.initTexture(gl, "mainmenu");
		}
	
	/**
	 * the display method draws all the visibles on the canvas.
	 */	
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
		
		if(!optmenu){
			this.clkbxman.drawAllText(0); // draw the text in the menu
			this.clkbxman.update();
		}
		else{
			this.optclkbxman.drawAllText(0);
			this.optclkbxman.update();
		}
		
		gl.glFlush();
	}
	
	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// Hopefully not needed
		
	}
	
	/**
	 * the reshape method reshapes all the visibles in the canvas and the canvas and frame it self.
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();
		
		// Setting the new screen size and adjusting the viewport.
		screenWidth = width;
		screenHeight = height;
		this.game.setScreenHeight(screenHeight);
		this.game.setScreenWidth(screenWidth);
		
		// Set the new projection matrix.
		DrawingUtil.orthographicProjection(gl, screenWidth, screenHeight);
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		//To init the drawing elements of overlay menu's/text etc.
		this.clkbxman.reshape(screenWidth, screenHeight);
		this.optclkbxman.reshape(screenWidth, screenHeight);
	}
	
	public GLCanvas getCanvas(){
		return this.canvas;
	}
	
	
	/**
	 * the method Addlinstening adds the listeners needed in the higscore state to the canvas
	 * @param input	the object user input is passed to set the eventlisteners
	 */
	private void AddListening(UserInput input){
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);
	}
	
	public void setOptionsMenu(){
		this.optmenu = true;
	}
	
	public void setMainMenu(){
		this.optmenu = false;
	}
}
