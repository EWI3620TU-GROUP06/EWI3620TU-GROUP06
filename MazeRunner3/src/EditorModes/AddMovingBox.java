package EditorModes;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import GameObjects.MoveableBox;
import LevelHandling.Level;
import LevelHandling.Maze;

/**
 * AddMovingBox adds a movable box on the tile de mouse was clicked. By dragging, the path of the 
 * box can be selected: the box will move to all selected tiles in order and then back,
 * with a speed of 1 second per tile.
 * 
 * @author Tom Hogervorst
 *
 */

public class AddMovingBox extends EditMode {
	
	private int pressedX, pressedZ;
	private int num;

	public AddMovingBox(Level level) {
		super(level);
		
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		if(pressedX != mazeX || pressedZ != mazeZ)
		{
			int dX = (mazeX - pressedX) * Maze.SQUARE_SIZE;
			int dZ = (mazeZ - pressedZ) * Maze.SQUARE_SIZE;
			level.getMoveableBoxes().get(num).addToPath(1000, new Vector3f(dX, 0, dZ));
			level.getMaze().select(mazeX, mazeZ);
		}
		pressedX = mazeX;
		pressedZ = mazeZ;
	}

	@Override
	public void mouseReleased() {
		level.getMoveableBoxes().get(num).addReversePath();
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.addMoveableBox(new MoveableBox(new Vector3d(mazeX * Maze.SQUARE_SIZE, 0, mazeZ * Maze.SQUARE_SIZE), 
				Maze.SQUARE_SIZE, Maze.SQUARE_SIZE));
		num = level.getMoveableBoxes().size() - 1;
		pressedX = mazeX;
		pressedZ = mazeZ;
	}

}
