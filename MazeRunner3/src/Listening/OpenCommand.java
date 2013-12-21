package Listening;

import EditorModes.AddMode;
import EditorModes.AddStatic;
import GameObjects.Editor;
import LevelHandling.Level;

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
		editor.setEditMode(new AddStatic(editor.getLevel(), AddMode.ADD_FLOOR));
	}
}
