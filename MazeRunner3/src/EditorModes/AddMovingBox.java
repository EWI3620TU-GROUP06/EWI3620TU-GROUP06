package EditorModes;

import javax.vecmath.Vector3d;

import GameObjects.MoveableBox;
import LevelHandling.Level;
import LevelHandling.Maze;

public class AddMovingBox extends EditMode {

	public AddMovingBox(Level level) {
		super(level);
		
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {

	}

	@Override
	public void mouseReleased() {

	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.addMoveableBox(new MoveableBox(new Vector3d(mazeX * Maze.SQUARE_SIZE, 0, mazeZ * Maze.SQUARE_SIZE), 
				Maze.SQUARE_SIZE, Maze.SQUARE_SIZE));
	}

}
