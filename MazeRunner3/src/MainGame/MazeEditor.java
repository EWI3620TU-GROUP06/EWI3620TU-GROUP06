package MainGame;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.vecmath.Vector3d;

import Drawing.*;
import GameObjects.Camera;
import GameObjects.Editor;
import GameStates.GameState;
import LevelHandling.Level;
import LevelHandling.Maze;
import Listening.UserInput;
import Main.Game;

import com.sun.opengl.util.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * MazeRunner is the base class of the game, functioning as the view controller
 * and game logic manager.
 * <p>
 * Functioning as the window containing everything, it initializes both JOGL,
 * the game objects and the game logic needed for MazeRunner.
 * <p>
 * For more information on JOGL, visit <a
 * href="http://jogamp.org/wiki/index.php/Main_Page">this page</a> for general
 * information, and <a
 * href="https://jogamp.org/deployment/jogamp-next/javadoc/jogl/javadoc/">this
 * page</a> for the specification of the API.
 * 
 * @author Bruno Scheele, revised by Mattijs Driel
 * 
 */
public class MazeEditor implements GLEventListener {
	static final long serialVersionUID = 7526471155622776147L;

	/*
	 * **********************************************
	 * * Local variables * **********************************************
	 */
	private GLCanvas canvas;

	private int screenWidth = 600, screenHeight = 600; // Screen size.
	private float FOV = 45.0f;

	private ArrayList<VisibleObject> visibleObjects; // A list of objects that will be displayed on screen.
	private Camera camera; // The camera object.
	private Editor editor; // The editor object;
	private UserInput input; // The user input object that controls the player/editor.
	private Level level; // The maze.
	private GameState state;
	private Game game;
	private FPSAnimator anim;
	private boolean pause;
	private boolean optpause;
	private TextBoxManager clkbxman;
	private TextBoxManager optclkbxman;
	private EditBoxManager editBoxManager;

	/*
	 * **********************************************
	 * * Initialization methods * **********************************************
	 */
	/**
	 * Initializes the complete MazeRunner game.
	 * <p>
	 * MazeRunner extends Java AWT Frame, to function as the window. It creats a
	 * canvas on itself where JOGL will be able to paint the OpenGL graphics. It
	 * then initializes all game components and initializes JOGL, giving it the
	 * proper settings to accurately display MazeRunner. Finally, it adds itself
	 * as the OpenGL event listener, to be able to function as the view
	 * controller.
	 */
	public MazeEditor(Game game, GameState state) {
		//No window anymore!
		this.game = game;
		this.state = state;
		initJOGL(); // Initialize JOGL.
		initObjects(); // Initialize all the objects!
		initMenuText();
	}

	/**
	 * initJOGL() sets up JOGL to work properly.
	 * <p>
	 * It sets the capabilities we want for MazeRunner, and uses these to create
	 * the GLCanvas upon which MazeRunner will actually display our screen. To
	 * indicate to OpenGL that is has to enter a continuous loop, it uses an
	 * Animator, which is part of the JOGL api.
	 */
	private void initJOGL() {
		// First, we set up JOGL. We start with the default settings.
		GLCapabilities caps = new GLCapabilities();
		// Then we make sure that JOGL is hardware accelerated and uses double
		// buffering.
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);

		// Now we add the canvas, where OpenGL will actually draw for us. We'll
		// use settings we've just defined.
		canvas = new GLCanvas(caps);
		game.add(canvas);
		/*
		 * We need to add a GLEventListener to interpret OpenGL events for us.
		 * Since MazeRunner implements GLEventListener, this means that we add
		 * the necesary init(), display(), displayChanged() and reshape()
		 * methods to this class. These will be called when we are ready to
		 * perform the OpenGL phases of MazeRunner.
		 */
		canvas.addGLEventListener(this);
		canvas.requestFocus();

		/*
		 * We need to create an internal thread that instructs OpenGL to
		 * continuously repaint itself. The Animator class handles that for
		 * JOGL.
		 */
		anim = new FPSAnimator(canvas, 60);
		anim.start();
	}

	/**
	 * initializeObjects() creates all the objects needed for the game to start
	 * normally.
	 * <p>
	 * This includes the following:
	 * <ul>
	 * <li>the default Maze
	 * <li>the Player
	 * <li>the Camera
	 * <li>the User input
	 * </ul>
	 * <p>
	 * Remember that every object that should be visible on the screen, should
	 * be added to the visualObjects list of MazeRunner through the add method,
	 * so it will be displayed automagically.
	 */
	private void initObjects() {
		// We define an ArrayList of VisibleObjects to store all the objects
		// that need to be
		// displayed by MazeRunner.
		visibleObjects = new ArrayList<VisibleObject>();
		// Add the maze that we will be using.
		level = new Level(new Maze());

		visibleObjects.add(level.getMaze());

		editor = new Editor(new Vector3d(level.getMaze().getSize() / 2, 60, level.getMaze().getSize()/2), 0, -89.99999);
		input = state.getGSM().getInput();
		editor.initSet(level, FOV, input);
		
		camera = new Camera(editor.getLocation(), editor.getHorAngle(), editor.getVerAngle());		

		editBoxManager = new EditBoxManager(editor, screenWidth, screenHeight);

		AddListening(input);

		editBoxManager.setControl(input);
	}

	private void initMenuText(){
		String[] commands = {"Resume", "Options","Main Menu", "Quit"};
		String[] optcommands = {"Toggle Fullscreen", "Back"};
		this.clkbxman = TextBoxManager.createMenu(screenWidth, screenHeight, "Pause", commands, this.state.getGSM());
		this.optclkbxman = TextBoxManager.createMenu(screenWidth, screenHeight, "Options", optcommands, this.state.getGSM());
		this.clkbxman.setControl(input);
		this.optclkbxman.setControl(input);
	}

	/*
	 * **********************************************
	 * * OpenGL event handlers * **********************************************
	 */

	/**
	 * init(GLAutodrawable) is called to initialize the OpenGL context, giving
	 * it the proper parameters for viewing.
	 * <p>
	 * Implemented through GLEventListener. It sets up most of the OpenGL
	 * settings for the viewing, as well as the general lighting.
	 * <p>
	 * It is <b>very important</b> to realize that there should be no drawing at
	 * all in this method.
	 */
	public void init(GLAutoDrawable drawable) {
		//drawable.setGL(new DebugGL(drawable.getGL())); // We set the OpenGL
		// pipeline to Debugging
		// mode.
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		
		Maze.initTextures(gl);
		editBoxManager.initTextures(gl);
		
		
		gl.glClearColor(0, 0, 0, 0); // Set the background color.

		DrawingUtil.perspectiveProjection(gl, glu, FOV, screenWidth, screenHeight);

		// Enable back-face culling.
		gl.glCullFace(GL.GL_BACK);
		gl.glEnable(GL.GL_CULL_FACE);

		// Enable Z-buffering.
		gl.glEnable(GL.GL_DEPTH_TEST);

		// Set and enable the lighting.
		float lightPosition[] = { 0.0f, 50.0f, 0.0f, 1.0f }; // High up in the sky!
		float lightColour[] = { 1.0f, 1.0f, 1.0f, 0.0f }; // White light!
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0); // Note that we're setting Light0.
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, lightColour, 0);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);

		// Set the shading model.
		gl.glShadeModel(GL.GL_SMOOTH);
	}

	/**
	 * display(GLAutoDrawable) is called upon whenever OpenGL is ready to draw a
	 * new frame and handles all of the drawing.
	 * <p>
	 * Implemented through GLEventListener. In order to draw everything needed,
	 * it iterates through MazeRunners' list of visibleObjects. For each
	 * visibleObject, this method calls the object's display(GL) function, which
	 * specifies how that object should be drawn. The object is passed a
	 * reference of the GL context, so it knows where to draw.
	 */
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		if (!pause){
			editBoxManager.update();
			if(!editBoxManager.isHoovering())
				editor.update(screenWidth, screenHeight);
			else{
				input.isLeftButtonPressed();
				input.isLeftButtonDragged();
				input.getMouseReleased();
			}

			if(editor.getLevel().getMaze() != visibleObjects.get(0))
			{
				level = editor.getLevel();

				visibleObjects.remove(0);
				visibleObjects.add(0, level.getMaze());
			}
			updateCamera();
		}

		double[] pos = new double[3];
		double[] vuv = new double[3];
		double[] vrp = new double[3];
		camera.getLocation().get(pos);
		camera.getVuv().get(vuv);
		camera.getVrp().get(vrp);

		glu.gluLookAt(pos[0], pos[1], pos[2], 
				vrp[0], vrp[1], vrp[2], vuv[0], vuv[1], vuv[2]);

		// Display all the visible objects of MazeRunner.
		for (Iterator<VisibleObject> it = visibleObjects.iterator(); it
				.hasNext();) {
			it.next().display(gl);
		}

		// When editing: use an orthographic projection to draw the HUD on the screen, 
		// then set the perspective projection back
		gl.glLoadIdentity();
		DrawingUtil.orthographicProjection(gl, screenWidth, screenHeight);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glColor4f(1f,1f,1f,1f);
		editBoxManager.drawTextures(gl);
		DrawingUtil.perspectiveProjection(gl, glu, FOV, screenWidth, screenHeight);

		gl.glEnable(GL.GL_LIGHTING);
		if(pause){
			DrawingUtil.orthographicProjection(gl, screenWidth, screenHeight);

			DrawingUtil.drawTrans(gl, 0, 0, screenWidth, screenHeight, 0.2f, 0.2f, 0.2f, 0.4f);
			if(optpause){
				this.optclkbxman.drawAllText(0);
			}
			else{
				this.clkbxman.drawAllText(0);
			}

			DrawingUtil.perspectiveProjection(gl, glu, FOV, screenWidth, screenHeight);
			
			if(optpause){
				this.optclkbxman.update();
			}
			else{
				this.clkbxman.update();
			}
			gl.glColor4f(1f,1f,1f,1f); //reset the glColor to white for textures
		}

		// Flush the OpenGL buffer.
		gl.glFlush();
	}

	/**
	 * displayChanged(GLAutoDrawable, boolean, boolean) is called upon whenever
	 * the display mode changes.
	 * <p>
	 * Implemented through GLEventListener. Seeing as this does not happen very
	 * often, we leave this unimplemented.
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		// GL gl = drawable.getGL();
	}

	/**
	 * reshape(GLAutoDrawable, int, int, int, int, int) is called upon whenever
	 * the viewport changes shape, to update the viewport setting accordingly.
	 * <p>
	 * Implemented through GLEventListener. This mainly happens when the window
	 * changes size, thus changin the canvas (and the viewport that OpenGL
	 * associates with it). It adjust the projection matrix to accomodate the
	 * new shape.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		// Setting the new screen size and adjusting the viewport.
		screenWidth = width;
		screenHeight = height;
		this.game.setScreenHeight(screenHeight);
		this.game.setScreenWidth(screenWidth);

		editBoxManager.reshape(screenWidth, screenHeight);
		this.clkbxman.reshape(screenWidth, screenHeight); // to reshape the text accordingly
		this.optclkbxman.reshape(screenWidth, screenHeight);

		// Set the new projection matrix.
		DrawingUtil.perspectiveProjection(gl, glu, FOV, screenWidth, screenHeight);
	}

	/*
	 * **********************************************
	 * * Methods * **********************************************
	 */

	/**
	 * updateCamera() updates the camera position and orientation.
	 * <p>
	 * This is done by copying the locations from the Player, since MazeRunner
	 * runs on a first person view.
	 */

	private void updateCamera() {

		camera.setLocation(editor.getLocation());
		camera.setHorAngle(editor.getHorAngle());
		camera.setVerAngle(editor.getVerAngle());

		camera.calculateVRP();
	}

	// Getter functions

	public GLCanvas getCanvas(){
		return canvas;
	}

	public void Pause(){
		pause = true;
		input.reset();
	}

	public void unPause(){
		pause = false;
		input.reset();
	}
	
	public void OptPause(){
		this.optpause = true;
	}
	
	public void unOptPause(){
		this.optpause = false;
	}

	private void AddListening(UserInput input){
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);
		canvas.addKeyListener(input);
		canvas.addMouseWheelListener(input);
	}
}