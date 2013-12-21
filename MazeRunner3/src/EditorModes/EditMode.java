package EditorModes;

import LevelHandling.Level;

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
