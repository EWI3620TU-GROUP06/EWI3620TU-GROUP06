package Drawing;

public class TextBoxManager extends ClickBoxManager{

	public void AddBox(ClickBox box)
	{
		if(box instanceof TextBox)
			Boxes.add(box);
	}

	public void update(){
		control.update();
		if(control.getClicked() != 0){
			for(ClickBox a: Boxes){
				if(a.isClickable() && a.isInBounds(control.getMouseX(), control.getMouseY())){
					a.execute();
				}
			}
		}
		if(control.getClicked() == 0){
			for(int i = 1; i < Boxes.size(); i++){
				TextBox t = (TextBox) Boxes.get(i);
				if(t.isInBounds(control.getMouseX(), control.getMouseY())){
					t.setColor(0.2f, 0.2f, 1f, 1f);
				}
				else{
					t.setColor(1f, 1f, 1f, 1f);
				}

			}
		}
	}

	public void reshape(int screenWidth, int screenHeight){
		for(ClickBox a: Boxes){
			a.reshape(screenWidth, screenHeight);
		}
	}



	public void drawAllText(){
		for (ClickBox a: Boxes){
			TextBox t = (TextBox) a;
			t.drawText();

		}
	}



}
