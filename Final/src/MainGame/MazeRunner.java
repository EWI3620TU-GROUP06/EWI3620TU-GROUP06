package MainGame;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.*;

import Audio.Audio;
import Drawing.*;
import GameObjects.Camera;
import GameObjects.Player;
import GameStates.GameState;
import LevelHandling.Level;
import LevelHandling.Maze;
import Listening.HighscoreCommand;
import Listening.NextLevelCommand;
import Listening.UserInput;
import Main.Game;
import MazeObjects.SkyBox;
import PSO.Swarm;
import Physics.Physics;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/** 
 * MazeRunner is the base class of the gameplay.
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
	public static Camera camera;
	private Physics physics;
	private UserInput input;								// The user input object that controls the player.
	private static Level level;									// The maze.
	private long previousTime; // Used to calculate elapsed time.
	private int timeSinceStart = 0;
	private GameState state;
	private GLCanvas canvas;
	private Game game;
	private FPSAnimator anim;
	private boolean pause;
	private boolean optpause;
	private boolean dead = false;
	private boolean finished = false;
	private float FOV = 60;
	private TextBoxManager clkbxman;
	private TextBoxManager optclkbxman;
	private TextBoxManager finishclkbxman;
	private TextBoxManager finalclkbxman;
	private TextBox scoreBox;
	private TextBox totalScoreBox;
	private TextBox finishScoreBox;
	private int currentScore = 0;
	float lightPosition[] = { (float)level.getMaze().getSizeX()/2f, 50.0f, (float)level.getMaze().getSizeZ()/2f, 1.0f };
	private SkyBox skybox;
	private boolean playingsound;
	
	private final static int finalLevel = 6;
	
	public static boolean camColl = false;
	public static float distance = 4;
	
	/*
	 * **********************************************
	 * *		Initialization methods				*
	 * **********************************************
	 */
	/**
	 * Initializes the complete MazeRunner game.
	 * <p>
	 * MazeRunner creates a canvas on 
	 * itself where JOGL will be able to paint the OpenGL graphics. It then initializes all 
	 * game components and initializes JOGL, giving it the proper settings to accurately 
	 * display MazeRunner. Finally, it adds itself as the OpenGL event listener, to be able 
	 * to function as the view controller.
	 */
	public MazeRunner(Game game, GameState state) {
		this.game = game;
		this.state = state;
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
		initJOGL();							// Initialize JOGL.
		initObjects();						// Initialize all the objects!
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
		game.add(canvas);
		/* We need to add a GLEventListener to interpret OpenGL events for us. Since MazeRunner implements
		 * GLEventListener, this means that we add the necesary init(), display(), displayChanged() and reshape()
		 * methods to this class.
		 * These will be called when we are ready to perform the OpenGL phases of MazeRunner. 
		 */
		canvas.addGLEventListener( this );
		canvas.requestFocus();

		hideCursor(); //hide cursor while playing

		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		anim = new FPSAnimator(canvas,50);
		anim.start();
	}

	public static void setLevel(Level lvl){
		level = lvl;
	}

	/**
	 * initializeObjects() creates all the objects needed for the game to start normally.
	 * <p>
	 * This includes the following:
	 * <ul>
	 * <li> the default level
	 * <li> the Player
	 * <li> the Camera
	 * <li> the physics
	 * <li> the sounds
	 * <li> the skybox
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
		state.setFinished(false);
		
		String[] sounds = new String[]{"wallcollide","balldrop","ballcollide","gameover","finish","jumpup","coin","speedup","button"};
		playingsound = false;
		Audio.initSounds(sounds);
		
		// Add the maze that we will be using.

		if (level == null){
			level = Level.readLevel(new File("src/Levels/level1.mz"));
		}
		level.getMaze().removeRedundantFaces();

		physics = new Physics(level.getMaze(), state.getDiffNumber());

		// Initialize the player.
		Vector3d playerPos = new Vector3d(level.getMaze().getStart()[0], level.getMaze().getStart()[1], level.getMaze().getStart()[2]);
		player = new Player(playerPos, -level.getMaze().getStart()[3], -45, level.getMaze(), physics, state.getDiffNumber());
		visibleObjects.add(player);

		int numberOfEnemies = (int) ((Maze.MAZE_SIZE_X + Maze.MAZE_SIZE_Z)/2 + 5)/(state.getDiffNumber() + 3);
		Swarm particles = new Swarm(physics, level.getMaze(), numberOfEnemies, state.getDiffNumber());
		particles.setCognitive(0.055f);
		particles.setSocial(0.055f);
		particles.setInertiaWeight(0.95f);
		particles.generate(numberOfEnemies);
		level.addSwarm(particles);
		physics.initParticles(particles);
		physics.initContactHandling(); //Initializes sound-handling. MUST BE AFTER initParticles() TO INCLUDE PARTICLE-SOUNDS

		camera = new Camera(player.getLocation(), player.getHorAngle(), player.getVerAngle() );
		
		skybox = new SkyBox(230, 230, (float) (camera.getLocation().x - 115), (float) (camera.getLocation().y - 115), (float) (camera.getLocation().z - 115));
		
		visibleObjects.add(skybox);
		
		input = state.getGSM().getInput();
		AddListening(input);
		player.setControl(input);
		
		level.addToVisible(visibleObjects);
		level.setAttributes(player, physics, this);
	}
	 /**
	 * the initMenuText method creates the clickboxes for the pause menu these clickboxes are described in the
	 * drawing package.
	 */
	private void initMenuText(){
		float[] white = {1, 1, 1, 1};
		String[] commands = {"Resume", "Highscores", "Options", "Main Menu", "Quit"};
		String[] optcommands = {"Toggle Fullscreen", "Back"};
		this.clkbxman = TextBoxManager.createMenu(screenWidth, screenHeight, "Pause", commands, this.state.getGSM());
		this.optclkbxman = TextBoxManager.createOptionsMenu(screenWidth, screenHeight, "Options", optcommands, this.state.getGSM());
		this.finishclkbxman = new TextBoxManager();
		finishclkbxman.AddBox(TextBox.createTitle(0.5f, 0.60f, screenWidth, screenHeight, 12, "Level clear!"));
		finishclkbxman.AddBox(TextBox.createHighscoreBox(0.1f, 0.4f, screenWidth, screenHeight, 20, "Score on this level:", white));
		finishScoreBox = TextBox.createHighscoreBox(0.7f, 0.4f, screenWidth, screenHeight, 20, "0", white);
		finishclkbxman.AddBox(finishScoreBox);
		if(state.getLevel() != 0){
			TextBox newBox = TextBox.createMenuBox(0.5f, 0.2f, screenWidth, screenHeight, 20, "Next Level");
			newBox.setCommand(new NextLevelCommand(this.state.getGSM()));
			finishclkbxman.AddBox(newBox);
		}
		else{
			TextBox newBox = TextBox.createMenuBox(0.5f, 0.2f, screenWidth, screenHeight, 20, "To Highscores");
			newBox.setCommand(new HighscoreCommand(this.state.getGSM()));
			finishclkbxman.AddBox(newBox);
		}
		finalclkbxman = TextBoxManager.createDeadMenu(screenWidth, screenHeight, this.state.getGSM(), input);
		this.clkbxman.setControl(input);
		this.optclkbxman.setControl(input);
		this.finishclkbxman.setControl(input);
		this.finalclkbxman.setControl(input);
		
		this.scoreBox = TextBox.createHighscoreBox(0.02f, 0.8f, 
				screenWidth, screenHeight, 22, "Score: 0", white);
		this.totalScoreBox = TextBox.createHighscoreBox(0.02f, 0.9f,
				screenWidth, screenHeight, 22, "Total Score: " + state.getScore(), white);

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
		
		level.init(gl);
		skybox.init(gl);
		player.init(gl);

		gl.glClearColor(0, 0, 0, 0);								// Set the background color.

		// Now we set up our viewpoint.
		DrawingUtil.perspectiveProjection(gl, glu, FOV, screenWidth, screenHeight);

		// Enable back-face culling.
		gl.glCullFace( GL.GL_BACK );
		gl.glEnable( GL.GL_CULL_FACE );
		gl.glEnable(GL.GL_SMOOTH);
		gl.glShadeModel(GL.GL_SMOOTH);

		// Enable Z-buffering.
		gl.glEnable( GL.GL_DEPTH_TEST );

		// Set and enable the lighting.		
		float lightColour[] = { 0.1f, 0.1f, 0.1f, 1.0f };	// No ambient
		float diffColour[] = {1f, 1f, 1f, 1.0f}; //White diffuse
		float specColour[] = {1f, 1f, 1f, 1.0f}; //white specular
		gl.glLightfv( GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0 );	// Note that we're setting Light0.
		gl.glLightfv( GL.GL_LIGHT0, GL.GL_AMBIENT, lightColour, 0);
		gl.glLightfv( GL.GL_LIGHT0, GL.GL_SPECULAR, specColour, 0);
		gl.glLightfv( GL.GL_LIGHT0, GL.GL_DIFFUSE, diffColour, 0);
		gl.glEnable( GL.GL_LIGHTING );
		gl.glEnable( GL.GL_LIGHT0 );

		// Set the shading model.
		gl.glShadeModel( GL.GL_SMOOTH );
		previousTime = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * the display method draws all the visibles on the canvas. And gives us all the possibilities to draw the
	 * to draw overlays in the mazerunner when paused, finished or died.
	 */
	public void display(GLAutoDrawable drawable) {
		
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		
		Calendar now = Calendar.getInstance();		
		long currentTime = now.getTimeInMillis();
		int deltaTime = (int)(currentTime - previousTime);
		previousTime = currentTime;
		//Only update the movement&camera when not in pause state

		if (!pause || finished){
			// Calculating time since last frame.
			timeSinceStart += deltaTime;
			if(!finished && state.getLevel() != 0)
				currentScore = 200*(state.getDiffNumber()+1) - (timeSinceStart*((state.getDiffNumber()*(int)1.2)+1)) / 1000;
			if(currentScore == 0 && state.getLevel() != 0){
				dead = true;
				state.getGSM().setPauseState();
			}
			scoreBox.setText("Score: " + currentScore);
			totalScoreBox.setText("Total Score: " + state.getScore());

			// Update any movement since last frame.
			level.update(deltaTime, player.getLocation());
			player.update(deltaTime);
			updateCamera();
		
			double pos[] = new double[3];
			player.getLocation().get(pos);
			if(pos[1] < -10){
				dead = true;
				state.getGSM().setPauseState();
			}
			if(!finished && (level.getMaze().isFinish(pos[0], pos[1], pos[2])))	
				{
					finished = true;
					finishScoreBox.setText(Integer.toString(currentScore));
					if (state.getLevel() != 0)
						state.setScore(currentScore);
				}	
		}
		//Always change the camera and draw the game-world
		double[] pos = new double[3];
		double[] vuv = new double[3];
		double[] vrp = new double[3];
		camera.getLocation().get(pos);
		camera.getVuv().get(vuv);
		camera.getVrp().get(vrp);

		glu.gluLookAt(pos[0], pos[1], pos[2], 
				vrp[0], vrp[1], vrp[2], vuv[0], vuv[1], vuv[2]);

		gl.glLightfv( GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0 );	
		
		// Display all the visible objects of MazeRunner.
		for( Iterator<VisibleObject> it = visibleObjects.iterator(); it.hasNext(); ) {
			it.next().display(gl);
		}
		DrawingUtil.orthographicProjection(gl, screenWidth, screenHeight);
		gl.glDisable(GL.GL_DEPTH_TEST);
		scoreBox.drawText(0);
		totalScoreBox.drawText(0);
		//Draw the menu if pause state

		if(pause && !finished){
			player.pause();
			level.pause();

			DrawingUtil.drawTrans(gl, 0, 0, screenWidth, screenHeight, 0.2f, 0.2f, 0.2f, 0.4f);
			if(dead){
				finalclkbxman.drawAllText(deltaTime);
				if(!playingsound){
					Audio.playSound("gameover");
					playingsound = true;
				}
			}
			else{
				if(optpause){
					this.optclkbxman.drawAllText(0);
				}
				else{
					this.clkbxman.drawAllText(0);
				}
			}

		}
		if(finished){
			if(!playingsound){
				Audio.playSound("finish");
				playingsound = true;
			}
			if(state.getLevel() == finalLevel)
			{
				finalclkbxman.setTitle("Game Complete!", 10);
				finalclkbxman.drawAllText(deltaTime);
			}
			else
			{
				finishclkbxman.drawAllText(deltaTime);
			}
			state.setFinished(true);
			showCursor();
		}
		DrawingUtil.perspectiveProjection(gl, glu, FOV, screenWidth, screenHeight);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_CULL_FACE);
		
		if(dead){
			this.finalclkbxman.update();
		}
		else if (finished){
			if(state.getLevel() == finalLevel)
				this.finalclkbxman.update();
			else
				this.finishclkbxman.update();
		}
		else if(pause ){
			if(optpause){
				this.optclkbxman.update();
			}
			else{
				this.clkbxman.update();
			}
		}

		gl.glLoadIdentity();
		gl.glFlush();
	}


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

		//Init the manudrawing elements to render title etc.
		this.clkbxman.reshape(screenWidth, screenHeight);
		this.optclkbxman.reshape(screenWidth, screenHeight);
		this.finishclkbxman.reshape(screenWidth, screenHeight);
		this.finalclkbxman.reshape(screenWidth, screenHeight);

		// Set the new projection matrix.
		DrawingUtil.perspectiveProjection(gl, glu, FOV, screenWidth, screenHeight);
	}

	/*
	 * **********************************************
	 * *				Methods						*
	 * **********************************************
	 */

	/**
	 * updateCamera() updates the camera position and orientation.
	 * <p>
	 * The camera is mostly 4 behind and 1.5 above the player ball, for better overview of the surroundings. 
	 * However, it is moved closer to the player when the camera would move into a solid object.
	 */

	private void updateCamera() {
		distance = 4;
		Vector3d cameraPos = new Vector3d(distance * Math.sin( Math.toRadians(player.getHorAngle())) * Math.cos( Math.toRadians(player.getVerAngle())),
				Math.sin(Math.toRadians(player.getVerAngle())) + 1.5,
				distance * Math.cos( Math.toRadians(player.getHorAngle())) * Math.cos(Math.toRadians(player.getVerAngle()))); 
		cameraPos.add(player.getLocation());
		
		if(!physics.cameraInWall((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z)){
			camera.setLocation( cameraPos);
			camera.setHorAngle( player.getHorAngle() );
		}
		else{
			while(physics.cameraInWall((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z) && distance >=0){
				distance -= 0.1f;
				cameraPos = new Vector3d(distance *Math.sin( Math.toRadians(player.getHorAngle())) * Math.cos( Math.toRadians(player.getVerAngle())),
					Math.sin(Math.toRadians(player.getVerAngle())) + 1.5,
					distance *Math.cos( Math.toRadians(player.getHorAngle())) * Math.cos(Math.toRadians(player.getVerAngle())));
				cameraPos.add(player.getLocation());
			}
			distance -= 0.5f;
			cameraPos = new Vector3d(distance *Math.sin( Math.toRadians(player.getHorAngle())) * Math.cos( Math.toRadians(player.getVerAngle())),
				Math.sin(Math.toRadians(player.getVerAngle())) + 1.5,
				distance *Math.cos( Math.toRadians(player.getHorAngle())) * Math.cos(Math.toRadians(player.getVerAngle())));
			cameraPos.add(player.getLocation());
			camera.setLocation( cameraPos);
			camera.setHorAngle( player.getHorAngle() );
		}
		
		camera.setVerAngle( player.getVerAngle() );
		camera.calculateVRP();
		
		skybox.moveTo(new Vector3f((float) (camera.getLocation().x - 115), (float) (camera.getLocation().y - 115), (float) (camera.getLocation().z - 115)));
	}
	
	/**
	 * method used to pause the game only possible when the player is not dead also shows the cursor in the canvas
	 */
	public void Pause(){
		pause = true;
		this.optpause = false;
		input.reset();
		showCursor();
	}

	public void OptPause(){
		this.optpause = true;
	}
	
	public void unPause(){
		if(!dead)
		{
			previousTime = Calendar.getInstance().getTimeInMillis();
			pause = false;
			input.reset();
			hideCursor();
		}
	}
	/**
	 * funcitonallity discrebed below
	 * @param addition		the parameter addition is used to add score to the current score when the player picked
	 * up the powerup add score
	 */
	public void addScore(int addition)
	{
		timeSinceStart -= addition * 1000;
	}

	public void unOptPause(){
		this.optpause = false;
	}

	private void hideCursor(){
		game.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(0,0),""));
	}

	private void showCursor(){
		game.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	/**
	* the method Addlinstening adds the listeners needed in the higscore state to the canvas
	* @param input	the object user input is passed to set the eventlisteners
	*/
	private void AddListening(UserInput input){
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);
		canvas.addKeyListener(input);
	}
	
	public static int getFinalLevel(){
		return finalLevel;
	}
}