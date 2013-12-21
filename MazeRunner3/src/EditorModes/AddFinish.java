package EditorModes;

import MainGame.Level;

public class AddFinish extends AddMode {

	public AddFinish(Level level)
	{
		super(level, AddMode.ADD_FINISH);
	}
	
	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.getMaze().clearSelected();
		level.getMaze().select(mazeX, mazeZ);
		level.getMaze().setFinish();
	}

	@Override
	public void mouseReleased()
	{
		level.getMaze().setFinish();
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.getMaze().select(mazeX, mazeZ);
		level.getMaze().setFinish();
	}

}
