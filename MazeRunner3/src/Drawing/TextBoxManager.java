package Drawing;

import GameStates.gStateMan;
import Listening.Command;
import Listening.EditorCommand;
import Listening.LoadCommand;
import Listening.MainMenuCommand;
import Listening.PlayCommand;
import Listening.QuitCommand;
import Listening.ResumeCommand;

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

	public static TextBoxManager createMenu(int screenWidth, int screenHeight, String title, String[] commands, gStateMan gsm)
	{
		int titleScale = 10;
		int textScale = 18;
		TextBoxManager res = new TextBoxManager();

		res.AddBox(new TextBox((int)(screenWidth/2),(int)(screenHeight*0.8), 
				screenWidth, screenHeight, 
				titleScale, "Impact", 0, title, 
				0.9f, 0.4f, 0.4f, 1f,
				false)); 

		for(int i = 0; i < commands.length; i++)
		{
			int posY = (int)(screenHeight * (0.8 - 0.7 * (i  + 1) / commands.length));
			
			res.AddBox(new TextBox((int)(screenWidth/2), posY, 
					screenWidth, screenHeight, 
					textScale, "Arial", 0, commands[i], 
					1f, 1f, 1f, 1f, 
					true));

			if(commands[i].equals("Resume")){
				res.setCommand(i + 1, new ResumeCommand(gsm));
			}
			if(commands[i].equals("Main Menu")){
				res.setCommand(i + 1, new MainMenuCommand(gsm));
			}
			if(commands[i].equals("Editor")){
				res.setCommand(i + 1, new EditorCommand(gsm));
			}
			if(commands[i].equals("Play")){
				res.setCommand(i + 1, new PlayCommand(gsm));
			}
			if(commands[i].equals("Load")){
				res.setCommand(i + 1, new LoadCommand(gsm));
			}
			if(commands[i].equals("Quit")){
				res.setCommand(i + 1, new QuitCommand());
			}

		}
		return res;
	}

}
