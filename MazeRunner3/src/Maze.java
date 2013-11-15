import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

import java.io.*;
import java.util.NoSuchElementException;
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

	private int[] selected = {-1,-1};

	private int[][] maze = 
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
		super();
	}
	
	public Maze(int[][] maze, int mazeSize)
	{
		super();
		MAZE_SIZE = mazeSize;
		this.maze = maze;
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

	public void select(int x, int z)
	{
		selected[0] = x;
		selected[1] = z;
	}

	public double getSize()
	{
		return MAZE_SIZE*SQUARE_SIZE;
	}

	public void addToSize(int n)
	{
		if(MAZE_SIZE + n > 0)
		{
			MAZE_SIZE += n;
			int[][]newMaze = new int[MAZE_SIZE][MAZE_SIZE];
			for (int i = 0; i < newMaze.length && i < maze.length; i++)
			{
				for (int j = 0; j < newMaze.length && j < maze.length; j++)
					newMaze[i][j] = maze[i][j];
			}
			maze = newMaze;
		}
	}
	
	public void toggleSelected()
	{
		if(selected[0] >= 0 && selected[0] < MAZE_SIZE && selected[1] >= 0 && selected[1] < MAZE_SIZE)
		{
			if(maze[selected[0]][selected[1]] == 0)
				maze[selected[0]][selected[1]] = 1;
			else
				maze[selected[0]][selected[1]] = 0;
		}
	}
	
	public void save(File file)
	{
		try{
			PrintWriter wr = new PrintWriter(file);
			wr.write(MAZE_SIZE + "\n");
			for(int i = 0; i < MAZE_SIZE; i++)
			{
				for(int j = 0; j < MAZE_SIZE; j++)
				{
					Integer tempInt = maze[i][j];
					String tempString = tempInt.toString();
					wr.write(tempString);
					if(j < MAZE_SIZE - 1)
						wr.write(" ");
					else
						wr.write("\n");
				}
			}
			wr.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static Maze read(File file)
	{
		try{
			Scanner sc = new Scanner(file);
			String temp = sc.next();
			int mazeSize = Integer.parseInt(temp);
			sc.nextLine();
			int[][] mazeArray = new int[mazeSize][mazeSize];
			for(int i = 0; i < mazeSize; i++)
			{
				for(int j = 0; j < mazeSize; j++)
				{
					temp = sc.next();
					mazeArray[i][j] = Integer.parseInt(temp);
				}
				if(sc.hasNextLine())
					sc.nextLine();
			}
			sc.close();
			return new Maze(mazeArray, mazeSize);
		}
		catch(NoSuchElementException e){
			System.err.println("Invalid file.");
		}
		catch(FileNotFoundException e){
			System.err.println("File not founs.");
		}
		return null;
	}

	public void display(GL gl) {
		GLUT glut = new GLUT();

		// draw the grid with the current material
		for( int i = 0; i < MAZE_SIZE; i++ )
		{
			for( int j = 0; j < MAZE_SIZE; j++ )
			{
				float wallColour[] =  { 0.5f, 0.0f, 0.7f, 1.0f };
				float floorColour[] = {0.0f, 0.0f, 1.0f, 0.0f};
				if(selected[0] == i && selected[1] == j)
				{
					wallColour[0] = 1.0f;
					wallColour[2] = 1.0f;
					floorColour[1] = 0.9f;
				}
				else
				{
					wallColour[0] = 0.5f;
					wallColour[2] = 0.7f;
					floorColour[1] = 0.0f;
				}
				gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);// Set the materials used by the wall.
				gl.glPushMatrix();

				if ( isWall(i, j) ){
					gl.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
					glut.glutSolidCube( (float) SQUARE_SIZE );
				}
				else{
					gl.glTranslated( i * SQUARE_SIZE, 0, j * SQUARE_SIZE);
					paintSingleFloorTile( gl, SQUARE_SIZE , floorColour); // Paint the floor.
				}
				gl.glPopMatrix();
			}
		}			
	}

	/**
	 * paintSingleFloorTile(GL, double) paints a single floor tile, to represent the floor of the entire maze.
	 * 
	 * @param gl	the GL context in which should be drawn
	 * @param size	the size of the tile
	 */
	private void paintSingleFloorTile(GL gl, double size, float[] wallColour)
	{
		// Setting the floor color and material.
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);	// Set the materials used by the floor.

		gl.glNormal3d(0, 1, 0);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, size);
		gl.glVertex3d(size, 0, size);
		gl.glVertex3d(size, 0, 0);		
		gl.glEnd();	
	}
}
