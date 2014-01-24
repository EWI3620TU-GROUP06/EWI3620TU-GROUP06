package Listening;

import EditorModes.AddRotating;
import GameObjects.Editor;
import LevelHandling.Maze;
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
			if(!Maze.customs.contains(custom)){
				Maze.customs.add(custom);
			}
			editor.setEditMode(new AddRotating(editor.getLevel(), (byte)(-Maze.customs.indexOf(custom) - 1)));

		}
	}
}
