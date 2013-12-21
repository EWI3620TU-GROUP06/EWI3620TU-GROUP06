package EditorModes;

import LevelHandling.Level;

public abstract class AddMode extends EditMode{
	
	public static final byte ADD_FLOOR = 0;
	public static final byte ADD_BOX = 1;
	public static final byte ADD_LOW_BOX = 2;
	public static final byte ADD_START = 3;
	public static final byte ADD_FINISH = 4;
	public static final byte ADD_RAMP = 5;
	public static final byte ADD_LOW_RAMP = 6;
	
	protected byte drawMode;
	protected int rotation;
	
	public AddMode(Level level, Byte drawMode)
	{
		super(level);
		this.drawMode = drawMode;
		rotation = 0;
	}

}
