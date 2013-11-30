package Listening;

import GameObjects.Editor;
import MainGame.Maze;

public class OpenCommand implements Command{
	
	private Editor editor;
	
	public OpenCommand(Editor editor)
	{
		this.editor = editor;
	}
	
	public void execute() {
		Maze maze = Editor.readMaze();
		if(maze != null)
			editor.setMaze(maze);
		
	}
}
