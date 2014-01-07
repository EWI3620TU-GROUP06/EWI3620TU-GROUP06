package EditorModes;

import javax.vecmath.Vector3d;

import GameObjects.PowerUp;
import LevelHandling.Level;
import LevelHandling.Maze;

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
		level.addPowerUp(new PowerUp(new Vector3d(mazeX * Maze.SQUARE_SIZE, 2.5, mazeZ * Maze.SQUARE_SIZE), type));	
	}

}
