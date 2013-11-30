package Listening;

import GameObjects.Editor;

public class DrawModeCommand implements Command {
	
	private byte drawMode;
	private Editor editor;
	
	public DrawModeCommand(Editor editor, byte mode)
	{
		drawMode = mode;
		this.editor = editor;
	}
	
	public void execute()
	{
		editor.setDrawMode(drawMode);
	}

}
