package EditorModes;

import MainGame.Level;

public class AddStart extends AddMode {
	
	private int pressedX, pressedZ;
	
	public AddStart(Level level){
		super(level, AddMode.ADD_START);
	}

	@Override
	public void mouseDragged(int mazeX, int mazeZ) {
		int dX = mazeX - pressedX;
		int dZ = mazeZ - pressedZ;
		double unrounded = Math.atan2(dX, -dZ) / (0.5 * Math.PI);
		rotation = 90 * (int)(Math.round(unrounded));
		level.getMaze().setStart(rotation);
	}
	
	@Override
	public void mousePressed(int mazeX, int mazeZ){
		pressedX = mazeX;
		pressedZ = mazeZ;
		level.getMaze().select(mazeX, mazeZ);
		level.getMaze().setStart(rotation);
	}
	
	@Override
	public void mouseReleased()
	{
		level.getMaze().setStart(rotation);
	}

}
