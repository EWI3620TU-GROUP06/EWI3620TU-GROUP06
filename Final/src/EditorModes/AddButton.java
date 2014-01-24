package EditorModes;

import LevelHandling.Level;

public class AddButton  extends EditMode
{

	public AddButton(Level level) {
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.setButton(mazeX, mazeZ, 0);
	}

	@Override
	public void mouseReleased() {
		
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.setButton(mazeX, mazeZ, 0);
	}

}
