package EditorModes;

import LevelHandling.Level;

public class AddRotating extends AddMode {
	
	private int pressedX, pressedZ;

	public AddRotating(Level level, byte drawMode)
	{
		super(level, drawMode);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		int dX = mazeX - pressedX;
		int dZ = mazeZ - pressedZ;
		double unrounded = Math.atan2(dX, -dZ) / (0.5 * Math.PI);
		rotation = 90 * (int)(Math.round(unrounded));
		level.getMaze().addBlock(drawMode, rotation);
	}
	
	@Override
	public void mousePressed(int mazeX, int mazeZ){
		pressedX = mazeX;
		pressedZ = mazeZ;
		level.getMaze().select(mazeX, mazeZ);
		level.getMaze().addBlock(drawMode, rotation);
	}
	
	@Override
	public void mouseReleased()
	{
		level.getMaze().addBlock(drawMode, rotation);
	}

}
