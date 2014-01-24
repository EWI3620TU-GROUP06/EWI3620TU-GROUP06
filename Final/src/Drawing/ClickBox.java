package Drawing;

import Listening.Command;

/**
 * the class clickbox is an abstract class for all the clickboxes. the clickboxes have options to be clickable
 * which means they can execute an command. 
 *
 */
public abstract class ClickBox {

	protected int[] location;

	public static int screenWidth;
	public static int screenHeight;
	protected int[] Bounds;
	protected boolean clickable;
	protected Command command;
	
	public ClickBox(int x, int y, int screenWidth, int screenHeight, int boundX, int boundY, boolean clickable){
		ClickBox.screenWidth = screenWidth;
		ClickBox.screenHeight = screenHeight;
		this.location = new int[]{x, y};
		this.clickable = clickable;
		this.Bounds = new int[]{location[0], location[0] + boundX, location[1] + boundY, location[1]};
	}
	
	public int[] getBounds(){
		return Bounds;
	}
	
	public void setBounds(int l, int r, int up, int low){
		this.Bounds = new int[]{l,r,up,low};
	}

	public int[] getLocation(){
		return location;

	}

	public void setLocation(int x, int y){
		location = new int[]{x,y};
	}

	public boolean isClickable(){
		return clickable;
	}

	public void setClickable(boolean click){
		this.clickable = click;
	}
	
	/*
	 * in the method setCommand the command of the clickbox is set
	 */
	public void setCommand(Command command){
		this.command = command;
	}

	public void execute(){
		command.execute();
	}
	
	/*
	 * the method isInBounds is used to determine if the mouse is clicked inside a clickbox
	 */
	public boolean isInBounds(int x, int y){

		if(x > Bounds[0] && x < Bounds[1]
				&& (screenHeight - y) < Bounds[2] && (screenHeight - y) > Bounds[3] ){
			return true;
		}
		else{
			return false;
		}
	}
	
	public abstract void reshape(int screenWitdh, int screenHeight);

}
