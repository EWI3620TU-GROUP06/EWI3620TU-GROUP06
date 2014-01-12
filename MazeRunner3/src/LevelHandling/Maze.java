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

	private int[][] selected = new int[MAZE_SIZE_X][MAZE_SIZE_Z];

	private MazeStack[][] maze = null;
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
					maze[i][j].removeRedundantFaces(maze[i + 1][j]);
				}
				if(j != maze.length - 1)
				{
					maze[i][j].removeRedundantFaces(maze[i][j + 1]);
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
			selected[x][z] = maze[x][z].size() - 1;
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
	
	public float getHeight(int x, int z)
	{
		return maze[x][z].getHeight();
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
	 * Sets the 'selected' flag of all elements in the maze to false.
	 */

	public void clearSelected()
	{
		for( int i = 0; i < maze.length; i++ )
			for( int j = 0; j < maze[0].length; j++ )
				selected[i][j] = -1;
	}

	public void selectedAll()
	{
		for( int i = 0; i < maze.length; i++ )
			for( int j = 0; j < maze[0].length; j++ )
				selected[i][j] = -2;
	}
	
	public void removeTop(int x, int z)
	{
		if(x >= 0 && x < MAZE_SIZE_X && z >= 0 && z < MAZE_SIZE_Z)
			maze[x][z].removeTop();
	}
	
	public void rotateTop(int x, int z, int angle, boolean xAxis, boolean yAxis, boolean zAxis)
	{
		if(xAxis)
			maze[x][z].rotateTopX(((float)z+ 0.5f) * SQUARE_SIZE, angle);
		if(yAxis)
			maze[x][z].rotateTopY(((float)x+ 0.5f) * SQUARE_SIZE, ((float)z+ 0.5f) * SQUARE_SIZE, angle);
		if(zAxis)
			maze[x][z].rotateTopZ(((float)x+ 0.5f) * SQUARE_SIZE, angle);
	}

	public void removeBlocks(byte drawMode)
	{
		for(int i = 0; i < maze.length; i++){
			for (int j = 0; j < maze[0].length; j++){
				MazeObject mzObj = maze[i][j].getInstanceOf(standards.get(drawMode));
				if(mzObj != null){
					maze[i][j].replace(mzObj, standards.get(ObjectMode.ADD_FLOOR).translate(
							i * SQUARE_SIZE, mzObj.getYMin(), j * SQUARE_SIZE ));
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
				for (int j = 0; j < res.maze.length; j++) {
					String line = sc.next();
					String[] codes = line.split("[;]");
					for(String code : codes)
					{
						String[] objectElements = code.split("[,]");
						byte object = Byte.parseByte(objectElements[0]);
						byte rotation = Byte.parseByte(objectElements[1]);
						if(object < 0)
							res.maze[i][j].add(customs.get(-(object + 1)).translate(SQUARE_SIZE * i, 0, SQUARE_SIZE * j));
						else
						{
							res.maze[i][j].add(
									standards.get(object).translate(SQUARE_SIZE * i, 0, SQUARE_SIZE * j));
						}
						res.maze[i][j].getTop().rotateVerticesX(rotation%4*90, 0, (float)(j + 0.5f)*SQUARE_SIZE);
						res.maze[i][j].getTop().rotateVerticesY(rotation/4*90, (float)(i + 0.5f)*SQUARE_SIZE, (float)(j + 0.5f)*SQUARE_SIZE);
					}
				}
			}

			return res;
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
						MazeObject object = maze[i][j].getInstanceOf(obj);
						CustomMazeObject that = (CustomMazeObject) object;
						that.setTexNum(obj.getTexNum());
					}
				}
			}
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

	public MazeObject get(int x, int y, int z)
	{
		if(x >= 0 && x < MAZE_SIZE_X && z >= 0 && z < MAZE_SIZE_Z)
			return maze[x][z].get().get(y);
		return new Floor(0, 0, 0, 0);
	}
	
	public ArrayList<MazeObject> get(int x, int z)
	{
		if(x >= 0 && x < MAZE_SIZE_X && z >= 0 && z < MAZE_SIZE_Z)
			return maze[x][z].get();
		return new ArrayList<MazeObject>();
	}

	public void set(MazeObject mazeObject, int x, int z)
	{
		if(x >= 0 && x < MAZE_SIZE_X && z >= 0 && z < MAZE_SIZE_Z)
			maze[x][z].add(mazeObject); 
	}

	public MazeObject[] getNeighbourTiles(int x, int z){
		MazeObject[] res = new MazeObject[4];
		// The lines below work for the tile relative
		// to the middle tile according to the comment behind it
		// but this assumes that x is vertical in the maze-array and z is horizontal
		res[0] = get((int)x-1, 0, (int)z); // up
		res[1] = get((int)x, 0, (int)z-1); // left
		res[2] = get((int)x+1, 0, (int)z); // down
		res[3] = get((int)x, 0, (int)z+1); // right
		return res;
	}

}
