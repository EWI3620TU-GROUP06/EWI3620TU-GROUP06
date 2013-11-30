package Listening;

import MainGame.Maze;

public class ResizeCommand implements Command{

	Maze maze;
	int sign;
	
	public ResizeCommand(Maze maze, int sign)
	{
		this.maze = maze;
		this.sign = sign;
	}
	
	public void execute()
	{
		maze.addToSize(sign);
	}
	
}
