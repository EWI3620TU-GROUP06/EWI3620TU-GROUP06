package EditorModes;

import LevelHandling.Level;

/**
 * AddStart adds a StartArrow MazeObject at the position the mouse is clicked. And rotates it towards the mouse
 * when it is dragged. Previous StartArrows are removed: there can be only one start Position. 
 * 
 * @author Tom Hogervorst
 *
 */

public class AddStart extends ObjectMode {

	private int pressedX, pressedZ;
	private int previousRotation = 0;

	public AddStart(Level level){
		super(level, ObjectMode.ADD_START);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		int dX = mazeX - pressedX;
		int dZ = mazeZ - pressedZ;
		double unrounded = Math.atan2(dX, -dZ) / (0.5 * Math.PI);
		rotation = 90 * (int)(Math.round(unrounded));
		level.getMaze().rotateTop(pressedX, pressedZ, rotation - previousRotation, false, true, false);
		previousRotation = rotation;
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ){
		pressedX = mazeX;
		pressedZ = mazeZ;
		level.getMaze().select(mazeX, mazeZ);
		level.getMaze().removeBlocks(drawMode);
		level.getMaze().addBlock(drawMode, rotation);
	}

	@Override
	public void mouseReleased()
	{
		//Do Nothing
	}

}
