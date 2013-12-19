package Listening;

import GameObjects.Editor;
import MainGame.Level;

public class OpenCommand implements Command{
	
	private Editor editor;
	
	public OpenCommand(Editor editor)
	{
		this.editor = editor;
	}
	
	public void execute() {
		Level level = Editor.readLevel();
		if(level != null)
			editor.setLevel(level);
		
	}
}
