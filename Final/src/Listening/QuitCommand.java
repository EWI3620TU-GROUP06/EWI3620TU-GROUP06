package Listening;

public class QuitCommand implements Command {

	public QuitCommand(){
		//Constructor does not need anything;
	}
	
	@Override
	public void execute() {
		System.exit(0);
	}

}
