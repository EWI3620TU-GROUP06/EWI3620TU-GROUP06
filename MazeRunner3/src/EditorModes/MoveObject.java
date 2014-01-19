package EditorModes;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import LevelHandling.Level;
import LevelHandling.Maze;
import MazeObjects.MazeObject;

/**
 * MoveObject moves the MazeObject that is selected when the mouse is clicked, to the position 
 * of the dragged mouse. A Floor MazeObject is added on the moved objects original place.
 * When the mouse is released, the object is fixed in place.
 * and 
 * @author Tom2
 *
 */

public class MoveObject extends EditMode {

	MazeObject selected;
	int pressedX, pressedZ;

	public MoveObject(Level level)
	{
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		if(mazeX != pressedX || mazeZ != pressedZ)
		{
			level.getMaze().removeTop(pressedX, pressedZ);

			level.getMaze().clearSelected();
			level.getMaze().select(mazeX,  mazeZ);
			Vector3f pos = (Vector3f)selected.getPos();
			level.getMaze().set(selected.translate(-pos.x, -pos.y, -pos.z), mazeX, mazeZ);
			pressedX = mazeX;
			pressedZ = mazeZ;
		}
	}

	@Override
	public void mouseReleased() {
		//do nothing
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.getMaze().select(mazeX, mazeZ);
		ArrayList<MazeObject> stack =	level.getMaze().get(mazeX, mazeZ);
		selected = stack.get(stack.size() - 1);
		pressedX = mazeX;
		pressedZ = mazeZ;
	}

}
