package EditorModes;

import LevelHandling.Level;

/**
 * Add finish adds a FinishTile MazeObject at the position the mouse is clicked or is dragged to.
 * Previous FinishTiles are removed: there can be only one finish Tile. 
 * 
 * @author Tom Hogervorst
 *
 */

public class AddFinish extends ObjectMode {

	public AddFinish(Level level)
	{
		super(level, ObjectMode.ADD_FINISH);
	}
	
	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.getMaze().clearSelected();
		level.getMaze().select(mazeX, mazeZ);
		level.getMaze().removeBlocks(drawMode);
		level.getMaze().addBlock(drawMode, rotation);
	}

	@Override
	public void mouseReleased()
	{
		level.getMaze().removeBlocks(drawMode);
		level.getMaze().addBlock(drawMode, rotation);
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.getMaze().select(mazeX, mazeZ);
		level.getMaze().removeBlocks(drawMode);
		level.getMaze().addBlock(drawMode, rotation);
	}

}
