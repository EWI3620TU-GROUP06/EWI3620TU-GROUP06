package EditorModes;

import LevelHandling.Level;

/**
 * Adds a button to the maze that controls the movement of the last added Movable Box. 
 * Activating this button will let the Movable Box it is connected to move continuously.
 * @author Tom Hogervorst
 *
 */

public class AddButton  extends EditMode
{

	public AddButton(Level level) {
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.setButton(mazeX, mazeZ, 0);
	}

	@Override
	public void mouseReleased() {
		
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.setButton(mazeX, mazeZ, 0);
	}

}
