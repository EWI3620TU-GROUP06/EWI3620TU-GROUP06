package EditorModes;

import LevelHandling.Level;

/**
 * Adds a button to the maze that controls the movement of the last added Movable Box. 
 * Activating this button will let the Movable Box it is connected to move over its path only once.
 * 
 * @author Jorne Boterman
 *
 */

public class AddOnceButton extends EditMode {
	
	public AddOnceButton(Level level) {
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.setButton(mazeX, mazeZ, 1);
	}

	@Override
	public void mouseReleased() {
		
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.setButton(mazeX, mazeZ, 1);
	}

}
