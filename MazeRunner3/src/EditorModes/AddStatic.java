package EditorModes;

import MainGame.Level;

public class AddStatic extends AddMode {
	
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
