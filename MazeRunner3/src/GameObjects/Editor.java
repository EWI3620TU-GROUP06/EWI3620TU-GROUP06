package GameObjects;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import Listening.Control;
import MainGame.Maze;
import MazeObjects.CustomMazeObject;
import MazeObjects.MazeObject;


public class Editor extends GameObject{

	private float FOV;

	private Control control; 

	private double horAngle, verAngle;

	private int selectedX, selectedZ;

	private int pressedX, pressedY;
	int angle;

	private Maze maze;

	private static float buttonSize;
	private final int numButtons = 11;

	private byte drawMode;

	private final byte DRAW_EMPTY = 0;
	private final byte DRAW_BOX = 1;
	private final byte DRAW_FLAT_BOX = 2;
	private final byte DRAW_START = 3;
	private final byte DRAW_FINISH = 4;
	private final byte DRAW_HIGH_RAMP = 5;
	private final byte DRAW_LOW_RAMP = 6;


	public Editor(double x, double y, double z, double h, double v)
	{
		super(x, y, z);
		horAngle = h;
		verAngle = v;
	}

	/**
	 * Sets the Field of View of the editor
	 * @param FOV	Field of View
	 */

	public void setFOV(float FOV)
	{
		this.FOV = FOV;
	}

	/**
	 * Sets the size of the buttons in the heads up display
	 * @param buttonSize	Size of the buttons
	 */

	public static void setButtonSize(float size)
	{
		buttonSize = size;
	}

	/**
	 * Sets the maze
	 * @param maze	Maze
	 */

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
	/**
	 * Gets the maze
	 * @return	Maze
	 */

	public Maze getMaze(){
		return maze;
	}

	/**
	 * Updates the editor according to the inputs and current state of the editor
	 * @param screenWidth	Width of the current window
	 * @param screenHeight	Height of the current window
	 */

	public void update(int screenWidth, int screenHeight)
	{
		if(control != null)
		{
			control.update();

			int notches = control.getNotches();
			// The Y position can never be lower then the highest wall
			if(locationY + notches > maze.SQUARE_SIZE)
				locationY += notches;
			// Store the position where the left button was originally pressed
			// This information is used while rotating slope that are to be placed. 
			if(control.isLeftButtonPressed())
			{
				pressedX = control.getMouseX();
				pressedY = control.getMouseY();
			}
			// When dragging the right mouse button, the camera is moved.
			if(control.isRightButtonDragged()){

				updateLocation(screenHeight);
			}
			// When a selection of squares made by dragging is released, the selected squares are toggled
			else if(control.isLeftReleased() && ! hooverButtons()){
				maze.addBlock(drawMode, angle);
			}
			else
			{
				updateCursor(screenHeight, screenWidth);
				// When dragging, the selected squares are remembered:
				if(!control.isLeftButtonDragged()){
					maze.clearSelected();
					// highlight the selection:
					maze.select(selectedX, selectedZ);
				}
				else{
					if(!hooverButtons())
					{
						// If the left button is dragged and the elements to be drawn non-rotatable and the mouse 
						// isn't located over the buttons: keep selecting more elements
						if(drawMode == DRAW_BOX || drawMode == DRAW_EMPTY || drawMode == DRAW_FLAT_BOX)
						{
							maze.select(selectedX, selectedZ);
						}
						else 
						{
							// If the element to be added is rotatable: find the correct orientation.
							int dX = control.getMouseX() - pressedX;
							int dY = control.getMouseY() - pressedY;
							if(Math.abs(dX) > Math.abs(dY) )
							{
								if(dX > 0)
									angle = 90;
								else
									angle = 270;
							}
							else
							{
								if(dY > 0)
									angle = 180;
								else
									angle = 0;
							}
							maze.addBlock(drawMode, angle);
						}
					}
				}
				// Take actions according to the pressed button.
				int button = getButtons();
				switch(button){
				case(0): maze.addToSize(1); break;
				case(1): maze.addToSize(-1); break;
				case(2):
					MazeObject customObject = CustomMazeObject.readFromOBJ("Eerste test.obj");
					maze.add(1, 1, customObject);
					break;//drawMode = DRAW_EMPTY; break;
				case(3): drawMode = DRAW_BOX; break;
				case(4): drawMode = DRAW_START; break;
				case(5): drawMode = DRAW_FINISH; break;
				case(6): drawMode = DRAW_HIGH_RAMP; break;
				case(7): drawMode = DRAW_LOW_RAMP; break;
				case(8): drawMode = DRAW_FLAT_BOX; break;
				case(9): save(); break;
				case(10): Maze maze_temp = readMaze(); 
				if(maze_temp != null)
					maze = maze_temp; break;

				case(-1): maze.addBlock(drawMode, angle);break;
				case(-2): maze.rotateSelected(); break;
				default: break;
				}

			}
		}
	}

	/**
	 * Checks whether the mouse is currently over one of the Heads Up Display buttons.
	 * @return	boolean that tells if the mouse is located over a button
	 */

	private boolean hooverButtons()
	{
		return control.getMouseX() < (numButtons * buttonSize) && control.getMouseY() < buttonSize;
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
	}

	/**
	 * Update the x and z location of the editor according to the amount the mouse is moved (while the right
	 * mouse button is dragged).
	 * @param screenHeight	Height of the current window.
	 */

	private void updateLocation(int screenHeight)
	{
		// The field of view relates to the portion of the map that is visible:
		double halfTan = Math.tan(Math.toRadians(FOV/2));
		double pixelsPerUnit = (screenHeight/2) / (halfTan * locationY); 

		// camera position in ogl coordinates:
		locationX = locationX - control.getdX() / pixelsPerUnit;
		locationZ = locationZ - control.getdY() / pixelsPerUnit;
	}

	/**
	 * Initialize the JFileChooser to be used for maze files. Standard file locations can be selected.
	 * @param fc	JFileChooser to be initialized.
	 */

	private static void selectDirectory(JFileChooser fc)
	{
		String[] myFileLocations = new String[]{ //TODO: Hard-coded file locations. Remove eventually.
				"C:\\Users\\Victor\\Documents\\MATLAB\\Computational Intelligence\\Assignment3",
		"C:\\Users\\Tom2\\Documents\\Java\\workspace\\MazeRunner2\\levels"};

		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Select a maze file");
		FileFilter filter = new FileNameExtensionFilter("Maze file", "mz", "maze");
		fc.setFileFilter(filter);
		File myMainDir;
		for(int i = 0; i < myFileLocations.length; i++){
			myMainDir = new File(myFileLocations[i]);
			if(myMainDir.exists()){
				fc.setCurrentDirectory(myMainDir);
				break;
			}
		}
	}

	/**
	 * Selects a file with a JFileChooser and save the maze to this file.
	 * @return	boolean the represents whether the save was successfull.
	 */

	public boolean save(){
		JFileChooser fc = new JFileChooser();
		selectDirectory(fc);

		int returnVal = fc.showDialog(fc, "Save");
		File file = fc.getSelectedFile();
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			maze.save(file);
			return true;
		}
		return false;
	}

	/**
	 * Selects a file with a JFileChooser and reads the maze from this file. If the file selection is cancelled
	 * or otherwise interrupted, null is returned.
	 * @return	Maze that was read from the chosen file.
	 */

	public static Maze readMaze(){
		JFileChooser fc = new JFileChooser();
		selectDirectory(fc);

		int returnVal = fc.showDialog(fc, "Open");
		File file = fc.getSelectedFile();
		Maze maze = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			maze = Maze.read(file);
		}

		return maze;
	}
}
