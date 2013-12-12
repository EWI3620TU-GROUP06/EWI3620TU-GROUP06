package Drawing;

import java.util.ArrayList;

import Listening.Command;
import Listening.Control;

public abstract class ClickBoxManager {

	protected ArrayList<ClickBox> Boxes;
	protected Control control;
	
	public ClickBoxManager(){
		Boxes = new ArrayList<ClickBox>();
	}
	
	public abstract void AddBox(ClickBox box);
	
	public void removeBox(TextBox box){
		Boxes.remove(box);
	}
	
	public void setControl(Control control){
		this.control = control;
		for(ClickBox c : Boxes)
		{
			if(c instanceof TextEditBox)
			{
				TextEditBox t = (TextEditBox) c;
				t.setControl(control);
			}
		}
	}
	
	public void setCommand(int i, Command command){
		Boxes.get(i).setCommand(command);
	}
	
	public abstract void update();
	
	public abstract void reshape(int screenWidth, int screenHeight);
	
}
