package MainGame;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import com.sun.opengl.util.Animator;

import GameStates.GameState;
import Main.Game;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/** 
 * MazeRunner is the base class of the game, functioning as the view controller and game logic manager.
 * <p>
 * Functioning as the window containing everything, it initializes both JOGL, the 
 * game objects and the game logic needed for MazeRunner.
 * <p>
 * For more information on JOGL, visit <a href="http://jogamp.org/wiki/index.php/Main_Page">this page</a>
 * for general information, and <a href="https://jogamp.org/deployment/jogamp-next/javadoc/jogl/javadoc/">this page</a>
 * for the specification of the API.
 * 
 * @author Bruno Scheele, revised by Mattijs Driel
 * 
 */
public class MazeRunner implements GLEventListener {
	static final long serialVersionUID = 7526471155622776147L;

	/*
 * **********************************************
 * *			Local variables					*
 * **********************************************
 */
	private int screenWidth, screenHeight;					// Screen size for reshaping
	private ArrayList<VisibleObject> visibleObjects;		// A list of objects that will be displayed on screen.
	private Player player;									// The player object.
	private Camera camera;									// The camera object.
	private UserInput input;								// The user input object that controls the player.
	private static Maze maze; 										// The maze.
	private PlayerSprite playerSprite;
	private long previousTime = Calendar.getInstance().getTimeInMillis(); // Used to calculate elapsed time.
	private GameState state;
	private GLCanvas canvas;
	private Game game;
	private Animator anim;
	
/*
 * **********************************************
 * *		Initialization methods				*
 * **********************************************
 */
	/**
	 * Initializes the complete MazeRunner game.
	 * <p>
	 * MazeRunner extends Java AWT Frame, to function as the window. It creates a canvas on 
	 * itself where JOGL will be able to paint the OpenGL graphics. It then initializes all 
	 * game components and initializes JOGL, giving it the proper settings to accurately 
	 * display MazeRunner. Finally, it adds itself as the OpenGL event listener, to be able 
	 * to function as the view controller.
	 */
	public MazeRunner(Game game, GameState state) {
		// Make a new window.
//		super("MazeRunner");
//		
//		// Let's change the window to our liking.
//		setSize( screenWidth, screenHeight);
//		setBackground( Color.white );
//
//		// The window also has to close when we want to.
//		this.addWindowListener( new WindowAdapter()
//		{
//			public void windowClosing( WindowEvent e )
//			{
//				System.exit(0);
//			}
//		});
		this.game = game;
		this.state = state;
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
		initJOGL();							// Initialize JOGL.
		initObjects();						// Initialize all the objects!
		
		// Set the frame to visible. This automatically calls upon OpenGL to prevent a blank screen.
//		setVisible(true);
	}
	
	private void initJOGL()	{
		// First, we set up JOGL. We start with the default settings.
		GLCapabilities caps = new GLCapabilities();
		// Then we make sure that JOGL is hardware accelerated and uses double buffering.
		caps.setDoubleBuffered( true );
		caps.setHardwareAccelerated( true );

		// Now we add the canvas, where OpenGL will actually draw for us. We'll use settings we've just defined. 
		canvas = new GLCanvas( caps );
		game.add(canvas);
		/* We need to add a GLEventListener to interpret OpenGL events for us. Since MazeRunner implements
		 * GLEventListener, this means that we add the necesary init(), display(), displayChanged() and reshape()
		 * methods to this class.
		 * These will be called when we are ready to perform the OpenGL phases of MazeRunner. 
		 */
		canvas.addGLEventListener( this );
		canvas.requestFocus();
		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		anim = new Animator( canvas );
		anim.start();
	}
	
	public static void setMaze(Maze mz){
		maze = mz;
	}

	/**
	 * initializeObjects() creates all the objects needed for the game to start normally.
	 * <p>
	 * This includes the following:
	 * <ul>
	 * <li> the default Maze
	 * <li> the Player
	 * <li> the Camera
	 * <li> the User input
	 * </ul>
	 * <p>
	 * Remember that every object that should be visible on the screen, should be added to the
	 * visualObjects list of MazeRunner through the add method, so it will be displayed 
	 * automagically. 
	 */
	private void initObjects()	{
		// We define an ArrayList of VisibleObjects to store all the objects that need to be
		// displayed by MazeRunner.
		visibleObjects = new ArrayList<VisibleObject>();
		// Add the maze that we will be using.

		if (maze == null){
			maze = new Maze();
		}

		visibleObjects.add( maze );

		// Initialize the player.
		player = new Player(maze.getStart()[0], maze.SQUARE_SIZE/2.0f, maze.getStart()[1], maze.getStart()[2],0, maze);
		
		playerSprite = new PlayerSprite((float)maze.SQUARE_SIZE, player.getLocationX(),player.getLocationY(), player.getLocationZ(), (float) player.getHorAngle());
		visibleObjects.add(playerSprite);
		
		camera = new Camera(player.getLocationX(), player.getLocationY(), player.getLocationZ(), 
				             player.getHorAngle(), player.getVerAngle() );
		
		input = new UserInput(canvas, state.getGSM());
		player.setControl(input);
	}
	
	public GLCanvas getCanvas(){
		return this.canvas;
	}

/*
 * **********************************************
 * *		OpenGL event handlers				*
 * **********************************************
 */

	/**
	 * init(GLAutodrawable) is called to initialize the OpenGL context, giving it the proper parameters for viewing.
	 * <p>
	 * Implemented through GLEventListener. 
	 * It sets up most of the OpenGL settings for the viewing, as well as the general lighting.
	 * <p> 
	 * It is <b>very important</b> to realize that there should be no drawing at all in this method.
	 */
	public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();
        
        maze.initTextures(gl);
        playerSprite.init(gl);
        
        gl.glClearColor(0, 0, 0, 0);								// Set the background color.
        
        // Now we set up our viewpoint.
        gl.glMatrixMode( GL.GL_PROJECTION );						// We'll use orthogonal projection.
        gl.glLoadIdentity();										// Reset the current matrix.
        glu.gluPerspective( 60, screenWidth, screenHeight, 200);	// Set up the parameters for perspective viewing.
        gl.glMatrixMode( GL.GL_MODELVIEW );
        
        // Enable back-face culling.
        gl.glCullFace( GL.GL_BACK );
        gl.glEnable( GL.GL_CULL_FACE );
        
        // Enable Z-buffering.
        gl.glEnable( GL.GL_DEPTH_TEST );
        
        // Set and enable the lighting.
        float lightPosition[] = { 0.0f, 50.0f, 0.0f, 1.0f }; 			// High up in the sky!
        float lightColour[] = { 1.0f, 1.0f, 1.0f, 0.0f };				// White light!
        gl.glLightfv( GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0 );	// Note that we're setting Light0.
        gl.glLightfv( GL.GL_LIGHT0, GL.GL_AMBIENT, lightColour, 0);
        gl.glEnable( GL.GL_LIGHTING );
        gl.glEnable( GL.GL_LIGHT0 );
        
        // Set the shading model.
        gl.glShadeModel( GL.GL_SMOOTH );
	}
	
	/**
	 * display(GLAutoDrawable) is called upon whenever OpenGL is ready to draw a new frame and handles all of the drawing.
	 * <p>
	 * Implemented through GLEventListener. 
	 * In order to draw everything needed, it iterates through MazeRunners' list of visibleObjects. 
	 * For each visibleObject, this method calls the object's display(GL) function, which specifies 
	 * how that object should be drawn. The object is passed a reference of the GL context, so it 
	 * knows where to draw.
	 */
	public void display(GLAutoDrawable drawable) {
		
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		if (anim.isAnimating()){	
			// Calculating time since last frame.
			Calendar now = Calendar.getInstance();		
			long currentTime = now.getTimeInMillis();
			int deltaTime = (int)(currentTime - previousTime);
			previousTime = currentTime;
			
			// Update any movement since last frame.
			updateMovement(deltaTime);
			updateCamera();
			 
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
			gl.glLoadIdentity();
	        glu.gluLookAt( camera.getLocationX(), camera.getLocationY(), camera.getLocationZ(), 
	 			   camera.getVrpX(), camera.getVrpY(), camera.getVrpZ(),
	 			   camera.getVuvX(), camera.getVuvY(), camera.getVuvZ() );
	
	        // Display all the visible objects of MazeRunner.
	        for( Iterator<VisibleObject> it = visibleObjects.iterator(); it.hasNext(); ) {
	        	it.next().display(gl);
	        }
	
	        gl.glLoadIdentity();
	        // Flush the OpenGL buffer.
	        gl.glFlush();
		}
		else{
			//draw the overlay menu
		}
	}

	
	/**
	 * displayChanged(GLAutoDrawable, boolean, boolean) is called upon whenever the display mode changes.
	 * <p>
	 * Implemented through GLEventListener. 
	 * Seeing as this does not happen very often, we leave this unimplemented.
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// GL gl = drawable.getGL();
	}
	
	/**
	 * reshape(GLAutoDrawable, int, int, int, int, int) is called upon whenever the viewport changes shape, to update the viewport setting accordingly.
	 * <p>
	 * Implemented through GLEventListener. 
	 * This mainly happens when the window changes size, thus changin the canvas (and the viewport 
	 * that OpenGL associates with it). It adjust the projection matrix to accomodate the new shape.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		
		// Setting the new screen size and adjusting the viewport.
		screenWidth = width;
		screenHeight = height;
		this.game.setScreenHeight(screenHeight);
		this.game.setScreenWidth(screenWidth);
		gl.glViewport( 0, 0, screenWidth, screenHeight );
		
		// Set the new projection matrix.
		gl.glMatrixMode( GL.GL_PROJECTION );
		gl.glLoadIdentity();
		glu.gluPerspective( 60, screenWidth/screenHeight, .1, 200 );
		gl.glMatrixMode( GL.GL_MODELVIEW );
	}

/*
 * **********************************************
 * *				Methods						*
 * **********************************************
 */

	/**
	 * updateMovement(int) updates the position of all objects that need moving.
	 * This includes rudimentary collision checking and collision reaction.
	 */
	private void updateMovement(int deltaTime)
	{
		player.update(deltaTime);
		
	/*	// TODO: implement collision
		if (maze.isWall(player.getLocationX()-1,player.getLocationZ()) ||
                    maze.isWall(player.getLocationX()+1,player.getLocationZ()) ||
                    maze.isWall(player.getLocationX(),player.getLocationZ()+1) ||
                    maze.isWall(player.getLocationX(),player.getLocationZ()-1)){
                  player.update(-deltaTime);
                }
		*/
		playerSprite.update(player.getLocationX(), player.getLocationY(), player.getLocationZ());
	}

	/**
	 * updateCamera() updates the camera position and orientation.
	 * <p>
	 * This is done by copying the locations from the Player, since MazeRunner runs on a first person view.
	 */
	
	private void updateCamera() {
		double cameraX = player.getLocationX() + 3 *Math.sin( Math.toRadians(player.getHorAngle())) * Math.cos( Math.toRadians(player.getVerAngle()) );
		double cameraY = player.getLocationY() + Math.sin(Math.toRadians(player.getVerAngle())) + 2;
		double cameraZ = player.getLocationZ() + 3 *Math.cos( Math.toRadians(player.getHorAngle())) * Math.cos(Math.toRadians(player.getVerAngle()));
		
		camera.setLocationX( cameraX);
		camera.setLocationY( cameraY );  
		camera.setLocationZ( cameraZ );
		camera.setHorAngle( player.getHorAngle() );
		camera.setVerAngle( player.getVerAngle() );
		camera.calculateVRP();
	}
	
	public void Pause() throws InterruptedException{
		if (anim.isAnimating() == true){
			anim.stop();;
		}
	}
	
	public void unPause(){
		if (anim.isAnimating() == false){
			anim.start();
		}
	}
}