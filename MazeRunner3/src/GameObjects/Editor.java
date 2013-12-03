package GameObjects;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.vecmath.Vector3d;

import Drawing.EditBoxManager;
import Listening.Control;
import MainGame.Maze;
import MazeObjects.CustomMazeObject;
import MazeObjects.MazeObject;


public class Editor extends GameObject{

	private float FOV;

	private Control control; 
	private EditBoxManager editBoxManager;

	private double horAngle, verAngle;

	private int selectedX, selectedZ;

	private int pressedX, pressedY;
	int angle;

	private Maze maze;
	/*
	private static float buttonSize;
	private final int numButtons = 11;*/

	private byte drawMode;

	public Editor(Vector3d pos, double h, double v)
	{
		super(pos);
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

	public void setEditBoxManager(EditBoxManager ebm)
	{
		editBoxManager = ebm;
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

	public void setDrawMode(byte drawMode)
	{
		this.drawMode = drawMode;
	}

	/**
	 * Updates the editor according to the inputs and current state of the editor
	 * @param screenWidth	Width of the current window
	 * @param screenHeight	Height of the current window
	 */

	public void update(int screenWidth, int screenHeight)
	{
		double pos[] = new double[3];
		location.get(pos);
		int notches = control.getNotches();
		// The Y position can never be lower then the highest wall
		if(pos[1] + notches > maze.SQUARE_SIZE)
			pos[1] += notches;
		// Store the position where the left button was originally pressed
		// This information is used while rotating slope that are to be placed. 
		if(control.isLeftButtonPressed())
		{
			pressedX = control.getMouseX();
			pressedY = control.getMouseY();
		}
		// When dragging the right mouse button, the camera is moved.
		if(control.isRightButtonDragged()){

			updateLocation(screenHeight, pos);
		}
		// When a selection of squares made by dragging is released, the selected squares are toggled
		else if(control.getMouseReleased() == 1 && !editBoxManager.isHoovering()){
			maze.addBlock(drawMode, angle);
		}
		else
		{
			updateCursor(screenHeight, screenWidth, pos);
			// When dragging, the selected squares are remembered:
			if(!control.isLeftButtonDragged()){
				maze.clearSelected();
				// highlight the selection:
				maze.select(selectedX, selectedZ);
			}
			else if(!editBoxManager.isHoovering() && drawMode < 4)
			{
				maze.select(selectedX, selectedZ);
			}
			else if(!editBoxManager.isHoovering())
			{
				// If the element to be added is rotatable: find the correct orientation.
				int dX = control.getMouseX() - pressedX;
				int dY = control.getMouseY() - pressedY;
				if(Math.abs(dX) > Math.abs(dY) )
				{
					angle = dX > 0 ? 90 : 270;
				}
				else
				{
					angle = dY > 0 ? 180 : 0;
				}
				maze.addBlock(drawMode, angle);
			}
		}
		location.set(pos);
	}

	/**
	 * Calculates at which position in the maze the mouse is currently pointing, and highlights that position.
	 * When the mouse points at a position outside the maze, nothing is highlighted.
	 * @param screenHeight	Height of the current window
	 * @param screenWidth	Width of the current window
	 */

	private void updateCursor(int screenHeight, int screenWidth, double[] pos)
	{
		// The field of view relates to the portion of the map that is visible:
		double halfTan = Math.tan(Math.toRadians(FOV/2));
		double pixelsPerUnit = (screenHeight/2) / (halfTan * pos[1]); 

		// cursor position in ogl coordinates:
		double cursorPositionX = (control.getMouseX() - screenWidth / 2) / pixelsPerUnit + pos[0];
		double cursorPositionZ = (control.getMouseY() - screenHeight / 2) / pixelsPerUnit + pos[2];

		// cursor position in maze coordinates:
		selectedX = (int)(cursorPositionX / maze.SQUARE_SIZE);
		selectedZ = (int)(cursorPositionZ / maze.SQUARE_SIZE);
	}

	/**
	 * Update the x and z location of the editor according to the amount the mouse is moved (while the right
	 * mouse button is dragged).
	 * @param screenHeight	Height of the current window.
	 */

	private void updateLocation(int screenHeight, double[] pos)
	{
		// The field of view relates to the portion of the map that is visible:
		double halfTan = Math.tan(Math.toRadians(FOV/2));
		double pixelsPerUnit = (screenHeight/2) / (halfTan * pos[1]); 

		// camera position in ogl coordinates:
		pos[0]= pos[0] - control.getdX() / pixelsPerUnit;
		pos[2] = pos[2] - control.getdY() / pixelsPerUnit;
	}
	
	public void addObject(MazeObject obj)
	{
		maze.add(selectedX, selectedZ, obj);
	}

	/**
	 * Initialize the JFileChooser to be used for maze files. Standard file locations can be selected.
	 * @param fc	JFileChooser to be initialized.
	 */

	private static void selectDirectory(JFileChooser fc, String fileType, String fileExtension)
	{
		String[] myFileLocations = new String[]{ //TODO: Hard-coded file locations. Remove eventually.
				"C:\\Users\\Victor\\Documents\\MATLAB\\Computational Intelligence\\Assignment3",
		"C:\\Users\\Tom2\\Documents\\Java\\workspace\\MazeRunner2\\levels"};

		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Select a maze file");
		FileFilter filter = new FileNameExtensionFilter(fileType, fileExtension);
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
		selectDirectory(fc, "Maze files", "mz");

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
		selectDirectory(fc, "Maze files", "mz");

		int returnVal = fc.showDialog(fc, "Open");
		File file = fc.getSelectedFile();
		Maze maze = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			maze = Maze.read(file);
		}

		return maze;
	}
	
	public static MazeObject readMazeObject(){
		JFileChooser fc = new JFileChooser();
		selectDirectory(fc, "Maze Objects", "obj");

		int returnVal = fc.showDialog(fc, "Open");
		File file = fc.getSelectedFile();
		MazeObject custom = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			custom = CustomMazeObject.readFromOBJ(file);
		}

		return custom;
	}
}
