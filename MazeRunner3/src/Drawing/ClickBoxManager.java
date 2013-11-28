package Drawing;

import java.util.ArrayList;

import Listening.Command;
import Listening.Control;

public class ClickBoxManager {
	
	private ArrayList<ClickBox> Boxes;
	private Control control;
	
	public ClickBoxManager(){
		Boxes = new ArrayList<ClickBox>();
	}
	
	public void AddBox(ClickBox box){
		Boxes.add(box);
	}
	
	public void setControl(Control control){
		this.control = control;
	}
	
	public void update(){
		if(control != null){
			control.update();
			if(control.getClicked() != 0){
				for(ClickBox a: Boxes){
					if(a.isClickable() && a.isInBounds(control.getMouseX(), control.getMouseY())){
						System.out.println("Button Clicked");
						a.execute();
					}
				}
			}
		}
	}
	
	public void reshape(int screenWidth, int screenHeight){
		for(ClickBox a: Boxes){
			a.reshape(screenWidth, screenHeight);
		}
	}
	
	public void removeBox(ClickBox box){
		Boxes.remove(box);
	}
	
	public void drawAllText(){
		for (ClickBox a: Boxes){
			a.drawText();
		}
	}
	
	public void setCommand(int i,Command command){
		Boxes.get(i).setCommand(command);
	}
	
}
