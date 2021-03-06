package EditorModes;

import LevelHandling.Level;

/**
 * Removes the MazeObject, Movable Box or PowerUp on the top of the stack where the mouse was clicked.
 * 
 * @author Tom Hogervorst
 *
 */

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
			level.getMaze().clearSelected();
			level.getMaze().select(mazeX, mazeZ);
			pressedX = mazeX;
			pressedZ = mazeZ;
		}
	}

	@Override
	public void mouseReleased() {
		// Do Nothing
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.removeTop(mazeX, mazeZ);
		pressedX = mazeX;
		pressedZ = mazeZ;
	}

}
