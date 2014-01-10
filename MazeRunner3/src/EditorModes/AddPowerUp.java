package EditorModes;

import LevelHandling.Level;

/**
 * AddPowerUp adds a powerUp of a certain type on the tile where the mouse was clicked. 
 * This Edit mode has no functions when dragging the mouse;
 * 
 * @author Tom Hogervorst
 *
 */

public class AddPowerUp extends EditMode {
	
	private byte type;

	public AddPowerUp(Level level, byte type) {
		super(level);
		this.type = type;
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		//Do Nothing
	}

	@Override
	public void mouseReleased() {
		//Do Nothing
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.addPowerUp(mazeX, mazeZ, type);	
	}

}
