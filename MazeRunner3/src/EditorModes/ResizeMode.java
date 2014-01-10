package EditorModes;

import LevelHandling.Level;

/**
 * The maze size is set so that the position the mouse is pressed or dragged to becomes the bottom-rightmost tile.
 * 
 * @author Tom Hogervorst
 *
 */

public class ResizeMode extends EditMode {

	public ResizeMode(Level level) {
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.resize(mazeX + 1, mazeZ + 1);
		level.getMaze().select(mazeX, mazeZ);
	}

	@Override
	public void mouseReleased() {
		//Do Nothing
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.resize(mazeX + 1, mazeZ + 1);
		level.getMaze().select(mazeX, mazeZ);
	}

}
