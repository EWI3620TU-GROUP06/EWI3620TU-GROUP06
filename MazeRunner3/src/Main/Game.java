package Main;

import GameStates.gStateMan;

import java.awt.*;
import java.awt.event.*;

import javax.media.opengl.*;

import com.sun.opengl.util.*;

public class Game extends Frame implements GLEventListener {
		static final long serialVersionUID = 7526471155622776147L;

		/*
	 * **********************************************
	 * *			Local variables					*
	 * **********************************************
	 */
		public GLCanvas canvas;

		private int screenWidth = 600, screenHeight = 600;		// Screen size.
		private gStateMan gsm;
		
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
		public Game() {
			// Make a new window.
			super("Main");
			
			// Let's change the window to our liking.
			setSize( screenWidth, screenHeight);
			setBackground( Color.white );

			// The window also has to close when we want to.
			this.addWindowListener( new WindowAdapter()
			{
				public void windowClosing( WindowEvent e )
				{
					System.exit(0);
				}
			});

			initJOGL();							// Initialize JOGL.
			gsm = new gStateMan(this);
			// Set the frame to visible. This automatically calls upon OpenGL to prevent a blank screen.
			setVisible(true);
		}
		
		/**
		 * initJOGL() sets up JOGL to work properly.
		 * <p>
		 * It sets the capabilities we want for MazeRunner, and uses these to create the GLCanvas upon which 
		 * MazeRunner will actually display our screen. To indicate to OpenGL that is has to enter a 
		 * continuous loop, it uses an Animator, which is part of the JOGL api.
		 */
		private void initJOGL()	{
			// First, we set up JOGL. We start with the default settings.
			GLCapabilities caps = new GLCapabilities();
			// Then we make sure that JOGL is hardware accelerated and uses double buffering.
			caps.setDoubleBuffered( true );
			caps.setHardwareAccelerated( true );

			// Now we add the canvas, where OpenGL will actually draw for us. We'll use settings we've just defined. 
			canvas = new GLCanvas( caps );
			add( canvas );
			/* We need to add a GLEventListener to interpret OpenGL events for us. Since MazeRunner implements
			 * GLEventListener, this means that we add the necesary init(), display(), displayChanged() and reshape()
			 * methods to this class.
			 * These will be called when we are ready to perform the OpenGL phases of MazeRunner. 
			 */
			canvas.addGLEventListener( this );
			
			/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
			 * The Animator class handles that for JOGL.
			 */
			Animator anim = new Animator( canvas );
			anim.start();
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
			drawable.setGL( new DebugGL(drawable.getGL() )); // We set the OpenGL pipeline to Debugging mode.
	        gsm.init(drawable);
	       
//			GL gl = drawable.getGL();
//	        GLU glu = new GLU();
	        
//	        gl.glClearColor(0, 0, 0, 0);								// Set the background color.
	        
	        // Now we set up our viewpoint.
//	        gl.glMatrixMode( GL.GL_PROJECTION );						// We'll use orthogonal projection.
//	        gl.glLoadIdentity();										// Reset the current matrix.
//	        glu.gluPerspective( 60, screenWidth, screenHeight, 200);	// Set up the parameters for perspective viewing.
//	        gl.glMatrixMode( GL.GL_MODELVIEW );
	        
	        // Enable back-face culling.
//	        gl.glCullFace( GL.GL_BACK );
//	        gl.glEnable( GL.GL_CULL_FACE );
	        
	        // Enable Z-buffering.
//	        gl.glDisable( GL.GL_DEPTH_TEST );
	        
	        // Set and enable the lighting.
//	        float lightPosition[] = { 0.0f, 50.0f, 0.0f, 1.0f }; 			// High up in the sky!
//	        float lightColour[] = { 1.0f, 1.0f, 1.0f, 0.0f };				// White light!
//	        gl.glLightfv( GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0 );	// Note that we're setting Light0.
//	        gl.glLightfv( GL.GL_LIGHT0, GL.GL_AMBIENT, lightColour, 0);
//	        gl.glEnable( GL.GL_LIGHTING );
//	        gl.glEnable( GL.GL_LIGHT0 );
//	        
	        // Set the shading model.
//	        gl.glShadeModel( GL.GL_SMOOTH );
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
			 
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
			gl.glLoadIdentity();

	        // Display all the visible objects of MazeRunner.
	        gl.glLoadIdentity();
	        gsm.draw(drawable);
	        // Flush the OpenGL buffer.
	        gl.glFlush();
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
			gsm.reshape(drawable, x, y, width, height);
		}
		
		public int getScreenWidth(){
			return this.screenWidth;
		}
		
		public int getScreenHeight(){
			return this.screenHeight;
		}
		
		public GLCanvas getCanvas(){
			return this.canvas;
		}
	/*
	 * **********************************************
	 * *				  Main						*
	 * **********************************************
	 */
		/**
		 * Program entry point
		 * 
		 * @param args
		 */
		public static void main(String[] args) {
			// Create and run MazeRunner.
			new Game();
		}
	}