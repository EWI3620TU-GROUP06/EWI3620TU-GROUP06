package GameObjects;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.vecmath.Vector3d;

import Drawing.EditBoxManager;
import LevelHandling.Level;
import LevelHandling.Maze;
import Listening.Control;
import MazeObjects.CustomMazeObject;

public class Editor extends GameObject{

	public static final byte DRAW_EMPTY = 0;
	public static final byte DRAW_BOX = 1;
	public static final byte DRAW_LOW_BOX = 2;
	public static final byte DRAW_START = 3;
	public static final byte DRAW_FINISH = 4;
	public static final byte DRAW_RAMP = 5;
	public static final byte DRAW_LOW_RAMP = 6;
	
	private float FOV;

	private Control control; 
	private EditBoxManager editBoxManager;

	private double horAngle, verAngle;

	private int selectedX, selectedZ;

	private int pressedX, pressedY;
	int angle;

	private Level level;
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

	public void setLevel(Level level) {
		this.level = level;
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

	public Level getLevel(){
		return level;
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
		if(pos[1] + notches > level.getMaze().SQUARE_SIZE)
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
			level.getMaze().addBlock(drawMode, angle);
		}
		else
		{
			updateCursor(screenHeight, screenWidth, pos);
			// When dragging, the selected squares are remembered:
			if(!control.isLeftButtonDragged()){
				level.getMaze().clearSelected();
				// highlight the selection:
				level.getMaze().select(selectedX, selectedZ);
			}
			else if(!editBoxManager.isHoovering() && drawMode < 4)
			{
				level.getMaze().select(selectedX, selectedZ);
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
				level.getMaze().addBlock(drawMode, angle);
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
		selectedX = (int)(cursorPositionX / level.getMaze().SQUARE_SIZE);
		selectedZ = (int)(cursorPositionZ / level.getMaze().SQUARE_SIZE);
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
	
	public void addObject(CustomMazeObject obj)
	{
		if(!Maze.customs.contains(obj)){
			Maze.customs.add(obj);
			drawMode = (byte)(6 + Maze.customs.size());
		}
		else
			drawMode = (byte)(6 + Maze.customs.indexOf(obj));
		
	}

	/**
	 * Initialize the JFileChooser to be used for maze files. Standard file locations can be selected.
	 * @param fc	JFileChooser to be initialized.
	 */

	private static void selectDirectory(JFileChooser fc, String fileType, String fileExtension, String fileName, String fileLocation)
	{
		String[] myFileLocations = new String[]{ "src\\" + fileLocation};

		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Select a " + fileName + " file");
		FileFilter filter = new FileNameExtensionFilter(fileType, fileExtension, fileName);
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
		selectDirectory(fc, "Maze files", "mz", "maze", "levels");

		int returnVal = fc.showDialog(fc, "Save");
		File file = fc.getSelectedFile();
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			level.saveLevel(file);
			return true;
		}
		return false;
	}

	/**
	 * Selects a file with a JFileChooser and reads the maze from this file. If the file selection is cancelled
	 * or otherwise interrupted, null is returned.
	 * @return	Maze that was read from the chosen file.
	 */

	public static Level readLevel(){
		JFileChooser fc = new JFileChooser();
		selectDirectory(fc, "Maze files", "mz", "maze", "levels");

		int returnVal = fc.showDialog(fc, "Open");
		File file = fc.getSelectedFile();
		Level level = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			level = Level.readLevel(file);
		}

		return level;
	}
	
	public static CustomMazeObject readMazeObject(){
		JFileChooser fc = new JFileChooser();
		selectDirectory(fc, "Maze Objects", "obj", "object", "objects");

		int returnVal = fc.showDialog(fc, "Open");
		File file = fc.getSelectedFile();
		CustomMazeObject custom = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			custom = CustomMazeObject.readFromOBJ(file);
		}

		return custom;
	}
}
