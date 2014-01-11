package EditorModes;

import LevelHandling.Level;

/**
 * The selected Maze Object is rotated 90 degrees around a certain axis when the mouse is pressed.
 * 
 * @author Tom Hogervorst.
 *
 */

public class RotateObject extends EditMode {
	
	private boolean[] axes = {false, false, false};
	
	public RotateObject(Level level, int axis)
	{
		super(level);
		if(axis >= 0 && axis < 3)
		this.axes[axis] = true;
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		// Do nothing
	}

	@Override
	public void mouseReleased() {
		// Do nothing
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.getMaze().rotateTop(mazeX, mazeZ, 90, axes[0], axes[1], axes[2]);
	}

}
