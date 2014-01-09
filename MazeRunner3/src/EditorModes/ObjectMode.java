package EditorModes;

import LevelHandling.Level;

/**
 * ObjectMode is an abstract class that extends EditMode and that is the parent of all Edit Mode that add 
 * MazeObjects to the maze.
 * @author Tom Hogervorst
 *
 */

public abstract class ObjectMode extends EditMode{
	
	public static final byte ADD_FLOOR = 0;
	public static final byte ADD_BOX = 1;
	public static final byte ADD_LOW_BOX = 2;
	public static final byte ADD_START = 3;
	public static final byte ADD_FINISH = 4;
	public static final byte ADD_RAMP = 5;
	public static final byte ADD_LOW_RAMP = 6;
	
	protected byte drawMode;
	protected int rotation;
	
	public ObjectMode(Level level, Byte drawMode)
	{
		super(level);
		this.drawMode = drawMode;
		rotation = 0;
	}

}
