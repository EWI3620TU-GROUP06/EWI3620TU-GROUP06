package GameStates;

import java.awt.*;
import java.awt.event.KeyEvent;

import MainGame.MazeRunner;

public class MenuState extends GameState {
	
	private Background background;
	private int currentChoice;
	private String[] options = {
			"Continue",
			"New Game",
			"Quit",
			};
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(gStateMan g){
		this.gsm = g;
		
		try{
			background = new Background("backgrounds/mainmenu.jpg",1);
			titleColor = new Color(255,255,255);
			titleFont = new Font("Calibri",Font.PLAIN,30);
			
			font = new Font("Arial",Font.PLAIN,11);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		// Kinda does nothing, in this case.
	}

	@Override
	public void update() {
		background.update();
	}

	@Override
	public void draw(Graphics2D g) {
		//draw the damn background
		background.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Mazerunnert", 80, 70);
		
		//draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++){
			if(i == currentChoice){
				g.setColor(Color.BLACK);
			}
			else{
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 145, 140 + i*15);
		}
	}
	
	public void select(){
		if(currentChoice == 0){
			//Continue
		}
		if(currentChoice == 1){
			new MazeRunner();
		}
		if(currentChoice == 2){
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}

	@Override
	public void keyReleased(int k) {
		// Do nothing

	}

}
