package LevelHandling;
import javax.media.opengl.GL;

import Drawing.DrawingUtil;
import Drawing.VisibleObject;
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

	public int MAZE_SIZE = 10;
	public static final int SQUARE_SIZE = 5;

	private boolean[][] selected = new boolean[MAZE_SIZE][MAZE_SIZE];

	private int[] startPosition = {6, 0, 5, 90};
	private int[] finishPosition = {8,8};

	private static Texture boxTexture;
	private static Texture floorTexture;
	private static Texture finishTexture;

	private MazeObject[][] maze = null;
	public static final ArrayList<MazeObject> standards = new ArrayList<MazeObject>(Arrays.asList(new MazeObject[]{
			new Floor(SQUARE_SIZE, 0, 0),
			new Box(SQUARE_SIZE, SQUARE_SIZE, 0, 0, 0),
			new Box(SQUARE_SIZE, (float)SQUARE_SIZE/2, 0, 0, 0),
			new StartTile(SQUARE_SIZE, 0, 0),
			new FinishTile(SQUARE_SIZE, 0, 0),
			new Ramp(SQUARE_SIZE, SQUARE_SIZE, 0, 0, 0),
			new Ramp(SQUARE_SIZE, (float)SQUARE_SIZE / 2,  0, 0, 0)	
	}));


	public static ArrayList<CustomMazeObject> customs = new ArrayList<CustomMazeObject>();
	private int customSize = 0;

	public Maze()
	{
		maze = new MazeObject[MAZE_SIZE][MAZE_SIZE];
		for(int i = 0; i < MAZE_SIZE; i++)
			for(int j = 0; j < MAZE_SIZE; j++)
				maze[i][j] = new Floor(SQUARE_SIZE, i * SQUARE_SIZE, j*SQUARE_SIZE);
	}

	public Maze(int mazeSize, int[] start, int[] finish, byte[][] object, byte[][] rotation)
	{
		MAZE_SIZE = mazeSize;
		startPosition = start;
		finishPosition = finish;
		maze = new MazeObject[MAZE_SIZE][MAZE_SIZE];
		for(int i = 0; i < MAZE_SIZE; i++){
			for(int j = 0; j < MAZE_SIZE; j++){
				if(object[i][j] < 0)
					maze[i][j] = customs.get(-(object[i][j] + 1)).translate(SQUARE_SIZE * i, 0, SQUARE_SIZE * j);
				else
					maze[i][j] = standards.get(object[i][j]).translate(SQUARE_SIZE * i, 0, SQUARE_SIZE * j);
				maze[i][j].rotateVerticesX(rotation[i][j]%4*90, 0, (float)(j + 0.5f)*SQUARE_SIZE);
				maze[i][j].rotateVerticesY(rotation[i][j]/4*90, (float)(i + 0.5f)*SQUARE_SIZE, (float)(j + 0.5f)*SQUARE_SIZE);
			}
		}
		selected = new boolean[MAZE_SIZE][MAZE_SIZE];
	}

	public void removeRedundantFaces()
	{
		for(int i = 0; i < maze[0].length; i++){
			for(int j  = 0; j < maze.length; j++){
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
		return x > finishPosition[0] * SQUARE_SIZE && x < (finishPosition[0] + 1) * SQUARE_SIZE &&
				z > finishPosition[1] * SQUARE_SIZE && z < (finishPosition[1] + 1) * SQUARE_SIZE;
	}

	/**
	 * Initialize the textures used by the maze.
	 * @param gl	instance of opengl.
	 */

	public static void initTextures(GL gl)
	{
		boxTexture = DrawingUtil.initTexture(gl, "wall");
		floorTexture = DrawingUtil.initTexture(gl, "floor");
		finishTexture = DrawingUtil.initTexture(gl, "finish");

		Box.addTexture(boxTexture);
		Floor.addTexture(floorTexture);
		StartTile.addTexture(floorTexture);
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
		if(x >= 0 && x < MAZE_SIZE && z >= 0 && z < MAZE_SIZE)
			selected[x][z] = true;
	}

	/**
	 * Returns the size of the maze in opengl units
	 * @return	the size of the maze in opengl units
	 */

	public double getSize()
	{
		return MAZE_SIZE*SQUARE_SIZE;
	}

	/**
	 * Gets the start position in opengl units and the initiial orientation of the player in the maze.
	 * @return	double array containing 1: the x and 2: the z coordinate of the startposition and 3: the initial angle. 
	 */

	public double[] getStart()
	{
		double[] res= new double[4];
		res[0] = (startPosition[0] + 0.5)* SQUARE_SIZE;
		res[1] = (startPosition[1] + 0.5)*SQUARE_SIZE;
		res[2] = (startPosition[2] + 0.5)* SQUARE_SIZE;
		res[3] = startPosition[3];
		return res;
	}

	/**
	 * Changes the size of the maze with the given amount. The maze must alway contain at least one square.
	 * @param n	the amount by which the maze size needs to be changed. 
	 */

	public void addToSize(int n)
	{
		if(MAZE_SIZE + n > 0)
		{
			// Create new maze and selected arrays
			MAZE_SIZE += n;
			MazeObject[][]newMaze = new MazeObject[MAZE_SIZE][MAZE_SIZE];
			boolean[][]newSelected = new boolean[MAZE_SIZE][MAZE_SIZE];

			for (int i = 0; i < newMaze[0].length; i++){
				for (int j = 0; j < newMaze.length; j++)
				{
					if(i < maze[0].length && j < maze.length)
						newMaze[i][j] = maze[i][j];
					else
						newMaze[i][j] = standards.get(0).translate(i * SQUARE_SIZE, 0, j*SQUARE_SIZE);
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
		for( int i = 0; i < MAZE_SIZE; i++ )
			for( int j = 0; j < MAZE_SIZE; j++ )
				selected[i][j] = false;
	}

	public void selectedAll()
	{
		for( int i = 0; i < MAZE_SIZE; i++ )
			for( int j = 0; j < MAZE_SIZE; j++ )
				selected[i][j] = true;
	}

	public void setStart(int rotation)
	{
		maze[startPosition[0]][startPosition[2]] = standards.get(0).translate(startPosition[0]*SQUARE_SIZE, startPosition[1]*SQUARE_SIZE, startPosition[2]*SQUARE_SIZE);
		for(int i = 0; i < MAZE_SIZE; i++){
			for (int j = 0; j < MAZE_SIZE; j++){
				if(selected[i][j] && !(i == finishPosition[0] && j == finishPosition[1])){
					startPosition[0] = i;
					startPosition[2] = j;
					startPosition[3] = rotation;
					break;
				}
			}
		}
		maze[startPosition[0]][startPosition[2]] = standards.get(3).translate(startPosition[0]*SQUARE_SIZE, startPosition[1]*SQUARE_SIZE, startPosition[2]*SQUARE_SIZE);
	}

	public void setFinish()
	{
		maze[finishPosition[0]][finishPosition[1]] = standards.get(0).translate(finishPosition[0]*SQUARE_SIZE, 0, finishPosition[1]*SQUARE_SIZE);
		for(int i = 0; i < MAZE_SIZE; i++){
			for (int j = 0; j < MAZE_SIZE; j++){
				if(selected[i][j]&& !(i == startPosition[0] && j == startPosition[2])){
					finishPosition[0] = i;
					finishPosition[1] = j;
					break;
				}
			}
		}
		maze[finishPosition[0]][finishPosition[1]] = standards.get(4).translate(finishPosition[0]*SQUARE_SIZE, 0, finishPosition[1]*SQUARE_SIZE);
	}

	/**
	 * Adds an element to the maze at the selected position(s). 
	 * @param drawMode	Which element needs to be added. See Editor for drawMode declarations.
	 */

	public void addBlock(byte drawMode, int rotation)
	{			
		for(int i = 0; i < MAZE_SIZE; i++)
			for (int j = 0; j < MAZE_SIZE; j++)
				if (selected[i][j] && !(i == startPosition[0] && j == startPosition[2]) &&
						!(i == finishPosition[0] && j == finishPosition[1])) {
					if(drawMode < 0)
						maze[i][j] = customs.get(-drawMode - 1).translate(i * SQUARE_SIZE, 0, j*SQUARE_SIZE);
					else
						maze[i][j] = standards.get(drawMode).translate(i * SQUARE_SIZE, 0, j*SQUARE_SIZE);
					maze[i][j].rotateVerticesY(rotation, (float)(i + 0.5f) * SQUARE_SIZE, (float)(j + 0.5f) * SQUARE_SIZE);
				}
	}

	/**
	 * Rotates the selected element(s) by ninety degrees, is the selected element(s) can  be rotated.
	 */

	public void rotateSelected(boolean x, boolean y, boolean z)
	{
		for(int i = 0; i < MAZE_SIZE; i++){
			for (int j = 0; j < MAZE_SIZE; j++){
				if (selected[i][j]){
					if(x)
						maze[i][j].rotateVerticesX(90, 0, (j + 0.5) * SQUARE_SIZE);
					if(y)
						maze[i][j].rotateVerticesY(90, (i + 0.5) * SQUARE_SIZE, (j + 0.5) * SQUARE_SIZE);
					if(z)
						maze[i][j].rotateVerticesZ(90, (i + 0.5) * SQUARE_SIZE, 0);
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
		if (startPosition[0] < 0 || startPosition[0] >= MAZE_SIZE
				|| startPosition[2] < 0 || startPosition[2] >= MAZE_SIZE) {
			System.err.println("Invalid start position.");
			return false;
		} else if (finishPosition[0] < 0 || finishPosition[0] >= MAZE_SIZE
				|| finishPosition[1] < 0 || finishPosition[1] >= MAZE_SIZE) {
			System.err.println("Invalid finish position.");
			return false;
		} else {
			try {
				wr.write(MAZE_SIZE + "\n");
				wr.write(startPosition[0] + " " + startPosition[1] + " "
						+ startPosition[2] +  " " + startPosition[3] + "\n");
				wr.write(finishPosition[0] + " " + finishPosition[1] + "\n");
				for(CustomMazeObject custom: customs)
					wr.write(custom.getFile().getCanonicalPath() + "\n");
				wr.write(0 + "\n");
				for (int i = 0; i < maze[0].length; i++) {
					for (int j = 0; j < maze.length; j++) {
						if(customs.contains(maze[i][j]))
							wr.print(-1 - customs.indexOf(maze[i][j]));
						else
							wr.print(standards.indexOf(maze[i][j]));
						int[] rotation = maze[i][j].rotation;
						wr.print("," + (rotation[0]/90%4 + 4*(rotation[1]/90%4) + 16*(rotation[2]/90%4)));
						if (j < maze.length - 1)
							wr.print(" ");
						else
							wr.print("\n");
					}
				}
				wr.write("\n");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

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
			int mazeSize = sc.nextInt();
			int[] newStart = new int[4];
			newStart[0] = sc.nextInt();
			newStart[1] = sc.nextInt();
			newStart[2] = sc.nextInt();
			newStart[3] = sc.nextInt();
			int[] newFinish = new int[2];
			newFinish[0] = sc.nextInt();
			newFinish[1] = sc.nextInt();
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
			byte[][] objects = new byte[mazeSize][mazeSize];
			byte[][] rotation = new byte[mazeSize][mazeSize];
			for (int i = 0; i < mazeSize; i++) {
				for (int j = 0; j < mazeSize; j++) {
					String line = sc.next();
					String[] objectElements = line.split("[,]");
					objects[i][j] = Byte.parseByte(objectElements[0]);
					rotation[i][j] = Byte.parseByte(objectElements[1]);
				}
			}
			return new Maze(mazeSize, newStart, newFinish, objects, rotation);
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
				for(int i = 0; i < maze[0].length; i++){
					for(int j = 0; j < maze.length; j++)
					{
						MazeObject object = maze[i][j];
						if(object instanceof CustomMazeObject){
							CustomMazeObject that = (CustomMazeObject) object;
							if(that.equals(obj)){
								that.setTexNum(obj.getTexNum());
							}

						}
					}
				}
			}


		}
		customSize = customs.size();
		for (int i = 0; i < MAZE_SIZE; i++) {
			for (int j = 0; j < MAZE_SIZE; j++) {
				//Define all colours and change them if the element is selected
				float wallColour[] = new float[4];
				wallColour[3] = 1.0f;
				float floorColour[] = new float[4];
				floorColour[3] = 1.0f;
				float startColour[] = {0.0f, 1.0f, 0.0f, 1.0f };
				float finishColour[] = { 1.0f, 0.0f, 0.0f, 1.0f };
				if (selected[i][j]) {
					wallColour[0] = 1.0f;
					wallColour[1] = 1.0f;
					wallColour[2] = 1.0f;
					floorColour[0] = 1.0f;
					floorColour[1] = 1.0f;
					floorColour[2] = 1.0f;
				} else {
					wallColour[0] = 0.6f;
					wallColour[1] = 0.6f;
					wallColour[2] = 0.6f;
					floorColour[0] = 0.6f;
					floorColour[1] = 0.6f;
					floorColour[2] = 0.6f;
				}
				//Draw the start and finish squares
				if (i == startPosition[0] && j == startPosition[2])
					maze[i][j].draw(gl,  startColour);
				else if (i == finishPosition[0] && j == finishPosition[1])
					maze[i][j].draw(gl,  finishColour);
				else
					maze[i][j].draw(gl,  wallColour);
			}
		}
	}

	public MazeObject get(int x, int z)
	{
		return maze[x][z];
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
