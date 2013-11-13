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
	int pos_x;
	int pos_y;
	int next_pos_x;
	int next_pos_y;
	
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
		this.dX = this.next_pos_x - this.pos_x;
		this.dY = this.next_pos_y - this.pos_y;
		this.pos_x = this.next_pos_x;
		this.pos_y = this.next_pos_y;
	}

	/*
	 * **********************************************
	 * *		Input event handlers				*
	 * **********************************************
	 */

	@Override
	public void mousePressed(MouseEvent event)
	{
		// TODO: Detect the location where the mouse has been pressed
		this.pos_x = event.getX();
		this.pos_y = event.getY();
		this.next_pos_x = event.getX();
		this.next_pos_y = event.getY();
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{		
		// TODO: Detect mouse movement while the mouse button is down
		this.next_pos_x = event.getX();
		this.next_pos_y = event.getY();
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		// TODO: Set forward, back, left and right to corresponding key presses
		char key = event.getKeyChar();
		switch (key){
		case 'w': this.forward = true; break;
		case 's': this.back = true; break;
		case 'a': this.left = true; break;
		case 'd' :this.right = true; break;
		default: break;
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		// TODO: Set forward, back, left and right to corresponding key presses
		char key = event.getKeyChar();
		switch (key){
		case 'w': this.forward = false; break;
		case 's': this.back = false; break;
		case 'a': this.left = false; break;
		case 'd' :this.right = false; break;
		default: break;
		}
	}

	/*
	 * **********************************************
	 * *		Unused event handlers				*
	 * **********************************************
	 */
	
	@Override
	public void mouseMoved(MouseEvent event)
	{
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
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		this.next_pos_x = this.pos_x;
		this.next_pos_y = this.pos_y;
	}


}
