package GameObjects;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.vecmath.Vector3d;


import EditorModes.*;

import LevelHandling.Level;
import LevelHandling.Maze;

import Listening.Control;
import MazeObjects.CustomMazeObject;

/**
 * the editor class is used to give the editor mode it functionality some of the methods in this class are described
 * below. 
 *
 */
public class Editor extends GameObject{

	private EditMode editMode;

	private float FOV;

	private Control control; 

	private double horAngle, verAngle;

	private int selectedX, selectedZ;

	int angle;

	private Level level;

	public Editor(Vector3d pos, double h, double v)
	{
		super(pos);
		horAngle = h;
		verAngle = v;
		editMode = new AddStatic(level, ObjectMode.ADD_FLOOR);
	}

	/**
	 * Sets some variables needed to use the editor. Control and field of view cannot be altered outside of this 
	 * method, because they do not need to be changed after initialization.
	 * @param level		Initial Level of that is loaded into the editor
	 * @param FOV		Field of view of the camera: used to calculate the cursor position.
	 * @param control	User input used to control the editor
	 */

	public void initSet(Level level, float FOV, Control control)
	{
		this.level = level;
		this.FOV = FOV;
		this.control = control;
		editMode = new AddStatic(level, ObjectMode.ADD_FLOOR);
	}

	/**
	 * Sets the maze
	 * @param maze	Maze
	 */

	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Returns the horizontal angle of the orientation.
	 * @return the horAngle
	 */
	public double getHorAngle() {
		return horAngle;
	}


	/**
	 * Returns the vertical angle of the orientation.
	 * @return the verAngle
	 */
	public double getVerAngle() {
		return verAngle;
	}
	
	/**
	 * Returns the level that is being edited
	 * @return	the edited level
	 */

	public Level getLevel(){
		return level;
	}
	
	/**
	 * Sets the mode of the editor to a certain mode.
	 * @param editMode	new edit mode
	 */

	public void setEditMode(EditMode editMode)
	{
		this.editMode = editMode;
	}

	/**
	 * Updates the editor according to the inputs and current mode of the editor.
	 * <p>
	 * In order, these actions are taken: 
	 * <ol>
	 * <li> The new camera position is calculated. The camera can be moved by dragging the right mouse button 
	 * and by rotating the mouse wheel.
	 * <li> The new Cursor position is calculated: The cursor highlights the top element of the maze stack over 
	 * which the mouse is currently hovering.
	 * <li> Actions are taken if the mouse is clicked, dragged or released according to the current edit mode.
	 * </ol>
	 * @param screenWidth	Width of the current window
	 * @param screenHeight	Height of the current window
	 */

	public void update(int screenWidth, int screenHeight)
	{
		int notches = control.getNotches();
		// The Y position can never be lower then the highest wall
		if(location.y + notches > Maze.SQUARE_SIZE)
			location.y += notches;
		// When dragging the right mouse button, the camera is moved.
		if(control.isRightButtonDragged()){
			updateLocation(screenHeight);
		}
		else
		{
			updateCursor(screenHeight, screenWidth);
			if(control.isLeftButtonPressed())
				editMode.mousePressed(selectedX, selectedZ);
			else if(control.getMouseReleased() == 1){
				editMode.mouseReleased();
			}
			else if(control.isLeftButtonDragged()){
				editMode.mouseDragged(selectedX, selectedZ);
			}
			else{
				level.getMaze().clearSelected();
				level.getMaze().select(selectedX, selectedZ);
			}
		}
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
		double pixelsPerUnit = (screenHeight/2) / (halfTan * location.y); 

		// cursor position in ogl coordinates:
		double cursorPositionX = (control.getMouseX() - screenWidth / 2) / pixelsPerUnit + location.x;
		double cursorPositionZ = (control.getMouseY() - screenHeight / 2) / pixelsPerUnit + location.z;

		// cursor position in maze coordinates:
		selectedX = (int)(cursorPositionX / Maze.SQUARE_SIZE);
		selectedZ = (int)(cursorPositionZ / Maze.SQUARE_SIZE);
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
		double pixelsPerUnit = (screenHeight/2) / (halfTan * location.y); 

		// camera position in ogl coordinates:
		location.x = location.x - control.getdX() / pixelsPerUnit;
		location.z = location.z - control.getdY() / pixelsPerUnit;
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
	
	/**
	 * Reads in a custom maze object from an .obj file in the object folder.
	 * @return	The Custom maze that was read in.
	 */

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
