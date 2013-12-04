package Listening;

import GameObjects.Editor;
import MazeObjects.MazeObject;

public class CustomCommand implements Command {
	
private Editor editor;
	
	public CustomCommand(Editor editor)
	{
		this.editor = editor;
	}
	
	public void execute() {
		MazeObject custom = Editor.readMazeObject();
		if(custom != null)
			editor.addObject(custom);
		
	}

}
