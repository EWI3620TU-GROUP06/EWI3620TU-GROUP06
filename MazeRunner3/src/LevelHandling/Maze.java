package LevelHandling;
import javax.media.opengl.GL;

import Drawing.DrawingUtil;
import Drawing.VisibleObject;
import EditorModes.ObjectMode;
import MazeObjects.Box;
import MazeObjects.CustomMazeObject;
import MazeObjects.FinishTile;
import MazeObjects.Floor;
import MazeObjects.MazeObject;
import MazeObjects.Ramp;
import MazeObjects.StartTile;

import com.sun.opengl.util.texture.Texture;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Maze represents the maze used by MazeRunner.
 * <p>
 * The Maze is defined as an array of integers, where 0 equals nothing and 1 equals a wall.
 * Note that the array is square and that MAZE_SIZE contains the exact length of one side.
 * This is to perform various checks to ensure that there will be no ArrayOutOfBounds 
 * exceptions and to perform the calculations needed by not only the display(GL) function, 
 * but also by functions in the MazeRunner class itself.<br />
 * Therefore it is of the utmost importance that MAZE_SIZE is correct.
 * <p>
 * SQUARE_SIZE is used by both MazeRunner and Maze itself for calculations of the 
 * display(GL) method and other functions. The larger this value, the larger the world of
 * MazeRunner will be.
 * <p>
 * This class implements VisibleObject to force the developer to implement the display(GL)
 * method, so the Maze can be displayed.
 * 
 * @author Bruno Scheele, revised by Mattijs Driel
 *
 */
public class Maze implements VisibleObject {

	public static int MAZE_SIZE_X = 10;
	public static int MAZE_SIZE_Z = 10;
	public static final int SQUARE_SIZE = 5;

	private boolean[][] selected = new boolean[MAZE_SIZE_X][MAZE_SIZE_Z];

	private MazeObject[][] maze = null;
	public static final ArrayList<MazeObject> standards = new ArrayList<MazeObject>(Arrays.asList(new MazeObject[]{
			new Floor(SQUARE_SIZE, 0, 0, 0),
			new Box(SQUARE_SIZE, SQUARE_SIZE, 0, 0, 0),
			new Box(SQUARE_SIZE, (float)SQUARE_SIZE/2, 0, 0, 0),
			new StartTile(SQUARE_SIZE, 0, 0, 0),
			new FinishTile(SQUARE_SIZE, 0, 0, 0),
			new Ramp(SQUARE_SIZE, SQUARE_SIZE, 0, 0, 0),
			new Ramp(SQUARE_SIZE, (float)SQUARE_SIZE / 2,  0, 0, 0)	
	}));


	public static ArrayList<CustomMazeObject> customs = new ArrayList<CustomMazeObject>();
	private int customSize = 0;

	public Maze()
	{
		maze = new MazeObject[MAZE_SIZE_X][MAZE_SIZE_Z];
		for(int i = 0; i < maze.length; i++)
			for(int j = 0; j < maze[0].length; j++)
				maze[i][j] = new Floor(SQUARE_SIZE, i * SQUARE_SIZE, 0, j*SQUARE_SIZE);
	}

	public Maze(int mazeSizeX, int mazeSizeZ, byte[][] object, byte[][] rotation)
	{
		MAZE_SIZE_X = mazeSizeX;
		MAZE_SIZE_Z = mazeSizeZ;
		maze = new MazeObject[MAZE_SIZE_X][MAZE_SIZE_Z];
		for(int i = 0; i < maze.length; i++){
			for(int j = 0; j < maze[0].length; j++){
				if(object[i][j] < 0)
					maze[i][j] = customs.get(-(object[i][j] + 1)).translate(SQUARE_SIZE * i, 0, SQUARE_SIZE * j);
				else
					maze[i][j] = standards.get(object[i][j]).translate(SQUARE_SIZE * i, 0, SQUARE_SIZE * j);
				maze[i][j].rotateVerticesX(rotation[i][j]%4*90, 0, (float)(j + 0.5f)*SQUARE_SIZE);
				maze[i][j].rotateVerticesY(rotation[i][j]/4*90, (float)(i + 0.5f)*SQUARE_SIZE, (float)(j + 0.5f)*SQUARE_SIZE);
			}
		}
		selected = new boolean[MAZE_SIZE_X][MAZE_SIZE_Z];
	}
	
	/**
	 * the method removeRedundantFaces removes all the non-visible faces from the levels. this is needed to
	 * remain a framerate around 60.
	 */
	
	public void removeRedundantFaces()
	{
		for(int i = 0; i < maze.length; i++){
			for(int j  = 0; j < maze[0].length; j++){
				if(i != maze[0].length - 1)
				{
					maze[i][j].removeRedunantFaces(maze[i + 1][j]);
				}
				if(j != maze.length - 1)
				{
					maze[i][j].removeRedunantFaces(maze[i][j + 1]);
				}
			}
		}
	}

	public boolean isFinish(double x, double z)
	{
		if(x >= 0 && x < MAZE_SIZE_X*SQUARE_SIZE && z >= 0 && z < MAZE_SIZE_Z*SQUARE_SIZE)
			return maze[(int)x / SQUARE_SIZE][(int)z / SQUARE_SIZE].equals(standards.get(ObjectMode.ADD_FINISH));
		return false;
	}

	/**
	 * Initialize the textures used by the maze.
	 * @param gl	instance of opengl.
	 */

	public static void initTextures(GL gl)
	{
		CustomMazeObject.clearTextures();
		Texture boxTexture = DrawingUtil.initTexture(gl, "wall");
		Texture floorTexture = DrawingUtil.initTexture(gl, "floor");
		Texture finishTexture = DrawingUtil.initTexture(gl, "finish");
		Texture startTexture = DrawingUtil.initTexture(gl, "start");

		Box.addTexture(boxTexture);
		Floor.addTexture(floorTexture);
		StartTile.addTexture(startTexture);
		Ramp.addTexture(boxTexture);
		FinishTile.addTexture(finishTexture);
	}

	/**
	 * Sets the 'selected' flag for the maze element with the given coordinate
	 * @param x	X coordinate of the element to be selected
	 * @param z	Z coordinate of the element to be selected
	 */

	public void select(int x, int z)
	{
		if(x >= 0 && x < MAZE_SIZE_X && z >= 0 && z < MAZE_SIZE_Z)
			selected[x][z] = true;
	}

	/**
	 * Returns the size of the maze in opengl units
	 * @return	the size of the maze in opengl units
	 */

	public double getSizeX()
	{
		return MAZE_SIZE_X * SQUARE_SIZE;
	}
	
	public double getSizeZ()
	{
		return  MAZE_SIZE_Z * SQUARE_SIZE;
	}

	/**
	 * Gets the start position in opengl units and the initiial orientation of the player in the maze.
	 * @return	double array containing 1: the x and 2: the z coordinate of the startposition and 3: the initial angle. 
	 */

	public double[] getStart()
	{
		double[] res= new double[4];
		for(int i = 0; i <  maze[0].length; i++){
			for(int j = 0; j < maze[0].length; j++)
			{
				if(maze[i][j].equals(standards.get(ObjectMode.ADD_START)))
				{
					res[0] = maze[i][j].getPos().x;
					res[1] = maze[i][j].getPos().y;
					res[2] = maze[i][j].getPos().z;
					res[3] = maze[i][j].getRotation()[1];
				}
			}
		}
		res[0] += 2.5;
		res[1] += 2.5;
		res[2] += 2.5;
		return res;
	}

	/**
	 * Changes the size of the maze with the given amount. The maze must alway contain at least one square.
	 * @param n	the amount by which the maze size needs to be changed. 
	 */

	public void setSize(int x, int z)
	{
		if(x > 0 && z > 0)
		{
			// Create new maze and selected arrays
			MAZE_SIZE_X = x;
			MAZE_SIZE_Z = z;
			MazeObject[][]newMaze = new MazeObject[MAZE_SIZE_X][MAZE_SIZE_Z];
			boolean[][]newSelected = new boolean[MAZE_SIZE_X][MAZE_SIZE_Z];

			for (int i = 0; i < newMaze.length; i++){
				for (int j = 0; j < newMaze[0].length; j++)
				{
					if(i < maze.length && j < maze[0].length)
						newMaze[i][j] = maze[i][j];
					else
						newMaze[i][j] = new Floor(SQUARE_SIZE, i * SQUARE_SIZE, 0, j*SQUARE_SIZE);
					newSelected[i][j] = false;
				}
			}

			maze = newMaze;
			selected = newSelected;
		}
	}

	/**
	 * Sets the 'selected' flag of all elements in the maze to false.
	 */

	public void clearSelected()
	{
		for( int i = 0; i < maze.length; i++ )
			for( int j = 0; j < maze[0].length; j++ )
				selected[i][j] = false;
	}

	public void selectedAll()
	{
		for( int i = 0; i < maze.length; i++ )
			for( int j = 0; j < maze[0].length; j++ )
				selected[i][j] = true;
	}

	public void removeBlocks(byte drawMode)
	{
		for(int i = 0; i < maze.length; i++){
			for (int j = 0; j < maze[0].length; j++){
				if(maze[i][j].equals(standards.get(drawMode))){
					maze[i][j] = standards.get(ObjectMode.ADD_FLOOR).translate(i * SQUARE_SIZE, 0, j * SQUARE_SIZE);
				}
			}
		}
	}

	/**
	 * Adds an element to the maze at the selected position(s). 
	 * @param drawMode	Which element needs to be added. See Editor for drawMode declarations.
	 */

	public void addBlock(byte drawMode, int rotation)
	{			
		for(int i = 0; i < maze.length; i++){
			for (int j = 0; j < maze[0].length; j++){
				if (selected[i][j] && !maze[i][j].equals(standards.get(ObjectMode.ADD_FINISH)) 
						&& !maze[i][j].equals(standards.get(ObjectMode.ADD_START))) {
					if(drawMode < 0)
						maze[i][j] = customs.get(-drawMode - 1).translate(i * SQUARE_SIZE, 0, j*SQUARE_SIZE);
					else
						maze[i][j] = standards.get(drawMode).translate(i * SQUARE_SIZE, 0, j*SQUARE_SIZE);
					maze[i][j].rotateVerticesY(rotation, (float)(i + 0.5f) * SQUARE_SIZE, (float)(j + 0.5f) * SQUARE_SIZE);
				}
			}
		}
	}

	/**
	 * Rotates the selected element(s) by ninety degrees, is the selected element(s) can  be rotated.
	 */

	public void rotateSelected(boolean x, boolean y, boolean z)
	{
		for(int i = 0; i < maze.length; i++){
			for (int j = 0; j < maze[0].length; j++){
				if (selected[i][j]){
					if(x)
						maze[i][j].rotateVerticesX(90, 2.5, (j + 0.5) * SQUARE_SIZE);
					if(y)
						maze[i][j].rotateVerticesY(90, (i + 0.5) * SQUARE_SIZE, (j + 0.5) * SQUARE_SIZE);
					if(z)
						maze[i][j].rotateVerticesZ(90, (i + 0.5) * SQUARE_SIZE, 2.5);
				}
			}
		}
	}

	/**
	 * Writes the maze to a file. First the size is written, then the start and finish positions and finally
	 * the maze data.
	 * @param file	File to be written.
	 */

	public boolean write(PrintWriter wr) {
		boolean start = false;
		boolean finish = false;

		for(int i = 0; i <  maze.length; i++){
			for(int j = 0; j < maze[0].length; j++)
			{
				if(maze[i][j].equals(standards.get(ObjectMode.ADD_START))){
					start = true;
				}
				if(maze[i][j].equals(standards.get(ObjectMode.ADD_FINISH))){
					finish = true;
				}
			}
		}
		if(!start){
			System.err.println("No start position defined");
		} else if(!finish){
			System.err.println("No finish position defined");
		}else{
			try {
				wr.write(MAZE_SIZE_X + " " + MAZE_SIZE_Z + "\n");
				for(CustomMazeObject custom: customs)
					wr.write(custom.getFile().getCanonicalPath() + "\n");
				wr.write(0 + "\n");
				for (int i = 0; i < maze.length; i++) {
					for (int j = 0; j < maze[0].length; j++) {
						if(customs.contains(maze[i][j]))
							wr.print(-1 - customs.indexOf(maze[i][j]));
						else
							wr.print(standards.indexOf(maze[i][j]));
						int[] rotation = maze[i][j].rotation;
						wr.print("," + (rotation[0]/90%4 + 4*(rotation[1]/90%4) + 16*(rotation[2]/90%4)));
						if (j < maze[0].length - 1)
							wr.print(" ");
						else
							wr.print("\n");
					}
				}
				wr.write("\n");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Reads maze from file. The syntax should be as the save method writes it: first the size, followd by start
	 * position (three elements) and finish position (two elements) and finilly the maze data, written in a matrix 
	 * with the size of the maze.
	 * @param file	File from which is to be read.
	 * @return		Maze that was read-in.
	 */

	public static Maze read(Scanner sc) {
		try {
			int mazeSizeX = sc.nextInt();
			int mazeSizeZ = sc.nextInt();
			customs = new ArrayList<CustomMazeObject>();
			sc.nextLine();
			while(!sc.hasNextByte()){
				String line = sc.nextLine();
				String[] splitLine = line.split("[\\\\]");
				int i = 0;
				while(i < splitLine.length && !splitLine[i].equals("src"))
					i++;
				String fileName = "src";
				i++;
				while (i < splitLine.length)
				{
					fileName += "\\" + splitLine[i];
					i++;
				}
				customs.add(CustomMazeObject.readFromOBJ(new File(fileName)));
			}
			sc.next();
			byte[][] objects = new byte[mazeSizeX][mazeSizeZ];
			byte[][] rotation = new byte[mazeSizeX][mazeSizeZ];
			for (int i = 0; i < objects.length; i++) {
				for (int j = 0; j < objects[0].length; j++) {
					String line = sc.next();
					String[] objectElements = line.split("[,]");
					objects[i][j] = Byte.parseByte(objectElements[0]);
					rotation[i][j] = Byte.parseByte(objectElements[1]);
				}
			}
			return new Maze(mazeSizeX, mazeSizeZ, objects, rotation);
		} catch (Exception e) {
			e.printStackTrace();
			return new Maze();
		}
	}

	/**
	 * Draws the maze.
	 * TODO: betere beschrijving.
	 */
	public void display(GL gl) {
		if(customSize != customs.size())
		{
			for(CustomMazeObject obj : customs){
				obj.setTexture(gl);
				for(int i = 0; i < maze.length; i++){
					for(int j = 0; j < maze[0].length; j++)
					{
						MazeObject object = maze[i][j];
						if(obj.equals(object)){
							CustomMazeObject that = (CustomMazeObject) object;
							that.setTexNum(obj.getTexNum());
						}
					}
				}
			}
		}
		customSize = customs.size();
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				//Define all colours and change them if the element is selected
				float selectedColour[] = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

				float notSelectedColour[] = new float[]{0.6f, 0.6f, 0.6f, 1.0f};
				if (selected[i][j]) {
					maze[i][j].draw(gl,  selectedColour);
				} else {
					maze[i][j].draw(gl,  notSelectedColour);
				}
			}
		}
	}

	public MazeObject get(int x, int z)
	{
		if(x >= 0 && x < MAZE_SIZE_X && z >= 0 && z < MAZE_SIZE_Z)
			return maze[x][z];
		return new Floor(0, 0, 0, 0);
	}

	public void set(MazeObject mazeObject)
	{
		for(int i = 0; i < maze.length; i++)
			for(int j = 0; j < maze[0].length; j++)
				if(selected[i][j])
					maze[i][j] = mazeObject;
	}

	public MazeObject[] getNeighbourTiles(int x, int z){
		MazeObject[] res = new MazeObject[4];
		// The lines below work for the tile relative
		// to the middle tile according to the comment behind it
		// but this assumes that x is vertical in the maze-array and z is horizontal
		res[0] = get((int)x-1, (int)z); // up
		res[1] = get((int)x, (int)z-1); // left
		res[2] = get((int)x+1, (int)z); // down
		res[3] = get((int)x, (int)z+1); // right
		return res;
	}

}
