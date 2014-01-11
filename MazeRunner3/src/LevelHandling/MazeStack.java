package LevelHandling;

import java.util.ArrayList;

import MazeObjects.*;

public class MazeStack {

	ArrayList<MazeObject> stack;

	public MazeStack(float x, float z)
	{
		stack = new ArrayList<MazeObject>();
		stack.add(new Floor(Maze.SQUARE_SIZE, x, -10, z));
	}

	public void add(MazeObject mzObj)
	{
		float yMin = mzObj.getYMin();
		float yMax = yMin;

		if(stack.size() > 0)
		{
			yMax = getTop().getYMax();
			if( mzObj.hasBottom(stack.get(stack.size() - 1).getYMax())){
				if(getTop().getHeight() == 0)
					removeTop();
				getTop().setTop(false);
			}
			mzObj = mzObj.translate(0, yMax - yMin, 0);
		}
		else
			mzObj = mzObj.translate(0, -10, 0);

		stack.add(mzObj);
	}

	public void removeTop()
	{
		if(stack.size() > 1)
		{
			stack.remove(stack.size() - 1);
		}
		if(stack.size() > 0)
		{
			getTop().setTop(true);
		}
	}

	public ArrayList<MazeObject> get()
	{
		return stack;
	}

	public float getHeight()
	{
		if(stack.size() > 0)
			return stack.get(stack.size() - 1).getHeight();
		else
			return 0;
	}

	public int size()
	{
		return stack.size();
	}

	public MazeObject getTop()
	{
		if(stack.size() > 0)
			return stack.get(stack.size() - 1);
		else
			return null;
	}

	public void clear()
	{
		stack.clear();
	}

	public void replace(MazeObject toBeReplaced, MazeObject replacer)
	{
		int index = stack.indexOf(toBeReplaced);
		stack.remove(index);
		stack.add(index, replacer);
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

	public static MazeStack standard(float x, float z)
	{
		MazeStack res = new MazeStack(x, z);
		res.add(new Box(Maze.SQUARE_SIZE, Maze.SQUARE_SIZE, x, -10, z));
		res.add(new Box(Maze.SQUARE_SIZE, Maze.SQUARE_SIZE, x, -5, z));
		res.add(new Floor(Maze.SQUARE_SIZE, x, 0, z));
		return res;
	}

}
