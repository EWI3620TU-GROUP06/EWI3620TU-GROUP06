package MainGame;
import java.io.File;

import javax.swing.JFileChooser;


public class Editor extends GameObject{
	
	private final String[] myFileLocations = new String[]{ //TODO: Hard-coded file locations. Remove eventually.
			"C:\\Users\\Victor\\Documents\\MATLAB\\Computational Intelligence\\Assignment3",
			"C:\\Users\\Kevin\\Dropbox\\TUDelft\\TI2735 - Computational Intelligence\\Assignment 3",
			"C:\\Users\\Kevin van As\\Dropbox\\TUDelft\\TI2735 - Computational Intelligence\\Assignment 3"};
	
	private JFileChooser fc;
	
	private float FOV;

	private Control control; 

	private double horAngle, verAngle;

	private int selectedX, selectedZ;
	
	private double dragSpeed = 0.0225;

	private Maze maze;

	private float buttonSize;
	private int numButtons = 4;

	public Editor(double x, double y, double z, double h, double v)
	{
		super(x, y, z);
		horAngle = h;
		verAngle = v;
		selectDirectory();
	}

	public void setFOV(float FOV)
	{
		this.FOV = FOV;
	}

	public void setButtonSize(float buttonSize)
	{
		this.buttonSize = buttonSize;
	}

	public void setMaze(Maze maze) {
		this.maze = maze;
	}

	/**
	 * Sets the Control object that will control the player's motion
	 * <p>
	 * The control must be set if the object should be moved.
	 * @param input
	 */
	public void setControl(Control control)
	{
		this.control = control;
	}

	/**
	 * Returns the horizontal angle of the orientation.
	 * @return the horAngle
	 */
	public double getHorAngle() {
		return horAngle;
	}

	/**
	 * Sets the horizontal angle of the orientation.
	 * @param horAngle the horAngle to set
	 */
	public void setHorAngle(double horAngle) {
		this.horAngle = horAngle;
	}

	/**
	 * Returns the vertical angle of the orientation.
	 * @return the verAngle
	 */
	public double getVerAngle() {
		return verAngle;
	}

	/**
	 * Sets the vertical angle of the orientation.
	 * @param verAngle the verAngle to set
	 */
	public void setVerAngle(double verAngle) {
		this.verAngle = verAngle;
	}
	
	public Maze getMaze()
	{
		return maze;
	}

	public void update(int screenWidth, int screenHeight, int deltaTime)
	{
		if(control != null)
		{
			control.update();

			int notches = control.getNotches();
			// The Y position can never be lower then the highest wall
			if(locationY + notches > 2*maze.SQUARE_SIZE)
				locationY += notches;
			
			if(control.isRightButtonDragged())
			{
				locationX -= control.getdX()*deltaTime*dragSpeed;
				locationZ -= control.getdY()*deltaTime*dragSpeed * screenHeight / screenWidth;
			}
			else
			{
				updateCursor(screenHeight, screenWidth);
				
				int button = getButtons();
				if(button == 0)
				{
					maze.addToSize(1);
				}
				if(button == 1)
				{
					maze.addToSize(-1);
				}
				if(button == 2)
				{
					if(save())
						System.out.println("Maze saved!");
				}
				if(button == 3)
				{
					maze = read();
				}
				if(button == -1)
				{
					maze.toggleSelected();
				}
				if(button == -2)
				{
					System.out.println("Clicked middle mouse button!");
				}
			}
		}
	}

	/**
	 * Calculates whether a button in the screen or a mouse button was pressed.
	 * @return	A value between 0 and numOfButtons - 1 is returned when a button is pressed,
	 *  -1, -2 or -3 is returned when mouse button left, middle or right respectively is clicked anywhere else.
	 *  numOfButtons is returned when the mouse is not clicked.
	 */

	private int getButtons()
	{
		byte mouseButton = control.getClicked();
		if(mouseButton > 0)
		{
			for(int i = 0; i < numButtons; i++)
			{
				// check for every button if it is clicked on.
				if( control.getMouseX() > (i * buttonSize) && control.getMouseX() < ((i + 1) * buttonSize) && control.getMouseY() < buttonSize)
					return i;
			}
			return -mouseButton;	// clicked somewhere else on the screen 
		}
		return numButtons; 
	}

	/**
	 * Calculates at which position in the maze the mouse is currently pointing, and highlights that position.
	 * When the mouse points at a position outside the maze, nothing is highlighted.
	 * @param screenHeight	Height of the current window
	 * @param screenWidth	Width of the current window
	 */

	private void updateCursor(int screenHeight, int screenWidth)
	{
		// The field of view relates to the portion of the map that is visible:
		double halfTan = Math.tan(Math.toRadians(FOV/2));
		double pixelsPerUnit = (screenHeight/2) / (halfTan * locationY); 

		// cursor position in ogl coordinates:
		double cursorPositionX = (control.getMouseX() - screenWidth / 2) / pixelsPerUnit + locationX;
		double cursorPositionZ = (control.getMouseY() - screenHeight / 2) / pixelsPerUnit + locationZ;

		// cursor position in maze coordinates:
		selectedX = (int)(cursorPositionX / maze.SQUARE_SIZE);
		selectedZ = (int)(cursorPositionZ / maze.SQUARE_SIZE);

		// highlight the selection:
		maze.select(selectedX, selectedZ);
	}
	
	private void selectDirectory()
	{
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle("Select a maze file");

		File myMainDir;
		for(int i = 0; i<myFileLocations.length; i++){
			myMainDir = new File(myFileLocations[i]);
			if(myMainDir.exists()){
				fc.setCurrentDirectory(myMainDir);
				break;
			}
		}
	}
	
	public boolean save(){
		int returnVal = fc.showSaveDialog(null);
		File file = fc.getSelectedFile();		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            maze.save(file);
            return true;
        }else{
        	return false;
        }
	}
	
	public Maze read(){
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
           return Maze.read(fc.getSelectedFile());
        }
		return null;
	}
}
