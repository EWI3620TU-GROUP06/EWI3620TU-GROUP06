package MainGame;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.*;

import Audio.Audio;
import Drawing.*;
import GameObjects.Camera;
import GameObjects.MoveableBox;
import GameObjects.Player;
import GameObjects.PowerUp;
import GameStates.GameState;
import GameStates.gStateMan;
import HighScore.SqlReadWrite;
import HighScore.Score;
import LevelHandling.Level;
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
	private TextBox deadclkbx;
	private TextBoxManager finishclbxman;
	private TextBox scoreBox;
	private TextBox totalScoreBox;
	private int currentScore = 0;
	private int timer = 0;
	float lightPosition[] = { (float)level.getMaze().getSize()/2f, 50.0f, (float)level.getMaze().getSize()/2f, 1.0f };
	private SkyBox skybox;
	private boolean playingsound;

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
		MoveableBox.resetIDs();
		this.game = game;
		this.state = state;
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
		initJOGL();							// Initialize JOGL.
		initObjects();						// Initialize all the objects!
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
		game.add(canvas);
		/* We need to add a GLEventListener to interpret OpenGL events for us. Since MazeRunner implements
		 * GLEventListener, this means that we add the necesary init(), display(), displayChanged() and reshape()
		 * methods to this class.
		 * These will be called when we are ready to perform the OpenGL phases of MazeRunner. 
		 */
		canvas.addGLEventListener( this );
		canvas.requestFocus();

		hideCursor();

		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		anim = new FPSAnimator(canvas,65);
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
		state.setFinished(false);
		
		String[] sounds = new String[]{"tick","test","test2"};
		playingsound = false;
		Audio.initSounds(sounds);
		
		// Add the maze that we will be using.

		if (level == null){
			level = Level.readLevel(new File("src/Levels/level1.mz"));
		}
		level.getMaze().removeRedundantFaces();

		Physics physics = new Physics(level.getMaze(), state.getDiffNumber());

		MoveableBox newBox = new MoveableBox(new Vector3d(30, 0, 10), 5, 5);
		
		newBox.addToPath(1000, new Vector3f(35, 0, 0));
		newBox.addToPath(7000, new Vector3f(-5, 0, 0));
		newBox.setCount(1);
		level.addMoveableBox(newBox);

		// Initialize the player.
		Vector3d playerPos = new Vector3d(level.getMaze().getStart()[0], level.getMaze().getStart()[1], level.getMaze().getStart()[2]);
		player = new Player(playerPos, level.getMaze().getStart()[3], -45, level.getMaze(), physics, state.getDiffNumber());

		visibleObjects.add(player);
		
		PowerUp newPower = new PowerUp(new Vector3d(22.5, 2.5, 42.5), PowerUp.COIN);
		PowerUp newPower2 = new PowerUp(new Vector3d(22.5, 2.5, 35.5), PowerUp.SPEED);
		PowerUp newPower3 = new PowerUp(new Vector3d(24.5, 2.5, 35.5), PowerUp.JUMP);
		level.addPowerUp(newPower);
		level.addPowerUp(newPower2);
		level.addPowerUp(newPower3);

		Swarm particles = new Swarm(physics, level.getMaze(), (int) (level.getMaze().MAZE_SIZE_X*(state.getDiffNumber() + 1))/4, state.getDiffNumber());
		particles.setCognitive(0.055f);
		particles.setSocial(0.055f);
		particles.setInertiaWeight(0.95f);
		particles.generate((int) (level.getMaze().MAZE_SIZE_X*(state.getDiffNumber() + 1))/4);
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

	private void initMenuText(){
		String[] commands = {"Resume", "Highscores", "Options", "Main Menu", "Quit"};
		String[] optcommands = {"Toggle Fullscreen", "Back"};
		String[] highscorecommands = {"Next Level"}; 
		this.clkbxman = TextBoxManager.createMenu(screenWidth, screenHeight, "Pause", commands, this.state.getGSM());
		this.optclkbxman = TextBoxManager.createMenu(screenWidth, screenHeight, "Options", optcommands, this.state.getGSM());
		this.finishclbxman = TextBoxManager.createFinishMenu(screenWidth, screenHeight, highscorecommands, this.state.getGSM(), input);
		
		this.clkbxman.setControl(input);
		this.optclkbxman.setControl(input);
		this.finishclbxman.setControl(input);
		float[] white = {1, 1, 1, 1};
		this.scoreBox = TextBox.createHighscoreBox(0.02f, 0.8f, 
				screenWidth, screenHeight, 22, "Score: 0", white);
		this.totalScoreBox = TextBox.createHighscoreBox(0.02f, 0.9f,
				screenWidth, screenHeight, 22, "Total Score: " + state.getScore(), white);
		this.deadclkbx = TextBox.createTitle(0.5f, 0.5f, 
				screenWidth, screenHeight, 6, "You Have Died!");
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
			if(!finished && (level.getMaze().isFinish(pos[0], pos[2])))	
				{
					finished = true;
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
			if(dead)
				deadclkbx.drawText(0);
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
				Audio.playSound("test2");
				playingsound = true;
			}
			finishclbxman.drawAllText(deltaTime);
			state.setFinished(true);
			showCursor();
		}
		DrawingUtil.perspectiveProjection(gl, glu, FOV, screenWidth, screenHeight);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_CULL_FACE);
		if(pause && !finished){
			if(optpause){
				this.optclkbxman.update();
			}
			else{
				this.clkbxman.update();
			}
		}
		if (finished)
			this.finishclbxman.update();
		
		gl.glLoadIdentity();
		gl.glFlush();
		
		if(finished){
			String name = "fout";
			if((name = finishclbxman.getText()) != null && state.getLevel() != 0){
				SqlReadWrite.Write(new Score(name, state.getScore()));
				showCursor();
				state.getGSM().setState(gStateMan.HIGHSCORESTATE);
			}
			if(state.getLevel() == 0){
				state.getGSM().setState(gStateMan.HIGHSCORESTATE);
			}
		}
		
		if(dead){
			if(!playingsound){
				Audio.playSound("test2");
				playingsound = true;
				}
			timer += deltaTime;
			if(timer > 2000){
				showCursor();
				state.getGSM().setState(gStateMan.HIGHSCORESTATE);
			}
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

		//Init the manudrawing elements to render title etc.
		this.clkbxman.reshape(screenWidth, screenHeight);
		this.optclkbxman.reshape(screenWidth, screenHeight);

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
	 * This is done by copying the locations from the Player, since MazeRunner runs on a first person view.
	 */

	private void updateCamera() {
		Vector3d cameraPos = new Vector3d(4 *Math.sin( Math.toRadians(player.getHorAngle())) * Math.cos( Math.toRadians(player.getVerAngle())),
				Math.sin(Math.toRadians(player.getVerAngle())) + 1.5,
				4 *Math.cos( Math.toRadians(player.getHorAngle())) * Math.cos(Math.toRadians(player.getVerAngle()))); 
		cameraPos.add(player.getLocation());

		camera.setLocation( cameraPos);
		camera.setHorAngle( player.getHorAngle() );
		camera.setVerAngle( player.getVerAngle() );
		camera.calculateVRP();
		
		skybox.moveTo(new Vector3f((float) (camera.getLocation().x - 115), (float) (camera.getLocation().y - 115), (float) (camera.getLocation().z - 115)));
	}

	public void Pause(){
		pause = true;
		input.reset();
		if(!dead)
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

	private void AddListening(UserInput input){
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);
		canvas.addKeyListener(input);
	}
}