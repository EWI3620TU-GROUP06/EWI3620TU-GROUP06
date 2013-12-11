package Drawing;

import java.util.ArrayList;

import GameStates.gStateMan;
import HighScore.ReadWrite;
import HighScore.Score;
import HighScore.SqlReadWrite;
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

	public void drawAllText(int deltaTime){
		for (ClickBox a: Boxes){
			TextBox t = (TextBox) a;
			t.drawText(deltaTime);

		}
	}

	public static TextBoxManager createMenu(int screenWidth, int screenHeight, String title, String[] commands, gStateMan gsm)
	{
		int titleScale = 10;
		int textScale = 18;
		TextBoxManager res = new TextBoxManager();
		
		res.AddBox(TextBox.createTitle(0.5f, 0.8f, 
				screenWidth, screenHeight, titleScale, title));

		for(int i = 0; i < commands.length; i++)
		{
			float posY = (float) (0.8 - 0.7 * (i  + 1) / commands.length);
			
			res.AddBox(TextBox.createMenuBox(0.5f, posY, 
					screenWidth, screenHeight, textScale, commands[i]));

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
		SqlReadWrite.Read();
		ArrayList<Score> scores = SqlReadWrite.highscores;
		TextBoxManager res = new TextBoxManager();
		res.AddBox(TextBox.createTitle(0.5f, 0.85f, 
				screenWidth, screenHeight, titleScale, "High Scores"));
		if(SqlReadWrite.mostRecentScore != null){
			res.AddBox(TextBox.createHighscoreBox(0.075f, 0.75f, 
				screenWidth, screenHeight, textScale, "Most Recent Score:", mostRecentColour));
			res.addHighScore(screenWidth, screenHeight, 0.5f, 0.75f, 
					scores.indexOf(SqlReadWrite.mostRecentScore), mostRecentColour);
		}
		for(int i = 0; i <  5 && i < scores.size(); i++){
			float posY = (float)(0.75 - 0.65 * (i  + 1) / 7);
			res.addHighScore(screenWidth, screenHeight, 0, posY, i, highscoreColour);
		}
		
		for(int i = 5; i <  10 && i < scores.size(); i++){
			float posY =  (float)(0.75 - 0.65 * (i  - 4) / 7);
			res.addHighScore(screenWidth, screenHeight, 0.5f, posY, i, highscoreColour);
		}
		TextBox mainMenu = TextBox.createMenuBox(0.2f,  0.15f, 
				screenWidth, screenHeight, textScale, "Main Menu");
		mainMenu.setCommand(new MainMenuCommand(gsm));
		TextBox play = TextBox.createMenuBox(0.5f, 0.15f, 
				screenWidth, screenHeight, textScale, "New Game");
		play.setCommand(new PlayCommand(gsm));
		TextBox quit = TextBox.createMenuBox(0.8f, 0.15f, 
				screenWidth, screenHeight, textScale, "Quit");
		quit.setCommand(new QuitCommand());
		res.AddBox(mainMenu);
		res.AddBox(play);
		res.AddBox(quit);
		return res;	
			
	}
	
	private void addHighScore(int screenWidth, int screenHeight, float x, float y, int ranking, float[] colour)
	{
		int textScale = 22;
		ArrayList<Score> scores = SqlReadWrite.highscores;
		this.AddBox(TextBox.createHighscoreBox(x + 0.02f, y, 
				screenWidth, screenHeight, textScale, (ranking + 1) + ".", colour));
		this.AddBox(TextBox.createHighscoreBox(x + 0.1f, y, 
				screenWidth, screenHeight, textScale, scores.get(ranking).getName(), colour));
		this.AddBox(TextBox.createHighscoreBox(x + 0.3f, y, 
				screenWidth, screenHeight, textScale, Integer.toString((int)scores.get(ranking).getScr()), colour));		
	}
	
	public String getText()
	{
		for(ClickBox t : Boxes)
		{
			if(t instanceof TextEditBox)
				return ((TextEditBox)t).getText();
		}
		return null;
	}

}
