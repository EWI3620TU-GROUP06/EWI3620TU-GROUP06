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
	private double x1, y1, moveY, moveX, recX, recY;// TODO: Add fields to help calculate mouse movement
	boolean drag = false;
	
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
		dY=  (int) (recY);
		dX =  (int) (recX);
		// TODO: Set dX and dY to values corresponding to mouse movement
	}

	/*
	 * **********************************************
	 * *		Input event handlers				*
	 * **********************************************
	 */

	@Override
	public void mousePressed(MouseEvent event)
	{
		
		if (event.getButton() == 1){
		x1 = event.getX();
		y1 = event.getY();
		drag = true;
		}
		else if (event.getButton() != 1){
			drag = false;
		}
		// TODO: Detect the location where the mouse has been pressed
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{		
		if (drag == true){
			moveX	= event.getX() - x1;
			moveY = event.getY() - y1;
			recX = moveX + recX;
			recY = moveY + recY;
		}
		
//			System.out.println(recX + " + " + recY );// TODO: Detect mouse movement while the mouse button is down
		
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_A){
			left = true;
			//System.out.println(getLeft());// TODO: Set forward, back, left and right to corresponding key presses
		}
		else if (event.getKeyCode() == KeyEvent.VK_S){
			back = true;
			//System.out.println(getBack());
		}
		else if (event.getKeyCode() == KeyEvent.VK_D){
			right = true;
			//System.out.println(getRight());
		}
		else if (event.getKeyCode() == KeyEvent.VK_W){
			forward = true;
			//System.out.println(getForward());
		}
		else if (event.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_A){
			left = false;
			//System.out.println(getLeft());
		}
		else if (event.getKeyCode() == KeyEvent.VK_S){
			back = false;
			//System.out.println(getBack());
		}
		else if (event.getKeyCode() == KeyEvent.VK_D){
			right = false;
			//System.out.println(getRight());
		}
		else if (event.getKeyCode() == KeyEvent.VK_W){
			forward = false;
			//System.out.println(getForward());
		}
		// TODO: Set forward, back, left and right to corresponding key presses
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
	}


}
