package EditorModes;

import LevelHandling.Level;

/**
 * Edit Mode is a abstract class that is used is used to set the editor to a certain mode (or state). 
 * Each edit mode specifies what the editor should do when the mouse is clicked, dragged of released.
 * 
 * @author Tom Hogervorst
 *
 */

public abstract class EditMode {
	
	protected Level level;
	
	public EditMode(Level level)
	{
		this.level = level;
	}
	
	public abstract void mouseDragged(int mazeX, int mazeZ);
	public abstract void mouseReleased();
	public abstract void mousePressed(int mazeX, int mazeZ);
	
	public void setLevel(Level level)
	{
		this.level = level;
	}

}
