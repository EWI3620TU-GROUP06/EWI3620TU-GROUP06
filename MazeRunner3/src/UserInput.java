import java.awt.event.*;

import javax.media.opengl.GLCanvas;

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
		implements MouseListener, MouseMotionListener, KeyListener
{
	// TODO: Add fields to help calculate mouse movement
	private int xPos;
	private int yPos;
	private int xdragPos;
	private int ydragPos;
	private boolean inscreen;
	
	/**
	 * UserInput constructor.
	 * <p>
	 * To make the new UserInput instance able to receive input, listeners 
	 * need to be added to a GLCanvas.
	 * 
	 * @param canvas The GLCanvas to which to add the listeners.
	 */
	public UserInput(GLCanvas canvas)
	{
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
	}
	
	/*
	 * **********************************************
	 * *				Updating					*
	 * **********************************************
	 */

	@Override
	public void update()
	{
		// TODO: Set dX and dY to values corresponding to mouse movement
		if(this.inscreen){
			//only reset for next mouse-drag if mouse in-screen!
			  this.dX = (xdragPos - xPos);
			  this.dY = (ydragPos - yPos);
			  this.xPos = xdragPos;
			  this.yPos = ydragPos;	
		}
		else{
			//Now we still rotate when the mouse has left the screen!
				if((xdragPos - xPos) != 0 && (ydragPos - yPos) == 0){
					this.dX = 1*(xdragPos - xPos)/(Math.abs(xdragPos - xPos));
					this.dY = 0;
				}
				else if((xdragPos - xPos) == 0 && (ydragPos - yPos) != 0){
					this.dX = 0;
					this.dY = 1*(ydragPos - yPos)/(Math.abs(ydragPos - yPos));
				}
				else if((xdragPos - xPos) != 0 && (ydragPos - yPos) != 0){
					this.dX = 1*(xdragPos - xPos)/(Math.abs(xdragPos - xPos));
					this.dY = 1*(ydragPos - yPos)/(Math.abs(ydragPos - yPos));
				}
				else{
					this.dX = 0;
					this.dY = 0;
				}
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
		// Detect the location where the mouse has been pressed
    	this.xPos = event.getX();
		this.yPos = event.getY();
		xdragPos = xPos;
		ydragPos = yPos;	
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		// TODO: Detect mouse movement while the mouse button is down
		this.xdragPos = event.getX();
		this.ydragPos = event.getY();
	}

	@Override
	public void keyPressed(KeyEvent event)
	{

		// TODO: Set forward, back, left and right to corresponding key presses
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
	}

	@Override
	public void keyReleased(KeyEvent event)
	{

		// TODO: Set forward, back, left and right to corresponding key presses
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
	}

	/*
	 * **********************************************
	 * *		Not so Unused event handlers		*
	 * **********************************************
	 */
	
	@Override
	public void mouseMoved(MouseEvent event)
	{
		//We wanna control without clicking!
		this.xdragPos = event.getX();
		this.ydragPos = event.getY();
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		this.inscreen = true;
	}
	
	//Comment
	
	@Override
	public void mouseExited(MouseEvent event)
	{
		this.inscreen = false;
		this.xdragPos = event.getX();
		this.ydragPos = event.getY();
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
	}


}
