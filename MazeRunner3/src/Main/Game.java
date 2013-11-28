package Main;

import GameStates.gStateMan;

import java.awt.*;
import java.awt.event.*;

public class Game extends Frame{
		static final long serialVersionUID = 7526471155622776147L;

		/*
	 * **********************************************
	 * *			Local variables					*
	 * **********************************************
	 */
		private int screenWidth = 1024, screenHeight = 576;		// Screen size.
		
	/*
	 * **********************************************
	 * *		Initialization methods				*
	 * **********************************************
	 */
		/**
		 * Screen creation
		 */
		public Game() {
			// Make a new window.
			super("MadBalls");
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
			
			new gStateMan(this);
		
			// The two lines here set the screen to full screen
			this.setUndecorated(true);
			this.setExtendedState(MAXIMIZED_BOTH);
			
			// Set the frame to visible. This automatically calls upon OpenGL to prevent a blank screen.
			setVisible(true);
		}
		
		public int getScreenWidth(){
			return this.screenWidth;
		}
		
		public int getScreenHeight(){
			return this.screenHeight;
		}
		
		public void setScreenHeight(int ScHeight){
			this.screenHeight = ScHeight;
		}
		
		public void setScreenWidth(int ScWidth){
			this.screenWidth = ScWidth;
		}
		
		public static void main(String[] args) {
			// Create and run MazeRunner.
			new Game();
		}
	}
