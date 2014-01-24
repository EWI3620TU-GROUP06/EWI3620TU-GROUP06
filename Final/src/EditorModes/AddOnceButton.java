package EditorModes;

import LevelHandling.Level;

public class AddOnceButton extends EditMode {
	
	public AddOnceButton(Level level) {
		super(level);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		level.setButton(mazeX, mazeZ, 1);
	}

	@Override
	public void mouseReleased() {
		
	}

	@Override
	public void mousePressed(int mazeX, int mazeZ) {
		level.setButton(mazeX, mazeZ, 1);
	}

}
