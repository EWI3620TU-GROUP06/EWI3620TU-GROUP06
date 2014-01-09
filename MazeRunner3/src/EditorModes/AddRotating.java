package EditorModes;

import LevelHandling.Level;

/**
 * AddRotating adds a rotatable MazeObject such as a ramp on the tile that was clicked.
 * The added MazeObject can be rotated by dragging the mouse after clicking in: the MazeObject is rotated 
 * towards the mouse position and is fixed in place upon releasing it.
 * 
 * @author Tom Hogervorst
 *
 */

public class AddRotating extends ObjectMode {
	
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
