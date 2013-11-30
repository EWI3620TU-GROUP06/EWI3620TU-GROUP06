package Listening;
/**
 * The Control class is an abstract class containing only basic functionality such
 * as getters for all possible commands.
 * <p>
 * An update method is also included, demanding all subclasses to implement it. 
 * This method will be called just before any getters are called. The reason for this
 * is to allow the subclass to set all fields to the most recent input.
 * <p>
 * For the purposes of the assignment, it might seem unnecessary to have the 
 * actual user input in a separate class from this class. Indeed there is no other
 * subclass to Control other than UserInput, but this might change once some sort of
 * AI controlling of GameObjects is desired. Any AI that needs to control an object
 * in the game can use the same methods any human player would use, which makes it a 
 * lot more intuitive to program an AI.
 * 
 * @author Mattijs Driel
 * 
 */
public abstract class Control
{
	protected boolean forward = false;
	protected boolean back = false;
	protected boolean left = false;
	protected boolean right = false;
	protected boolean jump = false;

	protected int dX = 0;
	protected int dY = 0;

	protected int notches = 0;

	protected int mouseX = 0, mouseY = 0;
	protected byte mouseClicked;
	protected boolean rightButtonDragged = false;
	protected boolean leftButtonDragged = false;
	protected byte mouseReleased;
	protected boolean leftButtonPressed = false;

	/**
	 * @return Returns true if forward motion is desired.
	 */
	public boolean getForward()
	{
		return forward;
	}

	/**
	 * @return Returns true if backwards motion is desired.
	 */
	public boolean getBack()
	{
		return back;
	}

	/**
	 * @return Returns true if left sidestepping motion is desired.
	 */
	public boolean getLeft()
	{
		return left;
	}

	/**
	 * @return Returns true if right sidestepping motion is desired.
	 */
	public boolean getRight()
	{
		return right;
	}
	
	/** 
	 * Returns true if jumping is true
	 */
	public boolean getJump(){
		boolean tempjump = jump;
		jump = false;
		return tempjump;
	}

	/**
	 * Gets the amount of rotation desired on the horizontal plane.
	 * @return The horizontal rotation.
	 */
	public int getdX()
	{
		return dX;
	}

	/**
	 * Gets the amount of rotation desired on the vertical plane.
	 * @return The vertical rotation.
	 */
	public int getdY()
	{
		return dY;
	}
	/**
	 * Gets the X position of the mouse in the screen.
	 * @return X position of the mouse
	 */
	public int getMouseX() {
		return mouseX;
	}
	/**
	 * Gets the Y position of the mouse in the screen.
	 * @return Y position of the mouse
	 */
	public int getMouseY() {
		return mouseY;
	}
	/**
	 * Gets which mouse button was clicked the last, since the last time this function was called.
	 * @return	The mouse button number: 1 = left, 2 = middle, 3 = right and 0 = none.
	 */
	public byte getClicked()
	{
		byte clicked = mouseClicked;
		mouseClicked = 0;
		return clicked;
	}
	
	/**
	 * Gets the amount of notches the mouse wheel has turned since the last time this function was called.
	 * @return	The amount of notches the mouse wheel turned
	 */
	public int getNotches() {
		int res = notches;
		notches = 0;
		return res;
	}
	
	public boolean isRightButtonDragged() {
		return rightButtonDragged;
	}
	
	public boolean isLeftButtonDragged() {
		return leftButtonDragged;
	}
	
	public byte getMouseReleased(){
		byte res = mouseReleased;
		mouseReleased = 0;
		return res;
	}
	
	public boolean isLeftButtonPressed(){
		boolean res = leftButtonPressed;
		leftButtonPressed = false;
		return res;
	}

	/**
	 * Updates the fields of the Control class to represent the
	 * most up-to-date values. 
	 */
	public abstract void update();
}
