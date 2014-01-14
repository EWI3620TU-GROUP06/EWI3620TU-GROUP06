package LevelHandling;

import java.util.ArrayList;

import MazeObjects.*;

public class MazeStack {

	ArrayList<MazeObject> stack;

	public MazeStack(float x, float z)
	{
		stack = new ArrayList<MazeObject>();
		stack.add(new Bottom(Maze.SQUARE_SIZE, x, -100, z));
	}

	public void add(MazeObject mzObj)
	{
		float yMin = mzObj.getYMin();
		float yMax = getTop().getYMax();
		mzObj = mzObj.translate(0, yMax - yMin, 0);
		
		if( mzObj.hasBottom(stack.get(stack.size() - 1).getYMax()) || mzObj.getHeight() == 0){
			if(getTop().getHeight() == 0){
				removeTop();
			}
			getTop().setTop(false);
		}
		
		stack.add(mzObj);
	}

	public void removeTop()
	{
		if(stack.size() > 1)
		{
			stack.remove(stack.size() - 1);
		}
		getTop().setTop(true);
	}

	public ArrayList<MazeObject> get()
	{
		return stack;
	}

	public float getHeight()
	{
		return stack.get(stack.size() - 1).getYMax();
	}

	public int size()
	{
		return stack.size();
	}

	public MazeObject getTop()
	{
		return stack.get(stack.size() - 1);
	}

	public void clear()
	{
		stack.clear();
	}
	
	public void remove(MazeObject mzObj)
	{
		if(stack.contains(mzObj))
			stack.remove(mzObj);
	}

	public MazeObject getInstanceOf(MazeObject that)
	{
		int index = stack.indexOf(that);
		if(index >= 0 && index < stack.size())
			return stack.get(index);
		else
			return null;
	}

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

	public void rotateTopY(float x, float z, int angle)
	{
		MazeObject toBeRotated = getTop();
		toBeRotated.rotateVerticesY(angle, x, z);
		if(stack.size() > 1)
		{
			stack.get(stack.size() - 2).setTop(!toBeRotated.hasBottom(stack.get(stack.size() - 2).getYMax()));
		}
	}

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

	public static MazeStack standard(float x, float z)
	{
		MazeStack res = new MazeStack(x, z);
		res.stack.add(new Pit(Maze.SQUARE_SIZE, 100, x, -100, z));
		res.add(new Floor(Maze.SQUARE_SIZE, x, 0, z));
		return res;
	}

}
