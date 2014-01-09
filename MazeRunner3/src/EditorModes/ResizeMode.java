package EditorModes;

import LevelHandling.Level;

public class ResizeMode extends EditMode {

	public ResizeMode(Level level) {
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.getMaze().setSize(mazeX + 1, mazeZ + 1);
		level.getMaze().select(mazeX, mazeZ);
	}

	@Override
	public void mouseReleased() {
		//Do Nothing
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.getMaze().setSize(mazeX + 1, mazeZ + 1);
		level.getMaze().select(mazeX, mazeZ);
	}

}
