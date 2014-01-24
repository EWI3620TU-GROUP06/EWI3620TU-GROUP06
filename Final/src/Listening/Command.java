package Listening;

/**
 * command is the interface used in all the objects that are used to give the menu's its functionallity
 * the editor command, highscore, mainmenu and playcommand let us switch between the gamestates. the nextlevel 
 * and the loadcommand do switch states to but after loading a new level.
 */

public interface Command {
	
	public void execute();
}
