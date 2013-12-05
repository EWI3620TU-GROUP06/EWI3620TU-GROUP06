package Listening;

import GameObjects.Editor;
import MazeObjects.CustomMazeObject;

public class CustomCommand implements Command {
	
	private Editor editor;
	
	public CustomCommand(Editor editor)
	{
		this.editor = editor;
	}
	
	public void execute() {
		CustomMazeObject custom = Editor.readMazeObject();
		if(custom != null){
			editor.addObject(custom);
		}
		
	}

}
