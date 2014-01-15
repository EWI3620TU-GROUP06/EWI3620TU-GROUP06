package LevelHandling;

import java.util.ArrayList;

import MazeObjects.*;

/**
 * The MazeStack Class defines a stack of Maze Objects: one column in the maze.
 * 
 * @author Tom Hogervorst
 *
 */

public class MazeStack {

	ArrayList<MazeObject> stack;
	
	/**
	 * Creates a new stack at the given location. the stack always has a bottom.
	 * @param x	x coordinate of the stack
	 * @param z	z coordinate of the stack
	 */

	public MazeStack(float x, float z)
	{
		stack = new ArrayList<MazeObject>();
		stack.add(new Bottom(Maze.SQUARE_SIZE, x, -100, z));
	}
	/**
	 * Adds a maze object to the top of the stack. If the previous top object of the stack had no height,
	 * it is removed. Any Top face of the previous top object is disabled, preventing two or more faces being
	 * drawn on the same place.
	 * @param mzObj	The MazeObject to be added.
	 */
	
	public void add(MazeObject mzObj)
	{
		float yMin = mzObj.getYMin();
		float yMax = getTop().getYMax();
		mzObj = mzObj.translate(0, yMax - yMin, 0);
		
		if( mzObj.hasBottom(stack.get(stack.size() - 1).getYMax()) || mzObj.getHeight() == 0){
			if(getTop().getHeight() == 0){
				pop();
			}
			getTop().setTop(false);
		}
		
		stack.add(mzObj);
	}
	/**
	 * Removes the top element of the stack.
	 */

	public void pop()
	{
		if(stack.size() > 1)
		{
			stack.remove(stack.size() - 1);
		}
		getTop().setTop(true);
	}
	
	/**
	 * Gets all elements in the stack.
	 * @return	The arraylist of Maze Objects in the stack
	 */

	public ArrayList<MazeObject> get()
	{
		return stack;
	}
	
	/**
	 * Returns the highest point in the stack.
	 * @return	highest point of the top Object in the stack
	 */

	public float getHeight()
	{
		return stack.get(stack.size() - 1).getYMax();
	}
	
	/**
	 * Returns the number of Objects in the stack.
	 * @return	Number of objects in the stack.
	 */

	public int size()
	{
		return stack.size();
	}
	
	public MazeObject getAtHeight(int height)
	{
		for(int i = 0; i < size(); i++)
		{
			if(stack.get(i).getYMax() >= height)
			{
				return stack.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Gets the top Object in the stack.
	 * @return	MazeObject at the top of the stack.
	 */

	public MazeObject getTop()
	{
		return stack.get(stack.size() - 1);
	}
	
	/**
	 * Clears the stack.
	 */

	public void clear()
	{
		stack.clear();
	}
	
	/**
	 * Removes one MazeObject of a certain kind from the stack.
	 * @param mzObj	MazeObject type to be removed.
	 */
	
	public void remove(MazeObject mzObj)
	{
		if(stack.contains(mzObj))
			stack.remove(mzObj);
	}
	
	/**
	 * Gets the instance of a MazeObject in the stack that of the same kind as the given one.
	 * @param that	Kind of MazeObject to be returned
	 * @return		MazeObject of the given type in the stack, else returns null
	 */

	public MazeObject getInstanceOf(MazeObject that)
	{
		int index = stack.indexOf(that);
		if(index >= 0 && index < stack.size())
			return stack.get(index);
		else
			return null;
	}
	
	/**
	 * Rotates the top mazeObject by a certain angley Coordinate around which is rotated is calculated 
	 * using the height of the top MazeObject
	 * 
	 * @param z		Z coordinate around which is rotated
	 * @param angle	angle with which is to be rotated
	 */

	public void rotateTopX(float z, int angle)
	{
		MazeObject toBeRotated = getTop();
		float y;
		if(stack.size() > 1)
			y = stack.get(stack.size() - 2).getYMax();
		else
			y = -10;
		y += 0.5f * Maze.SQUARE_SIZE;
		toBeRotated.rotateVerticesX(angle, y, z);
		if(stack.size() > 1)
		{
			stack.get(stack.size() - 2).setTop(!toBeRotated.hasBottom(stack.get(stack.size() - 2).getYMax()));
		}
	}
	
	/**
	 * Rotates the top mazeObject by a certain angle
	 * 
	 * @param x		X coordinate around which is rotated
	 * @param z		Z coordinate around which is rotated
	 * @param angle	angle with which is to be rotated
	 */

	public void rotateTopY(float x, float z, int angle)
	{
		MazeObject toBeRotated = getTop();
		toBeRotated.rotateVerticesY(angle, x, z);
		if(stack.size() > 1)
		{
			stack.get(stack.size() - 2).setTop(!toBeRotated.hasBottom(stack.get(stack.size() - 2).getYMax()));
		}
	}
	
	/**
	 * Rotates the top mazeObject by a certain angle. y Coordinate around which is rotated is calculated 
	 * using the height of the top MazeObject.
	 * 
	 * @param x		X coordinate around which is rotated
	 * @param angle	angle with which is rotated
	 */

	public void rotateTopZ(float x, int angle)
	{
		MazeObject toBeRotated = getTop();
		float y;
		if(stack.size() > 1)
			y = stack.get(stack.size() - 2).getYMax();
		else
			y = -10;
		y += 0.5f * Maze.SQUARE_SIZE;
		toBeRotated.rotateVerticesZ(angle, x, y);
		if(stack.size() > 1)
		{
			stack.get(stack.size() - 2).setTop(!toBeRotated.hasBottom(stack.get(stack.size() - 2).getYMax()));
		}
	}
	
	/**
	 * Removes redundant faces that the given stack and this stack share. Also redundant faces inside this stack 
	 * itself are removed.
	 * 
	 * @param that	MazeStack that is checked with this to remove all redundant faces. 
	 */
	
	public void removeRedundantFaces(MazeStack that)
	{
		for(int i = 0; i < stack.size(); i++)
		{
			MazeObject thisObject = this.stack.get(i);
			if(i < stack.size() - 1)
				thisObject.removeRedunantFaces(this.stack.get(i + 1));
			for(MazeObject thatObject : that.stack)
			{
				if(thisObject.getHeight() == thatObject.getHeight() &&
						thisObject.getYMin() == thatObject.getYMin())
				{
					thisObject.removeRedunantFaces(thatObject);
				}
					
			}
		}
	}
	
	/**
	 * Returns a standard stack that is used in an empty maze
	 * @param x	x coordinate of the stack
	 * @param z	z coordinate of the stack
	 * @return	standard MazeStack
	 */

	public static MazeStack standard(float x, float z)
	{
		MazeStack res = new MazeStack(x, z);
		res.stack.add(new Pit(Maze.SQUARE_SIZE, 100, x, -100, z));
		res.add(new Floor(Maze.SQUARE_SIZE, x, 0, z));
		return res;
	}

}
