package Listening;

import GameObjects.Editor;

public class SaveCommand implements Command{
	
	private Editor editor;
	
	public SaveCommand(Editor editor)
	{
		this.editor = editor;
	}
	
	public void execute()
	{
		editor.save();
	}
}
