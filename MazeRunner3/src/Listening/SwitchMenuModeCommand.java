package Listening;

import Drawing.EditBoxManager;

public class SwitchMenuModeCommand implements Command {
	
	EditBoxManager editbxman;
	
	public SwitchMenuModeCommand(EditBoxManager e)
	{
		editbxman = e;
	}

	@Override
	public void execute() {
		editbxman.toggleMenuMode();
	}

}
