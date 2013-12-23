package EditorModes;

import javax.vecmath.Vector3f;

import LevelHandling.Level;
import LevelHandling.Maze;
import MazeObjects.Floor;
import MazeObjects.MazeObject;

public class MoveObject extends EditMode {
	
	MazeObject selected;
	MazeObject previous;
	
	public MoveObject(Level level)
	{
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.getMaze().set(previous);
		previous = level.getMaze().get(mazeX, mazeZ);
		level.getMaze().clearSelected();
		level.getMaze().select(mazeX,  mazeZ);
		Vector3f pos = (Vector3f)selected.getPos().clone();
		pos.sub(new Vector3f(mazeX*Maze.SQUARE_SIZE, 0, mazeZ*Maze.SQUARE_SIZE), pos);
		level.getMaze().set(selected.translate(pos.x, pos.y, pos.z));
	}

	@Override
	public void mouseReleased() {
		//do nothing
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.getMaze().select(mazeX, mazeZ);
		selected = level.getMaze().get(mazeX,  mazeZ);
		previous = new Floor(Maze.SQUARE_SIZE, mazeX * Maze.SQUARE_SIZE, mazeZ * Maze.SQUARE_SIZE);
	}

}
