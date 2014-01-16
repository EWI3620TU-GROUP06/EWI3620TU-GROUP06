package EditorModes;

import java.util.Calendar;

import javax.vecmath.Vector3f;

import LevelHandling.Level;
import LevelHandling.Maze;

/**
 * AddMovingBox adds a movable box on the tile de mouse was clicked. By dragging, the path of the 
 * box can be selected: the box will move to all selected tiles in order,
 * the speed depents on the speed with which the tiles were dragged.
 * 
 * @author Tom Hogervorst
 *
 */

public class AddMovingBox extends EditMode {
	
	private int pressedX, pressedZ;
	private int num;
	private long previousTime;

	public AddMovingBox(Level level) {
		super(level);
		
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		if(pressedX != mazeX || pressedZ != mazeZ)
		{
			Calendar now = Calendar.getInstance();		
			long currentTime = now.getTimeInMillis();
			float deltaTime = (float) (currentTime - previousTime);
			previousTime = currentTime;
			level.getMoveableBoxes().get(num).addToPath((int)deltaTime, new Vector3f(
					mazeX * Maze.SQUARE_SIZE, 0, mazeZ*Maze.SQUARE_SIZE));
			level.getMaze().select(mazeX, mazeZ);
			
		}
		pressedX = mazeX;
		pressedZ = mazeZ;
	}

	@Override
	public void mouseReleased() {
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.addMoveableBox(mazeX, mazeZ);
		num = level.getMoveableBoxes().size() - 1;
		pressedX = mazeX;
		pressedZ = mazeZ;
		Calendar now = Calendar.getInstance();		
		previousTime = now.getTimeInMillis();
	}

}
