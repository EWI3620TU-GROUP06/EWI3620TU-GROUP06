package Listening;

import EditorModes.AddStatic;
import EditorModes.EditMode;
import GameObjects.Editor;

public class EditModeCommand implements Command {
	
	private EditMode editMode;
	private Editor editor;
	
	public EditModeCommand(Editor editor, EditMode mode)
	{
		editMode = mode;
		this.editor = editor;
	}
	
	public void execute()
	{
		editMode.setLevel(editor.getLevel());
		if(editMode instanceof AddStatic)
			System.out.println("Add static ");
		editor.setEditMode(editMode);
	}
	
}
