package Drawing;

import java.util.ArrayList;

import GameStates.gStateMan;
import HighScore.ReadWrite;
import HighScore.Score;
import Listening.*;

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
				if(t.isInBounds(control.getMouseX(), control.getMouseY()) && t.isClickable()){
					t.setColor(0.2f, 0.2f, 1f, 1f);
				}
				else if (t.isClickable()){
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
				false, TextBox.ALIGN_MIDDLE)); 

		for(int i = 0; i < commands.length; i++)
		{
			int posY = (int)(screenHeight * (0.8 - 0.7 * (i  + 1) / commands.length));
			
			res.AddBox(new TextBox((int)(screenWidth/2), posY, 
					screenWidth, screenHeight, 
					textScale, "Arial", 0, commands[i], 
					1f, 1f, 1f, 1f, 
					true, TextBox.ALIGN_MIDDLE));

			if(commands[i].equals("Resume")){
				res.setCommand(i + 1, new ResumeCommand(gsm));
			}
			if(commands[i].equals("Main Menu")){
				res.setCommand(i + 1, new MainMenuCommand(gsm));
			}
			if(commands[i].equals("Editor")){
				res.setCommand(i + 1, new EditorCommand(gsm));
			}
			if(commands[i].equals("Toggle Fullscreen")){
				res.setCommand(i + 1, new FullScreenCommand(gsm));
			}
			if(commands[i].equals("Play")){
				res.setCommand(i + 1, new PlayCommand(gsm));
			}
			if(commands[i].equals("Highscores")){
				res.setCommand(i + 1, new HighscoreCommand(gsm));
			}
			if(commands[i].equals("Options")){
				res.setCommand(i+1, new OptionsCommand(gsm));
			}
			if(commands[i].equals("Load")){
				res.setCommand(i + 1, new LoadCommand(gsm));
			}
			if(commands[i].equals("Back")){
				res.setCommand(i + 1, new BackCommand(gsm));
			}
			if(commands[i].equals("Quit")){
				res.setCommand(i + 1, new QuitCommand());
			}
		}
		return res;
	}
	
	public static TextBoxManager createHighscoreMenu(int screenWidth, int screenHeight, int ranking, gStateMan gsm)
	{
		int titleScale = 12;
		int textScale = 22;
		float[] highscoreColour = {0.3f, 0.7f, 0.3f, 1f};
		float[] mostRecentColour = {1f, 1f, 1f, 1f};
		ReadWrite.readHighscore();
		ArrayList<Score> scores = ReadWrite.highscores;
		TextBoxManager res = new TextBoxManager();
		res.AddBox(new TextBox((int)(screenWidth/2),(int)(screenHeight*0.85), 
				screenWidth, screenHeight, 
				titleScale, "Impact", 0, "High Scores", 
				0.9f, 0.4f, 0.4f, 1f,
				false, TextBox.ALIGN_MIDDLE));
		if(ReadWrite.mostRecentScore != null){
			res.AddBox(new TextBox((int)(screenWidth*0.075), (int)(screenHeight * 0.75), 
				screenWidth, screenHeight, 
				textScale, "Arial", 0, "Most Recent Score:", 
				1.0f, 1.0f, 1.0f, 1f, 
				false, TextBox.ALIGN_LEFT));
			res.addHighScore(screenWidth, screenHeight, screenWidth / 2, (int) (screenHeight * 0.75), 
					ReadWrite.indexOf(ReadWrite.mostRecentScore), mostRecentColour);
		}
		for(int i = 0; i <  5 && i < scores.size(); i++)
		{
			int posY = (int)(screenHeight * (0.75 - 0.65 * (i  + 1) / 7));
			res.addHighScore(screenWidth, screenHeight, 0, posY, i, highscoreColour);
		}
		
		for(int i = 5; i <  10 && i < scores.size(); i++)
		{
			int posY = (int)(screenHeight * (0.75 - 0.65 * (i  - 4) / 7));
			res.addHighScore(screenWidth, screenHeight, screenWidth / 2, posY, i, highscoreColour);
		}
		TextBox mainMenu = new TextBox((int)(screenWidth*0.2), (int)(screenHeight * 0.15), 
				screenWidth, screenHeight, 
				textScale, "Arial", 0, "Main Menu", 
				1f, 1f, 1f, 1f, 
				true, TextBox.ALIGN_MIDDLE);
		mainMenu.setCommand(new MainMenuCommand(gsm));
		TextBox play = new TextBox((int)(screenWidth*0.5), (int)(screenHeight * 0.15), 
				screenWidth, screenHeight, 
				textScale, "Arial", 0, "New Game", 
				1f, 1f, 1f, 1f, 
				true, TextBox.ALIGN_MIDDLE);
		play.setCommand(new PlayCommand(gsm));
		TextBox quit = new TextBox((int)(screenWidth*0.8), (int)(screenHeight * 0.15), 
				screenWidth, screenHeight, 
				textScale, "Arial", 0, "Quit", 
				1f, 1f, 1f, 1f, 
				true, TextBox.ALIGN_MIDDLE);
		quit.setCommand(new QuitCommand());
		res.AddBox(mainMenu);
		res.AddBox(play);
		res.AddBox(quit);
		return res;	
			
	}
	
	private void addHighScore(int screenWidth, int screenHeight, int x, int y, int ranking, float[] colour)
	{
		int textScale = 22;
		ArrayList<Score> scores = ReadWrite.highscores;
		this.AddBox(new TextBox( x + (int)(screenWidth*1/50), y, 
				screenWidth, screenHeight, 
				textScale, "Arial", 0, (ranking + 1) + ".", 
				colour[0], colour[1], colour[2], colour[3],
				false,TextBox.ALIGN_LEFT));
		this.AddBox(new TextBox(x + (int)(screenWidth*1/10), y, 
				screenWidth, screenHeight, 
				textScale, "Arial", 0, scores.get(ranking).getName(), 
				colour[0], colour[1], colour[2], colour[3],  
				false, TextBox.ALIGN_LEFT));
		this.AddBox(new TextBox(x + (int)(screenWidth*3/10), y, 
				screenWidth, screenHeight, 
				textScale, "Arial", 0, Integer.toString((int)scores.get(ranking).getScr()), 
				colour[0], colour[1], colour[2], colour[3], 
				false, TextBox.ALIGN_LEFT));
		
	}

}
