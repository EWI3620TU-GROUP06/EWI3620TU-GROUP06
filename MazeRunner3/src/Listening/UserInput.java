package Listening;
import java.awt.Robot;
import java.awt.event.*;


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
	public UserInput(gStateMan gsm)
	{
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
		boundX = this.gsm.getState(this.gsm.getCurState()).getCanvas().getBounds().x;
		boundY = this.gsm.getState(this.gsm.getCurState()).getCanvas().getBounds().y;
		
		// TODO: Set dX and dY to values corresponding to mouse movement
		if (this.gsm.getCurState() == gStateMan.PLAYSTATE && !this.gsm.getState(this.gsm.getCurState()).getPaused() && !this.gsm.getState(this.gsm.getCurState()).getFinished()){
			int gamePosX = (int) gsm.getGame().getLocationOnScreen().getX();
			int gamePosY = (int) gsm.getGame().getLocationOnScreen().getY();
			int midScreenWidth = gsm.getGame().getWidth()/2;
			int midScreenHeight = gsm.getGame().getHeight()/2;
			this.dX = (xdragPos - midScreenWidth);
			this.dY = (ydragPos - midScreenHeight);
			
			robot.mouseMove(midScreenWidth + gamePosX + boundX, midScreenHeight + gamePosY + boundY); // robot used to hold the mouse in the middle of the screen only in playstate
			xdragPos = midScreenWidth;
			ydragPos = midScreenHeight;
			
			
		}
		if (this.gsm.getCurState() == gStateMan.EDITSTATE){
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
		typedKey = event.getKeyCode();
		if(typedKey == KeyEvent.VK_CAPS_LOCK){
			upperCase = !upperCase;}
		
		if (this.gsm.getCurState() != gStateMan.MENUSTATE){
			if ((event.getKeyChar() == 'w') || (event.getKeyChar() == 'W')){
				this.forward = true;
			}
			else if ((event.getKeyChar() == 'a') || (event.getKeyChar() == 'A')){
				this.left = true;
			}
			else if ((event.getKeyChar() == 's') || (event.getKeyChar() == 'S')){
				this.back = true;
			}
			else if ((event.getKeyChar() == 'd') || (event.getKeyChar() == 'D')){
				this.right = true;
			}
			else if((event.getKeyChar() == 'q'))
				down = true;
			else if (event.getKeyCode() == KeyEvent.VK_SPACE){
				this.jump = true;
			}
			if (event.getKeyCode() == KeyEvent.VK_ESCAPE){
				if (this.gsm.getState(this.gsm.getCurState()).getPaused() == true){
					this.gsm.setUnPauseState();
				}
				else{
					this.gsm.setPauseState();
				}
			}
			else if(event.getKeyCode() == KeyEvent.VK_SHIFT){
				upperCase =  true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		if ((event.getKeyChar() == 'w') || (event.getKeyChar() == 'W')){
			this.forward = false;
		}
		else if ((event.getKeyChar() == 'a') || (event.getKeyChar() == 'A')){
			this.left = false;
		}
		else if ((event.getKeyChar() == 's') || (event.getKeyChar() == 'S')){
			this.back = false;
		}
		else if ((event.getKeyChar() == 'd') || (event.getKeyChar() == 'D')){
			this.right = false;
		}
		else if (event.getKeyChar() == 'q'){
			this.down = false;
		}
		else if (event.getKeyCode() ==  KeyEvent.VK_SPACE){
			this.jump = false;
		}
		else if(event.getKeyCode() == KeyEvent.VK_SHIFT){
			upperCase =  false;
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
			mouseReleased = (byte) event.getButton();
			if(event.getButton() == 1){
				leftButtonDragged = false;
			}
			if(event.getButton() == 3){
				rightButtonDragged = false;
			}
	}

	public void mouseWheelMoved(MouseWheelEvent event)
	{
		notches += event.getWheelRotation();
	}
	
}
