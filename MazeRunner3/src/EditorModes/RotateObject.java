package EditorModes;

import LevelHandling.Level;

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
		level.getMaze().rotateSelected(axes[0], axes[1], axes[2]);
	}

}
