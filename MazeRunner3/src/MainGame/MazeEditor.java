package MainGame;
import java.awt.*;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import com.sun.opengl.util.*;

import java.util.ArrayList;
import java.util.Calendar;
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
public class MazeEditor extends Frame implements GLEventListener {
	static final long serialVersionUID = 7526471155622776147L;

	/*
	 * **********************************************
	 * * Local variables * **********************************************
	 */
	private GLCanvas canvas;

	private int screenWidth = 600, screenHeight = 600; // Screen size.
	private float buttonSize = screenWidth / 11.0f < screenHeight / 10.f ? screenWidth / 11.0f : screenHeight / 10.f;
	private float FOV = 45.0f;

	private ArrayList<VisibleObject> visibleObjects; // A list of objects that will be displayed on screen.
	private Player player;
	private Camera camera; // The camera object.
	private Editor editor; // The editor object;
	private UserInput input; // The user input object that controls the player/editor.
	private Maze maze; // The maze.
	private long previousTime = Calendar.getInstance().getTimeInMillis();
	
	boolean editing = true;

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
	public MazeEditor() {
		// Make a new window.
		super("MazeEditor");

		// Let's change the window to our liking.
		setSize(screenWidth, screenHeight);
		setBackground(Color.white);

		// The window also has to close when we want to.
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//editor.save();
				System.exit(0);
			}
		});

		initJOGL(); // Initialize JOGL.
		initObjects(); // Initialize all the objects!

		// Set the frame to visible. This automatically calls upon OpenGL to
		// prevent a blank screen.
		setVisible(true);
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
		add(canvas);
		/*
		 * We need to add a GLEventListener to interpret OpenGL events for us.
		 * Since MazeRunner implements GLEventListener, this means that we add
		 * the necesary init(), display(), displayChanged() and reshape()
		 * methods to this class. These will be called when we are ready to
		 * perform the OpenGL phases of MazeRunner.
		 */
		canvas.addGLEventListener(this);

		/*
		 * We need to create an internal thread that instructs OpenGL to
		 * continuously repaint itself. The Animator class handles that for
		 * JOGL.
		 */
		Animator anim = new Animator(canvas);
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
		maze = new Maze();
		visibleObjects.add(maze);
		
		player = new Player(maze.getStart()[0], maze.SQUARE_SIZE/2.0f, maze.getStart()[1], maze.getStart()[2],0);

		editor = new Editor(maze.getSize() / 2, 60, maze.getSize()/2, 0, -89.99999);

		camera = new Camera(editor.getLocationX(), editor.getLocationY(),
				editor.getLocationZ(), editor.getHorAngle(),
				editor.getVerAngle());

		input = new UserInput(canvas);
		player.setControl(input);
		editor.setControl(input);
		editor.setFOV(FOV);
		editor.setMaze(maze);
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
		drawable.setGL(new DebugGL(drawable.getGL())); // We set the OpenGL
		// pipeline to Debugging
		// mode.
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		gl.glClearColor(0, 0, 0, 0); // Set the background color.

		// Now we set up our viewpoint.
		gl.glMatrixMode(GL.GL_PROJECTION); // We'll use orthogonal projection.
		gl.glLoadIdentity(); // Reset the current matrix.
		glu.gluPerspective(60, screenWidth, screenHeight, 200); // Set up the parameters for perspective viewing.
		gl.glMatrixMode(GL.GL_MODELVIEW);

		// Enable back-face culling.
		gl.glCullFace(GL.GL_BACK);
		gl.glEnable(GL.GL_CULL_FACE);

		// Enable Z-buffering.
		gl.glEnable(GL.GL_DEPTH_TEST);

		// Set and enable the lighting.
		float lightPosition[] = { 0.0f, 50.0f, 0.0f, 1.0f }; // High up in the
		// sky!
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

		// Update any movement since last frame.
		editor.update(screenWidth, screenHeight);
		// Calculating time since last frame.
		Calendar now = Calendar.getInstance();		
		long currentTime = now.getTimeInMillis();
		int deltaTime = (int)(currentTime - previousTime);
		previousTime = currentTime;
				
		// Update any movement since last frame.
		player.update(deltaTime);
		updateCamera();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		glu.gluLookAt(camera.getLocationX(), camera.getLocationY(),
				camera.getLocationZ(), camera.getVrpX(), camera.getVrpY(),
				camera.getVrpZ(), camera.getVuvX(), camera.getVuvY(),
				camera.getVuvZ());

		// Display all the visible objects of MazeRunner.
		for (Iterator<VisibleObject> it = visibleObjects.iterator(); it
				.hasNext();) {
			it.next().display(gl);
		}

		// When editing: use an orthographic projection to draw the HUD on the screen, 
		// then set the perspective projection back
		gl.glLoadIdentity();
		orthographicProjection(gl);
		gl.glDisable(GL.GL_LIGHTING);
		drawButtons(gl);
		gl.glEnable(GL.GL_LIGHTING);
		perspectiveProjection(gl, glu);


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

		buttonSize = screenWidth / 11.0f < screenHeight / 10.f ? screenWidth / 11.0f : screenHeight / 10.f;
		editor.setButtonSize(buttonSize);

		// Set the new projection matrix.
		perspectiveProjection(gl, glu);
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
		// Use either the location of the player or the editor to update the camera
		if(editing)
		{
			camera.setLocationX(editor.getLocationX());
			camera.setLocationY(editor.getLocationY());
			camera.setLocationZ(editor.getLocationZ());
			camera.setHorAngle(editor.getHorAngle());
			camera.setVerAngle(editor.getVerAngle());
		} 
		else
		{
			camera.setLocationX(player.getLocationX());
			camera.setLocationY(player.getLocationY());
			camera.setLocationZ(player.getLocationZ());
			camera.setHorAngle(player.getHorAngle());
			camera.setVerAngle(player.getVerAngle());
		}

		camera.calculateVRP();
	}

	/*
	 * Main
	 */

	public static void main(String[] args)
	{
		new MazeEditor();
	}

	/**
	 * A method that draws the top left buttons on the screen.
	 * 
	 * @param gl
	 */
	private void drawButtons(GL gl) {
		// Draw a puls on top of the first box
		gl.glLineWidth(4);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		lineOnScreen(gl, buttonSize / 10.0f, screenHeight - buttonSize / 2.0f,
				buttonSize * 9.0f / 10.0f, screenHeight - buttonSize / 2.0f);
		lineOnScreen(gl, buttonSize / 2.0f, screenHeight - buttonSize / 10.0f,
				buttonSize / 2.0f, screenHeight - buttonSize * 9.0f / 10.0f);

		// Draw a minus on top of the second box.
		lineOnScreen(gl, buttonSize * 1.10f, screenHeight - buttonSize / 2.0f,
				buttonSize * 19.0f / 10.0f, screenHeight - buttonSize / 2.0f);

		// Draw a cube on the fourth box.
		boxOnScreen(gl, buttonSize * 3.10f, screenHeight - buttonSize* 0.9f, buttonSize * 0.8f);

		// Draw a slope on the seventh box.
		slopeOnScreen(gl, buttonSize * 6.10f, screenHeight - buttonSize * 0.9f, buttonSize * 0.8f, buttonSize * 0.8f);

		// Draw a slope on the eighth box.
		slopeOnScreen(gl, buttonSize * 7.10f, screenHeight - buttonSize * 0.9f, buttonSize * 0.8f, buttonSize * 0.4f);

		//Draw a rectangle on the ninth box.
		rectangleOnScreen(gl, buttonSize * 8.10f, screenHeight - buttonSize * 0.9f, buttonSize * 0.8f, buttonSize * 0.4f);

		// Draw the background boxes
		gl.glColor3f(0, 0.5f, 0f);
		boxOnScreen(gl, 0.0f, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0, 0.5f);
		boxOnScreen(gl, buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0, 0);
		boxOnScreen(gl, 2*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0.5f, 0);
		boxOnScreen(gl, 3*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0.5f, 0f);
		boxOnScreen(gl, 4*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0, 0.5f);
		boxOnScreen(gl, 5*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0, 0);
		boxOnScreen(gl, 6*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0.5f, 0);
		boxOnScreen(gl, 7*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0.5f, 0f);
		boxOnScreen(gl, 8*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0, 0.5f);
		boxOnScreen(gl, 9*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0, 0);
		boxOnScreen(gl, 10*buttonSize, screenHeight - buttonSize, buttonSize);

		// Draw line around buttons
		gl.glColor3f(0.8f,  0.8f,  0.8f);
		rectangleOnScreen(gl, 0, screenHeight - buttonSize * 1.1f, buttonSize * 11, buttonSize * 0.1f);
		rectangleOnScreen(gl, buttonSize*11, screenHeight - buttonSize * 1.1f, buttonSize * 0.1f, buttonSize * 1.1f);
	}

	/**
	 * Help method that uses GL calls to draw a line.
	 */
	private void lineOnScreen(GL gl, float x1, float y1, float x2, float y2) {
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glEnd();
	}

	/**
	 * Help method that uses GL calls to draw a square
	 */
	private void boxOnScreen(GL gl, float x, float y, float size)
	{
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + size, y);
		gl.glVertex2f(x + size, y + size);
		gl.glVertex2f(x, y + size);
		gl.glEnd();
	}
	/**
	 * Help method that uses GL calls to draw a slope
	 */
	private void slopeOnScreen(GL gl, float x, float y, float xSize, float ySize)
	{
		gl.glBegin(GL.GL_TRIANGLES);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + xSize, y);
		gl.glVertex2f(x, y + ySize);
		gl.glEnd();
	}

	/**
	 * Help method that uses GL calls to draw a rectangle
	 */
	private void rectangleOnScreen(GL gl, float x, float y, float xSize, float ySize)
	{
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + xSize, y);
		gl.glVertex2f(x + xSize, y + ySize);
		gl.glVertex2f(x, y + ySize);
		gl.glEnd();
	}

	/**
	 * Convenience method to perform an orthographic projection
	 */

	private void orthographicProjection(GL gl)
	{
		gl.glViewport(0, 0, screenWidth, screenHeight);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0.0f, screenWidth, 0.0f, screenHeight, 0.0f, 1.0f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}

	/**
	 * Convenience method to perform a perspective projection
	 */
	private void perspectiveProjection(GL gl, GLU glu)
	{
		gl.glViewport(0, 0, screenWidth, screenHeight);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(FOV, (float)screenWidth / (float)screenHeight, 0.001f, Float.MAX_VALUE); 
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}
}