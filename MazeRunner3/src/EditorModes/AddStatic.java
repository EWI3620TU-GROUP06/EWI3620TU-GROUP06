package EditorModes;

import LevelHandling.Level;

/**
 * AddStatic adds non-rotatable MazeObjects such as Box of Floor on the selected tiles. 
 * Tiles can be selected by clicking and dragging: objects are added when the mouse is released.
 * @author Tom Hogervorst
 *
 */

public class AddStatic extends ObjectMode {
	
	public AddStatic(Level level, byte drawMode)
	{
		super(level, drawMode);
	}
	
	@Override
	public void mousePressed(int mazeX, int mazeZ)
	{
		level.getMaze().select(mazeX, mazeZ);
	}
	
	@Override 
	public void mouseDragged(int mazeX, int mazeZ)
	{
		level.getMaze().select(mazeX, mazeZ);
	}
	
	@Override
	public void mouseReleased()
	{
		level.getMaze().addBlock(drawMode, rotation);
	}

}
