package MainGame;
import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

import java.io.*;
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
	public final double SQUARE_SIZE = 5;

	private boolean[][] selected = new boolean[MAZE_SIZE][MAZE_SIZE];

	private int[] startPosition = {6, 5, 90};
	private int[] finishPosition = {8,8};


	private StartArrow arrow = new StartArrow((float) SQUARE_SIZE, startPosition[2]);

	private final Box box = new Box((float) SQUARE_SIZE,(float) SQUARE_SIZE); 
	private final Ramp ramp0 = new Ramp((float) SQUARE_SIZE, (float) SQUARE_SIZE, 0);
	private final Ramp ramp1 = new Ramp((float) SQUARE_SIZE, (float) SQUARE_SIZE, 1);
	private final Ramp ramp2 = new Ramp((float) SQUARE_SIZE, (float) SQUARE_SIZE, 2);
	private final Ramp ramp3 = new Ramp((float) SQUARE_SIZE, (float) SQUARE_SIZE, 3);
	private final Box flatBox = new Box((float) SQUARE_SIZE,  (float) SQUARE_SIZE / 2);
	private final Ramp lowRamp0 = new Ramp((float) SQUARE_SIZE,  (float) SQUARE_SIZE / 2, 0);
	private final Ramp lowRamp1 = new Ramp((float) SQUARE_SIZE,  (float) SQUARE_SIZE / 2, 1);
	private final Ramp lowRamp2 = new Ramp((float) SQUARE_SIZE,  (float) SQUARE_SIZE / 2, 2);
	private final Ramp lowRamp3 = new Ramp((float) SQUARE_SIZE,  (float) SQUARE_SIZE / 2, 3);

	private Texture boxTexture;
	private Texture floorTexture;

	private byte[][] maze = 

		{	{  1,  1,  1,  1,  1,  1,  1,  1,  1,  1 },
			{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
			{  1,  0,  0,  0,  0,  0,  1,  1,  1,  1 },
			{  1,  0,  1,  0,  0,  0,  1,  0,  0,  1 },
			{  1,  0,  1,  0,  1,  0,  1,  0,  0,  1 },
			{  1,  0,  1,  0,  1,  0,  1,  0,  0,  1 },
			{  1,  0,  0,  0,  1,  0,  1,  0,  0,  1 },
			{  1,  0,  0,  0,  1,  1,  1,  0,  0,  1 },
			{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
			{  1,  1,  1,  1,  1,  1,  1,  1,  1,  1 }	};
	
	public Maze()
	{
		//Standard maze is loaded
	}
			
	public Maze(int mazeSize, int[] start, int[] finish, byte[][] newMaze)
	{
		MAZE_SIZE = mazeSize;
		startPosition = start;
		finishPosition = finish;
		maze = new byte[MAZE_SIZE][MAZE_SIZE];
		for(int i = 0; i < MAZE_SIZE; i++){
			for(int j = 0; j < MAZE_SIZE; j++){
				maze[i][j] = newMaze[i][j];
			}
		}
		selected = new boolean[MAZE_SIZE][MAZE_SIZE];
	}
	/**
	 * Initialize the textures used by the maze.
	 * @param gl	instance of opengl.
	 */

	public void initTextures(GL gl)
	{
		try{
			InputStream stream = getClass().getResourceAsStream("wall.jpg");
			TextureData data = TextureIO.newTextureData(stream, false, "jpg");
			this.boxTexture = TextureIO.newTexture(data);
			stream = getClass().getResourceAsStream("floor.jpg");
			data = TextureIO.newTextureData(stream, false, "jpg");
			this.floorTexture = TextureIO.newTexture(data);
			stream.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * isWall(int x, int z) checks for a wall.
	 * <p>
	 * It returns whether maze[x][z] contains a 1.
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a wall at maze[x][z]
	 */
	public boolean isWall( int x, int z )
	{
		if( x >= 0 && x < MAZE_SIZE && z >= 0 && z < MAZE_SIZE )
			return maze[x][z] == 1;
		else
			return false;
	}

	/**
	 * isWall(double x, double z) checks for a wall by converting the double values to integer coordinates.
	 * <p>
	 * This method first converts the x and z to values that correspond with the grid 
	 * defined by maze[][]. Then it calls upon isWall(int, int) to check for a wall.
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a wall at maze[x][z]
	 */
	public boolean isWall( double x, double z )
	{
		int gX = convertToGridX( x );
		int gZ = convertToGridZ( z );
		return isWall( gX, gZ );
	}

	/**
	 * Converts the double x-coordinate to its correspondent integer coordinate.
	 * @param x		the double x-coordinate
	 * @return		the integer x-coordinate
	 */
	private int convertToGridX( double x )
	{
		return (int)Math.floor( x / SQUARE_SIZE );
	}

	/**
	 * Converts the double z-coordinate to its correspondent integer coordinate.
	 * @param z		the double z-coordinate
	 * @return		the integer z-coordinate
	 */
	private int convertToGridZ( double z )
	{
		return (int)Math.floor( z / SQUARE_SIZE );
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
		double[] res= new double[3];
		res[0] = (startPosition[0] + 0.5)* SQUARE_SIZE;
		res[1] = (startPosition[1] + 0.5)* SQUARE_SIZE;
		res[2] = startPosition[2];
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
			MAZE_SIZE += n;
			byte[][]newMaze = new byte[MAZE_SIZE][MAZE_SIZE];
			boolean[][]newSelected = new boolean[MAZE_SIZE][MAZE_SIZE];
			for (int i = 0; i < newMaze[0].length && i < maze[0].length; i++)
			{
				for (int j = 0; j < newMaze.length && j < maze.length; j++)
				{
					newMaze[i][j] = maze[i][j];
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
	
	/**
	 * Adds an element to the maze at the selected position(s). 
	 * @param drawMode	Which element needs to be added. See Editor for drawMode declarations.
	 * @param angle		Orientation of the element the needs to be added: This value is always a multiple of ninety degrees.
	 */

	public void addBlock(byte drawMode, int angle)
	{
		for(int i = 0; i < MAZE_SIZE; i++)
			for (int j = 0; j < MAZE_SIZE; j++)
				if (selected[i][j] && !(drawMode != 3 && i == startPosition[0] && j == startPosition[1])
						&& !(i == finishPosition[0] && j == finishPosition[1])) {
					if (drawMode < 3)
						maze[i][j] = drawMode;

					if (drawMode == 3) {
						maze[i][j] = 0;
						startPosition[0] = i;
						startPosition[1] = j;
						startPosition[2] = angle;
						arrow.setAngle(angle);
					}
					if (drawMode == 4) {
						maze[i][j] = 0;
						finishPosition[0] = i;
						finishPosition[1] = j;
					}
					if (drawMode > 4) {
						int orientation = (angle / 45 + 1) / 2;

						maze[i][j] = (byte) (4 * (drawMode - 4) + orientation);
					}
				}
	}
	
	/**
	 * Rotates the selected element(s) by ninety degrees, is the selected element(s) can  be rotated.
	 */

	public void rotateSelected()
	{
		for(int i = 0; i < MAZE_SIZE; i++)
			for (int j = 0; j < MAZE_SIZE; j++)
				if (selected[i][j])
				{
					if(maze[i][j] > 3)
						maze[i][j] = (byte)(maze[i][j] - maze[i][j] % 4 + (maze[i][j] + 1) % 4); 
				}
	}
	
	/**
	 * Writes the maze to a file. First the size is written, then the start and finish positions and finally
	 * the maze data.
	 * @param file	File to be written.
	 */

	public void save(File file) {
		if (startPosition[0] < 0 || startPosition[0] >= MAZE_SIZE
				|| startPosition[1] < 0 || startPosition[1] >= MAZE_SIZE) {
			System.err.println("Invalid start position.");
		} else if (finishPosition[0] < 0 || finishPosition[0] >= MAZE_SIZE
				|| finishPosition[1] < 0 || finishPosition[1] >= MAZE_SIZE) {
			System.err.println("Invalid finish position.");
		} else {
			try {
				PrintWriter wr = new PrintWriter(file);
				wr.write(MAZE_SIZE + "\n");
				wr.write(startPosition[0] + " " + startPosition[1] + " "
						+ startPosition[2] + "\n");
				wr.write(finishPosition[0] + " " + finishPosition[1] + "\n");
				for (int i = 0; i < maze[0].length; i++) {
					for (int j = 0; j < maze.length; j++) {
						if (j < maze.length - 1)
							wr.print(maze[i][j] + " ");
						else
							wr.print(maze[i][j] + "\n");
					}
				}
				wr.close();
			} catch (Exception e) {
				e.printStackTrace();
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

	public static Maze read(File file) {
		try {
			Scanner sc = new Scanner(file);
			int mazeSize = sc.nextInt();
			int[] newStart = new int[3];
			newStart[0] = sc.nextInt();
			newStart[1] = sc.nextInt();
			newStart[2] = sc.nextInt();
			int[] newFinish = new int[2];
			newFinish[0] = sc.nextInt();
			newFinish[1] = sc.nextInt();
			byte[][] newMaze = new byte[mazeSize][mazeSize];
			for (int i = 0; i < mazeSize; i++) {
				for (int j = 0; j < mazeSize; j++) {
					newMaze[i][j] = sc.nextByte();
				}
			}
			sc.close();
			return new Maze(mazeSize, newStart, newFinish, newMaze);
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
		for (int i = 0; i < MAZE_SIZE; i++) {
			for (int j = 0; j < MAZE_SIZE; j++) {
				//Define all colours and change them if the element is selected
				float wallColour[] = new float[4];
				wallColour[3] = 1.0f;
				float floorColour[] = new float[4];
				floorColour[3] = 1.0f;
				float startColour[] = {0.0f, 1.0f, 0.0f, 1.0f };
				float finishColour[] = { 1.0f, 0.0f, 0.0f, 1.0f };
				float arrowColour[] = { 1.0f, 1.0f, 1.0f, 1.0f };
				if (selected[i][j]) {
					wallColour[0] = 1.0f;
					wallColour[1] = 1.0f;
					wallColour[2] = 1.0f;
					floorColour[0] = 1.0f;
					floorColour[1] = 1.0f;
					floorColour[2] = 1.0f;
				} else {
					wallColour[0] = 0.8f;
					wallColour[1] = 0.8f;
					wallColour[2] = 0.8f;
					floorColour[0] = 0.8f;
					floorColour[1] = 0.8f;
					floorColour[2] = 0.8f;
				}
				gl.glPushMatrix();
				gl.glTranslated(i * SQUARE_SIZE, 0, j * SQUARE_SIZE);
				//Draw the start and finish squares
				if (i == startPosition[0] && j == startPosition[1])
				{
					arrow.draw(gl, arrowColour);
					paintSingleFloorTile(gl, SQUARE_SIZE, startColour);
				}
				else if (i == finishPosition[0] && j == finishPosition[1])
					paintSingleFloorTile(gl, SQUARE_SIZE, finishColour);
				else{
					//draw the element
					switch (maze[i][j]) {
					case 1:
						boxTexture.enable(); 
						boxTexture.bind();
						box.draw(gl, wallColour);
						boxTexture.disable(); 
						break;
					case 2:
						flatBox.draw(gl, wallColour);
						break;
					case 4:
						ramp0.draw(gl, wallColour);
						break;
					case 5:
						ramp1.draw(gl, wallColour);
						break;
					case 6:
						ramp2.draw(gl, wallColour);
						break;
					case 7:
						ramp3.draw(gl, wallColour);
						break;
					case 8:
						lowRamp0.draw(gl, wallColour);
						break;
					case 9:
						lowRamp1.draw(gl, wallColour);
						break;
					case 10:
						lowRamp2.draw(gl, wallColour);
						break;
					case 11:
						lowRamp3.draw(gl, wallColour);
						break;
					default:
						paintSingleFloorTile(gl, SQUARE_SIZE, floorColour);
						break;
					}
				}
				gl.glPopMatrix();
			}
		}
	}

	/**
	 * paintSingleFloorTile(GL, double) paints a single floor tile, to represent
	 * the floor of the entire maze.
	 * 
	 * @param gl
	 *            the GL context in which should be drawn
	 * @param size
	 *            the size of the tile
	 */
	private void paintSingleFloorTile(GL gl, double size, float[] wallColour) {
		
		floorTexture.enable(); // Enable the background texture
		floorTexture.bind(); // Bind the background texture to the next object
		
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0); // Set the materials used by the floor.

		gl.glNormal3d(0, 1, 0);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex3d(0, 0, 0);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3d(0, 0, size);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3d(size, 0, size);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3d(size, 0, 0);
		gl.glTexCoord2f(0, 1);
		gl.glEnd();
		floorTexture.disable(); // Setting the floor color and material.
	}	

}
