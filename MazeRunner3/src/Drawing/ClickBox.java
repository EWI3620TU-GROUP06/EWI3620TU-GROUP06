package Drawing;

import Listening.Command;

public abstract class ClickBox {

	protected int[] location;

	protected int screenWidth;
	protected int screenHeight;
	protected int[] Bounds;
	protected boolean clickable;
	protected Command command;
	
	public ClickBox(int x, int y, int screenWidth, int screenHeight, int boundX, int boundY, boolean clickable){
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
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

	public void setCommand(Command command){
		this.command = command;
	}

	public void execute(){
		command.execute();
	}
	
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
