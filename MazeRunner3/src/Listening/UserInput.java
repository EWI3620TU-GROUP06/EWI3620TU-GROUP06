package Listening;
import java.awt.Robot;
import java.awt.event.*;

import javax.media.opengl.GLCanvas;

import GameStates.gStateMan;


/**
 * The UserInput class is an extension of the Control class. It also implements three 
 * interfaces, each providing handler methods for the different kinds of user input.
 * <p>
 * For making the assignment, only some of these handler methods are needed for the 
 * desired functionality. The rest can effectively be left empty (i.e. the methods 
 * under 'Unused event handlers').  
 * <p>
 * Note: because of how java is designed, it is not possible for the game window to
 * react to user input if it does not have focus. The user must first click the window 
 * (or alt-tab or something) before further events, such as keyboard presses, will 
 * function.
 * 
 * @author Mattijs Driel
 *
 */
public class UserInput extends Control 
implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener
{
	// TODO: Add fields to help calculate mouse movement
	private int xPos;
	private int yPos;
	private int xdragPos;
	private int ydragPos;
	private gStateMan gsm;
	private Robot robot;
	

	
	private GLCanvas canvas;
	private int boundX;
	private int boundY;

	/**
	 * UserInput constructor.
	 * <p>
	 * To make the new UserInput instance able to receive input, listeners 
	 * need to be added to a GLCanvas.
	 * 
	 * @param canvas The GLCanvas to which to add the listeners.
	 */
	public UserInput(GLCanvas canvas, gStateMan gsm)
	{
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseWheelListener(this);
		this.canvas = canvas;
		try{
			robot = new Robot();
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		this.gsm = gsm;
	}

	/*
	 * **********************************************
	 * *				Updating					*
	 * **********************************************
	 */

	@Override
	public void update()
	{
		boundX = canvas.getBounds().x;
		boundY = canvas.getBounds().y;
		
		// TODO: Set dX and dY to values corresponding to mouse movement
		if (this.gsm.getCurState() == 1 && !this.gsm.getState(this.gsm.getCurState()).getPaused()){
			int gamePosX = (int) gsm.getGame().getLocationOnScreen().getX();
			int gamePosY = (int) gsm.getGame().getLocationOnScreen().getY();
			int midScreenWidth = gsm.getGame().getWidth()/2;
			int midScreenHeight = gsm.getGame().getHeight()/2;
			this.dX = (xdragPos - midScreenWidth);
			this.dY = (ydragPos - midScreenHeight);
			
			robot.mouseMove(midScreenWidth + gamePosX + boundX, midScreenHeight + gamePosY + boundY);
			xdragPos = midScreenWidth;
			ydragPos = midScreenHeight;
			
			
		}
		if (this.gsm.getCurState() == 2){
			this.dX = (xdragPos - xPos);
			this.dY = (ydragPos - yPos);
			this.xPos = xdragPos;
			this.yPos = ydragPos;	
		}
	}

	/*
	 * **********************************************
	 * *		Input event handlers				*
	 * **********************************************
	 */

	@Override
	public void mousePressed(MouseEvent event)
	{
		if(event.getButton() == 1){
			leftButtonDragged = true;
			leftButtonPressed = true;
		}
		if(event.getButton() == 3){
			rightButtonDragged = true;
		}

		this.xdragPos = event.getX();
		this.ydragPos = event.getY();

		this.mouseX = event.getX();
		this.mouseY = event.getY();
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		this.xdragPos = event.getX();
		this.ydragPos = event.getY();

		this.mouseX = event.getX();
		this.mouseY = event.getY();
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		if (this.gsm.getCurState() != 0){
			if (event.getKeyChar() == 'w'){
				this.forward = true;
			}
			else if (event.getKeyChar() == 'a'){
				this.left = true;
			}
			else if (event.getKeyChar() == 's'){
				this.back = true;
			}
			else if (event.getKeyChar() == 'd'){
				this.right = true;
			}
			else if (event.getKeyCode() == 32){
				this.jump = true;
			}
			if (event.getKeyCode() == 27){
				if (this.gsm.getState(this.gsm.getCurState()).getPaused() == true){
					this.gsm.getState(this.gsm.getCurState()).unPause();
				}
				else{
					this.gsm.setPauseState();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		if (event.getKeyChar() == 'w'){
			this.forward = false;
		}
		else if (event.getKeyChar() == 'a'){
			this.left = false;
		}
		else if (event.getKeyChar() == 's'){
			this.back = false;
		}
		else if (event.getKeyChar() == 'd'){
			this.right = false;
		}
		else if (event.getKeyCode() == 32){
			this.jump = false;
		}
	}

	/*
	 * **********************************************
	 * *		Not so Unused event handlers		*
	 * **********************************************
	 */

	@Override
	public void mouseMoved(MouseEvent event)
	{
		this.xdragPos = event.getX();
		this.ydragPos = event.getY();

		this.mouseX = event.getX();
		this.mouseY = event.getY();
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		this.mouseX = event.getX();
		this.mouseY = event.getY();
		mouseClicked = (byte)event.getButton();

		if(event.getButton() == 1)
			leftReleased = false;
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
	}

	//Comment

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
			if(event.getButton() == 1){
				leftButtonDragged = false;
				leftReleased = true;
			}
			if(event.getButton() == 3)
				rightButtonDragged = false;
	}

	public void mouseWheelMoved(MouseWheelEvent event)
	{
		notches += event.getWheelRotation();
	}
	
}
