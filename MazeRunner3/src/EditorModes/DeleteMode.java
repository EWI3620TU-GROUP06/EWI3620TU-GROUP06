package EditorModes;

import LevelHandling.Level;

public class DeleteMode extends EditMode {
	
	int pressedX, pressedZ;

	public DeleteMode(Level level) {
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		if(mazeX != pressedX || mazeZ != pressedZ)
		{
			level.getMaze().removeTop(mazeX, mazeZ);
		}
	}

	@Override
	public void mouseReleased() {
		// Do Nothing
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.getMaze().removeTop(mazeX, mazeZ);
		pressedX = mazeX;
		pressedZ = mazeZ;
	}

}
