package Listening;

import Drawing.EditBoxManager;

public class SwitchMenuModeCommand implements Command {
	
	EditBoxManager editbxman;
	byte mode;
	
	public SwitchMenuModeCommand(EditBoxManager e, byte mode)
	{
		editbxman = e;
		this.mode = mode;
	}

	@Override
	public void execute() {
		editbxman.toggleMenuMode(mode);
	}

}
