package MainGame;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.vecmath.Vector3d;

import Audio.Audio;
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
 * the mazeEditor class sets up the maze editor which is one of the four main game states in this game.
 * <p>
 * the maze editor let us create custom mazes and create levels in wich the game "Mad Balls" can be played.
 *  
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
	 * * Initialization methods					    * 
	 * **********************************************
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
	 * the init jogl method sets up the opengl environment and canvas in wich the editor options and the edited
	 * maze is drawn.
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
	 * the init objects intializes all the objects that need to be displayed in the editor such as the maze.
	 */
	private void initObjects() {
		// We define an ArrayList of VisibleObjects to store all the objects
		// that need to be
		// displayed by the editor.
		visibleObjects = new ArrayList<VisibleObject>();
		// Add the maze that we will be using.
		level = new Level(new Maze());

		visibleObjects.add(level.getMaze());

		editor = new Editor(new Vector3d(level.getMaze().getSizeX() / 2, 60, level.getMaze().getSizeZ()/2), 0, -89.99999);
		input = state.getGSM().getInput();
		editor.initSet(level, FOV, input);
		
		camera = new Camera(editor.getLocation(), editor.getHorAngle(), editor.getVerAngle());		

		editBoxManager = new EditBoxManager(editor, screenWidth, screenHeight);

		AddListening(input);

		editBoxManager.setControl(input);
		
		String[] sounds = new String[]{"balldrop"};
		Audio.initSounds(sounds);
	}

	/**
	 * the initMenuText method creates the clickboxes for the higscore menu these clickboxes are described in the
	 * drawing package.
	 */
	private void initMenuText(){
		String[] commands = {"Resume", "Options","Main Menu", "Quit"};
		String[] optcommands = {"Toggle Fullscreen", "Back"};
		this.clkbxman = TextBoxManager.createMenu(screenWidth, screenHeight, "Pause", commands, this.state.getGSM());
		this.optclkbxman = TextBoxManager.createOptionsMenu(screenWidth, screenHeight, "Options", optcommands, this.state.getGSM());
		this.clkbxman.setControl(input);
		this.optclkbxman.setControl(input);
	}

	/*
	 * **********************************************
	 * * OpenGL event handlers * **********************************************
	 */

	/**
	 * the init method sets up the drawing mode for the editor and sets up the ligth in the editor
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
		
		level.init(gl);
	}

	/**
	 * display(GLAutoDrawable) is called upon whenever OpenGL is ready to draw a
	 * new frame and handles all of the drawing.
	 * <p>
	 * Implemented through GLEventListener. In order to draw everything needed,
	 * it iterates through the editors list of visibleObjects. For each
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

			if(editor.getLevel() != level || level.changedSomething())
			{
				level.removeFromVisible(visibleObjects);
				level = editor.getLevel();
				level.addToVisible(visibleObjects);
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

		// Display all the visible objects of editor.
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
		level.getMaze().removeRedundantFaces();
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		// GL gl = drawable.getGL();
	}
	 /**
	 * the reshape method reshapes all the visibles in the canvas and the canvas and frame it self.
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
	 * This is done by copying the locations from the editor.
	 */

	private void updateCamera() {
		level.update(0, new Vector3d(0,0,0));

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
	
	/**
	 * the method Addlinstening adds the listeners needed in the higscore state to the canvas
	 * @param input	the object user input is passed to set the eventlisteners
	 */
	private void AddListening(UserInput input){
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);
		canvas.addKeyListener(input);
		canvas.addMouseWheelListener(input);
	}
}