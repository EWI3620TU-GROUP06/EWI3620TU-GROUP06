package MainGame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Editor extends GameObject{

	private final String[] myFileLocations = new String[]{ //TODO: Hard-coded file locations. Remove eventually.
			"C:\\Users\\Victor\\Documents\\MATLAB\\Computational Intelligence\\Assignment3",
	"C:\\Users\\Tom2\\Documents\\Java\\workspace\\MazeRunner3\\levels"};

	private JFileChooser fc;

	private float FOV;

	private Control control; 

	private double horAngle, verAngle;

	private int selectedX, selectedZ;

	private Maze maze;

	private float buttonSize;
	private final int numButtons = 11;
	
	private byte drawMode;
	
	private final byte DRAW_EMPTY = 0;
	private final byte DRAW_BOX = 1;
	private final byte DRAW_START = 2;
	private final byte DRAW_FINISH = 3;
	private final byte DRAW_HIGH_RAMP = 4;
	private final byte DRAW_LOW_RAMP = 5;
	private final byte DRAW_FLAT_BOX = 6;

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

	public void update(int screenWidth, int screenHeight)
	{
		if(control != null)
		{
			control.update();

			int notches = control.getNotches();
			// The Y position can never be lower then the highest wall
			if(locationY + notches > maze.SQUARE_SIZE)
				locationY += notches;
			
			// When dragging the left mouse button, the camera is moved.
			if(control.isRightButtonDragged()){
				updateLocation(screenHeight);
			}
			// When a selection of squares made by dragging is released, the selected squares are toggled
			else if(control.isLeftReleased()){
				maze.addBlock(drawMode);
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
					if(!(drawMode == DRAW_START || drawMode == DRAW_FINISH || drawMode == DRAW_HIGH_RAMP || drawMode == DRAW_LOW_RAMP))
					{
						maze.select(selectedX, selectedZ);
					}
				}
				

				int button = getButtons();
				switch(button){
				case(0): maze.addToSize(1); break;
				case(1): maze.addToSize(-1); break;
				case(2): drawMode = DRAW_EMPTY; break;
				case(3): drawMode = DRAW_BOX; break;
				case(4): drawMode = DRAW_START; break;
				case(5): drawMode = DRAW_FINISH; break;
				case(6): drawMode = DRAW_HIGH_RAMP; break;
				case(7): drawMode = DRAW_LOW_RAMP; break;
				case(8): drawMode = DRAW_FLAT_BOX; break;
				case(9): save(); break;
				case(10): read(); break;
				case(-1): maze.addBlock(drawMode); break;
				case(-2): System.out.println("Clicked middle mouse button!"); break;
				default: break;
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
	}

	private void updateLocation(int screenHeight)
	{
		// The field of view relates to the portion of the map that is visible:
		double halfTan = Math.tan(Math.toRadians(FOV/2));
		double pixelsPerUnit = (screenHeight/2) / (halfTan * locationY); 

		// camera position in ogl coordinates:
		locationX = locationX - control.getdX() / pixelsPerUnit;
		locationZ = locationZ - control.getdY() / pixelsPerUnit;
	}

	private void selectDirectory()
	{
		fc = new JFileChooser();
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

	public boolean save(){
		int returnVal = fc.showDialog(fc, "Save");
		File file = fc.getSelectedFile();
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			maze.save(file);
			return true;
		}
		return false;
	}

	public boolean read(){
		int returnVal = fc.showDialog(fc, "Open");
		File file = fc.getSelectedFile();
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			maze.read(file);
			return true;
		}
		return false;
	}
	
	public static Maze readMaze(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Select a maze file");
		FileFilter filter = new FileNameExtensionFilter("Maze file", "mz", "maze");
		fc.setFileFilter(filter);
		
		int returnVal = fc.showDialog(fc, "Open");
		File file = fc.getSelectedFile();
		Maze maze = new Maze();
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			maze.read(file);
		}
		return maze;
	}
}
