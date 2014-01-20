package LevelHandling;
import javax.media.opengl.GL;

import Drawing.DrawingUtil;
import Drawing.ErrorMessage;
import Drawing.VisibleObject;
import EditorModes.ObjectMode;
import MazeObjects.*;

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

	private int[][] selected = new int[MAZE_SIZE_X][MAZE_SIZE_Z];

	private MazeStack[][] maze = null;
	public static final ArrayList<MazeObject> standards = new ArrayList<MazeObject>(Arrays.asList(new MazeObject[]{
			new Floor(SQUARE_SIZE, 0, 0, 0),
			new Box(SQUARE_SIZE, SQUARE_SIZE, 0, 0, 0),
			new Box(SQUARE_SIZE, (float)SQUARE_SIZE/2, 0, 0, 0),
			new StartTile(SQUARE_SIZE, 0, 0, 0),
			new FinishTile(SQUARE_SIZE, 0, 0, 0),
			new Ramp(SQUARE_SIZE, SQUARE_SIZE, 0, 0, 0),
			new Ramp(SQUARE_SIZE, (float)SQUARE_SIZE / 2,  0, 0, 0),
			new Pit(SQUARE_SIZE, 20, 0, -20, 0),
			new Bottom(SQUARE_SIZE, 0, -20, 0),
			new Empty(SQUARE_SIZE, 0, 0, 0)
	}));


	public static ArrayList<CustomMazeObject> customs = new ArrayList<CustomMazeObject>();
	private int customSize = 0;

	public Maze()
	{
		maze = new MazeStack[MAZE_SIZE_X][MAZE_SIZE_Z];
		for(int i = 0; i < maze.length; i++)
			for(int j = 0; j < maze[0].length; j++)
				maze[i][j] = MazeStack.standard(i * SQUARE_SIZE, j * SQUARE_SIZE);
	}

	public Maze(int mazeSizeX, int mazeSizeZ)
	{
		MAZE_SIZE_X = mazeSizeX;
		MAZE_SIZE_Z = mazeSizeZ;
		maze = new MazeStack[MAZE_SIZE_X][MAZE_SIZE_Z];
		for(int i = 0; i < maze.length; i++)
			for(int j = 0; j < maze[0].length; j++)
				maze[i][j] = new MazeStack(i * SQUARE_SIZE, j*SQUARE_SIZE);
		selected = new int[MAZE_SIZE_X][MAZE_SIZE_Z];
	}

	private boolean isInBounds(int x, int z)
	{
		return x >= 0 && x < MAZE_SIZE_X && z >= 0 && z < MAZE_SIZE_Z;
	}

	/**
	 * the method removeRedundantFaces removes all the non-visible faces from the levels. this is needed to
	 * increase the performance of the game: otherwise a lot of redundant face are drawn and added to the 
	 * physics world.
	 */
	public void removeRedundantFaces()
	{
		for(int i = 0; i < maze.length; i++){
			for(int j  = 0; j < maze[0].length; j++){
				if(i < maze[0].length - 1)
				{
					maze[i][j].removeRedundantFaces(maze[i + 1][j]);
				}
				if(j < maze[0].length - 1)
				{
					maze[i][j].removeRedundantFaces(maze[i][j + 1]);
				}
			}
		}
	}

	/**
	 * Checks whether the given position is inside the bounds of the Finish tile
	 * @param x	x coordinate of the player location
	 * @param y	y coordinate of the player location
	 * @param z	z coordinate of the player location
	 * @return	boolean that states whether the given position is the finish position.
	 */

	public boolean isFinish(double x, double y, double z)
	{
		int intX = (int)x / SQUARE_SIZE;
		int intZ = (int)z / SQUARE_SIZE;
		if(isInBounds(intX, intZ)){
			MazeObject finishTile = null;
			if((finishTile = maze[intX][intZ].getInstanceOf(standards.get(ObjectMode.ADD_FINISH))) != null){
				return y > finishTile.getYMin()- 1 && y < finishTile.getYMin();
			}
		}
		return false;
	}

	/**
	 * Initialize the textures used by the maze.
	 * @param gl	instance of opengl.
	 */

	public void initTextures(GL gl)
	{
		CustomMazeObject.clearTextures();
		Texture boxTexture = DrawingUtil.initTexture(gl, "wall");
		Texture floorTexture = DrawingUtil.initTexture(gl, "floor");
		Texture finishTexture = DrawingUtil.initTexture(gl, "finish");
		Texture startTexture = DrawingUtil.initTexture(gl, "start");
		Texture pitTexture = DrawingUtil.initTexture(gl, "pit");

		Box.addTexture(boxTexture);
		Floor.addTexture(floorTexture);
		StartTile.addTexture(startTexture);
		Ramp.addTexture(boxTexture);
		FinishTile.addTexture(finishTexture);
		Pit.addTexture(pitTexture);
	}

	/**
	 * Sets the 'selected' flag for the maze element with the given coordinate
	 * @param x	X coordinate of the element to be selected
	 * @param z	Z coordinate of the element to be selected
	 */

	public void select(int x, int z)
	{
		if(isInBounds(x, z))
			selected[x][z] = maze[x][z].size() - 1;
	}

	/**
	 * Returns the size of the maze in the x direction in opengl units
	 * @return	the size of the maze in the x direction in opengl units
	 */

	public double getSizeX()
	{
		return MAZE_SIZE_X * SQUARE_SIZE;
	}

	/**
	 * Returns the size of the maze in the z direction in opengl units
	 * @return 	the size of the maze in the z direction in opengl units
	 */

	public double getSizeZ()
	{
		return  MAZE_SIZE_Z * SQUARE_SIZE;
	}

	/**
	 * Returns the height of a certain stack in th maze
	 * @param x	x coordinate of the stack
	 * @param z	z coordinate of the stack
	 * @return	Height in opengl units of the Maze stack.
	 */

	public float getHeight(int x, int z)
	{
		if(isInBounds(x,z))
			return maze[x][z].getHeight();
		return Integer.MIN_VALUE;
	}

	/**
	 * Gets the start position in opengl units and the initial orientation of the player in the maze.
	 * @return	double array containing 1: the x, 2: the y coordinate  and 3: the z coordinate of the 
	 * start position and 4: the initial angle. 
	 */

	public double[] getStart()
	{
		double[] res= new double[4];
		for(int i = 0; i <  maze[0].length; i++){
			for(int j = 0; j < maze[0].length; j++)
			{
				MazeObject start = null;
				if((start = maze[i][j].getInstanceOf(standards.get(ObjectMode.ADD_START))) != null)
				{
					res[0] = start.getPos().x;
					res[1] = start.getPos().y;
					res[2] = start.getPos().z;
					res[3] = start.getRotation()[1];
				}
			}
		}
		res[0] += 2.5;
		res[1] += 2.5;
		res[2] += 2.5;
		return res;
	}

	/**
	 * Sets the maze size to the given value
	 * @param x	new maze size in the x direction . 
	 * @param z	new maze size in the z direction . 
	 */

	public void setSize(int x, int z)
	{
		if(x > 0 && z > 0)
		{
			// Create new maze and selected arrays
			MAZE_SIZE_X = x;
			MAZE_SIZE_Z = z;
			MazeStack[][] newMaze = new MazeStack[MAZE_SIZE_X][MAZE_SIZE_Z];
			int[][] newSelected = new int[MAZE_SIZE_X][MAZE_SIZE_Z];

			for (int i = 0; i < newMaze.length; i++){
				for (int j = 0; j < newMaze[0].length; j++)
				{
					if(i < maze.length && j < maze[0].length)
						newMaze[i][j] = maze[i][j];
					else
						newMaze[i][j] = MazeStack.standard(i * SQUARE_SIZE, j * SQUARE_SIZE);
					newSelected[i][j] = -1;
				}
			}

			maze = newMaze;
			selected = newSelected;
		}
	}

	/**
	 * Sets the 'selected' flag of all elements in the maze to -1, which means: not selected.
	 */

	public void clearSelected()
	{
		for( int i = 0; i < maze.length; i++ )
			for( int j = 0; j < maze[0].length; j++ )
				selected[i][j] = -1;
	}

	/**
	 * Sets the 'selected' flag of all elements in the maze to -2, which means: all selected.
	 */

	public void selectedAll()
	{
		for( int i = 0; i < maze.length; i++ )
			for( int j = 0; j < maze[0].length; j++ )
				selected[i][j] = -2;
	}

	/**
	 * Removes the top MazeObject on the given location 
	 * @param x	x coordinate of the removed object
	 * @param z z coordinate of the removed object
	 */

	public void removeTop(int x, int z)
	{
		if(isInBounds(x, z))
			maze[x][z].pop();
	}

	/**
	 * Rotates the top MazeObject on the given location around any of the three axes.
	 * @param x		x coordinate of the rotated object
	 * @param z		z coordinate of the rotated object
	 * @param angle	rotated angle
	 * @param xAxis	boolean specifying whether the rotation is around the x axis.
	 * @param yAxis	boolean specifying whether the rotation is around the y axis.
	 * @param zAxis	boolean specifying whether the rotation is around the z axis.
	 */

	public void rotateTop(int x, int z, int angle, boolean xAxis, boolean yAxis, boolean zAxis)
	{
		if(isInBounds(x, z)){
			if(xAxis)
				maze[x][z].rotateTopX(((float)z+ 0.5f) * SQUARE_SIZE, angle);
			if(yAxis)
				maze[x][z].rotateTopY(((float)x+ 0.5f) * SQUARE_SIZE, ((float)z+ 0.5f) * SQUARE_SIZE, angle);
			if(zAxis)
				maze[x][z].rotateTopZ(((float)x+ 0.5f) * SQUARE_SIZE, angle);
		}
	}

	/**
	 * Removes all MazeObjects of a specific type from each stack in the maze, if possible.
	 * 
	 * @param drawMode	byte specifying which MazeObject needs to be removed
	 */

	public void removeBlocks(byte drawMode)
	{
		for(int i = 0; i < maze.length; i++){
			for (int j = 0; j < maze[0].length; j++){
				while(maze[i][j].getInstanceOf(standards.get(drawMode)) != null){
					maze[i][j].remove(standards.get(drawMode));
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
				if (selected[i][j] != -1 ) {
					if(drawMode < 0)
						maze[i][j].add(customs.get(-drawMode - 1).translate(i * SQUARE_SIZE, 0, j*SQUARE_SIZE));
					else
						maze[i][j].add(standards.get(drawMode).translate(i * SQUARE_SIZE, 0, j*SQUARE_SIZE));
					maze[i][j].getTop().rotateVerticesY(rotation, (float)(i + 0.5f) * SQUARE_SIZE, (float)(j + 0.5f) * SQUARE_SIZE);
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
				if(maze[i][j].getInstanceOf(standards.get(ObjectMode.ADD_START)) != null){
					start = true;
				}
				if(maze[i][j].getInstanceOf(standards.get(ObjectMode.ADD_FINISH)) != null){
					finish = true;
				}
			}
		}
		if(!start){
			ErrorMessage.show("No start position defined\nPlease input start position before saving.");
		} else if(!finish){
			ErrorMessage.show("No finish position defined\nPlease input finish position before saving.");
		}else{
			try {
				wr.write(MAZE_SIZE_X + " " + MAZE_SIZE_Z + "\n");
				for(CustomMazeObject custom: customs)
					wr.write(custom.getFile().getCanonicalPath() + "\n");
				wr.write(0 + "\n");
				for (int i = 0; i < maze.length; i++) {
					for (int j = 0; j < maze[0].length; j++) {
						ArrayList<MazeObject> stack = maze[i][j].get();
						for(int k = 0; k < stack.size(); k++)
						{
							if(customs.contains(stack.get(k)))
								wr.print(-1 - customs.indexOf(stack.get(k)));
							else
								wr.print(standards.indexOf(stack.get(k)));
							int[] rotation = stack.get(k).rotation;
							wr.print("," + (rotation[0]/90%4 + 4*(rotation[1]/90%4) + 16*(rotation[2]/90%4)));
							if(k != stack.size() - 1)
								wr.print(";");
						}
						if (j < maze[0].length - 1)
							wr.print(" ");
						else
							wr.print("\n");
					}
				}
				wr.write("\n");
				return true;
			} catch (Exception e) {
				ErrorMessage.show("Exception while writing maze.\n" + e.toString());
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
			Maze res = new Maze(mazeSizeX, mazeSizeZ);
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
			for (int i = 0; i < res.maze.length; i++) {
				for (int j = 0; j < res.maze[0].length; j++) {
					String line = sc.next();
					String[] codes = line.split("[;]");
					for(String code : codes)
					{
						String[] objectElements = code.split("[,]");
						byte object = Byte.parseByte(objectElements[0]);
						byte rotation = Byte.parseByte(objectElements[1]);
						if(object < 0)
							res.maze[i][j].add(customs.get(-(object + 1)).translate(SQUARE_SIZE * i, 0, SQUARE_SIZE * j));
						else if (object != 8)
							res.maze[i][j].add(standards.get(object).translate(SQUARE_SIZE * i, 0, SQUARE_SIZE * j));
						res.maze[i][j].rotateTopX(((float)j+ 0.5f) * SQUARE_SIZE, rotation%4*90);
						res.maze[i][j].rotateTopY(((float)i+ 0.5f) * SQUARE_SIZE, ((float)j+ 0.5f) * SQUARE_SIZE, rotation/4*90);
						res.maze[i][j].rotateTopZ(((float)i+ 0.5f) * SQUARE_SIZE, rotation/16*90);
					}
				}
			}

			return res;
		} catch (Exception e) {
			ErrorMessage.show("Exception while reading maze.\n" + e.toString());
			return new Maze();
		}
	}

	public void setCustomTextures(GL gl)
	{

		for(CustomMazeObject obj : customs){
			obj.setTexture(gl);
			for(int i = 0; i < maze.length; i++){
				for(int j = 0; j < maze[0].length; j++)
				{
					for(MazeObject object : maze[i][j].get())
					{
						if(obj.equals(object))
						{
							CustomMazeObject that = (CustomMazeObject) object;
							that.setTexNum(obj.getTexNum());
						}
					}
				}
			}
		}
	}

	/**
	 * Draws the maze. First the textures of all custom maze objects are initialized, if the were not already.
	 * Then each object gets a color depending on whether or not it was selected.
	 */
	public void display(GL gl) {
		if(customSize != customs.size())
		{
			setCustomTextures(gl);
		}
		//Define all colours and change them if the element is selected
		float selectedColour[] = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

		float notSelectedColour[] = new float[]{0.6f, 0.6f, 0.6f, 1.0f};
		customSize = customs.size();
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				ArrayList<MazeObject> stack = maze[i][j].get();
				for(int k = 0; k < stack.size(); k++)
				{
					if (selected[i][j] == k || selected[i][j] == -2) {
						stack.get(k).draw(gl,  selectedColour);
					} else {
						stack.get(k).draw(gl,  notSelectedColour);
					}
				}
			}
		}
	}

	/**
	 * Gets the Maze Object on a certain index on a certain stack, if it exists.
	 * @param x	x coordinate of the stack
	 * @param y	index of the object in the stack.
	 * @param z z coordinate of the stack
	 * @return	The requested mazeObject, if it exists, else a Floor object without dimensions.
	 */

	public MazeObject get(int x, int y, int z)
	{
		if(isInBounds(x, z))
			if(y >= 0 && y < maze[x][z].size())
				return maze[x][z].get().get(y);
		return null;
	}

	/**
	 * Gets all maze Objects in a certain stack in the maze.
	 * @param x x coordinate of the stack
	 * @param z z coordinate of the stack
	 * @return	ArrayList of the objects in the requested stack, this list is empty if the stack doent exists.
	 */

	public ArrayList<MazeObject> get(int x, int z)
	{
		if(isInBounds(x, z))
			return maze[x][z].get();
		return new ArrayList<MazeObject>();
	}

	/**
	 * Adds the given mazeObject to a selected stack
	 * @param mazeObject	Maze Object that is added
	 * @param x				x coordinate of the stack
	 * @param z				z coordinate of the stack
	 */

	public void set(MazeObject mazeObject, int x, int z)
	{
		if(isInBounds(x, z))
			maze[x][z].add(mazeObject.translate(x*SQUARE_SIZE, 0, z*SQUARE_SIZE)); 
	}

	public float getFloorHeight(int x, int z)
	{
		if(isInBounds(x, z))
		{
			MazeObject floor =  maze[x][z].getInstanceOf(standards.get(ObjectMode.ADD_FLOOR));
			if(floor != null)
			{
				return maze[x][z].getHeight();
			}
		}
		return -1;
	}

	public MazeObject[] getNeighbourTiles(int x, int z, float height){
		if(isInBounds(x, z))
		{
			MazeObject[] res = new MazeObject[4];
			// The lines below work for the tile relative
			// to the middle tile according to the comment behind it
			// but this assumes that x is vertical in the maze-array and z is horizontal
			if(x > 0)
				res[0] = maze[(int)x-1][(int)z].getAtHeight(height); // up
			if(z > 0)
				res[1] = maze[(int)x][(int)z-1].getAtHeight(height); // left
			if(x < MAZE_SIZE_X - 1)
				res[2] = maze[(int)x+1][(int)z].getAtHeight(height); // down
			if(z < MAZE_SIZE_Z - 1)
				res[3] = maze[(int)x][(int)z+1].getAtHeight(height); // right
			return res;
		}
		return null;
	}

}
