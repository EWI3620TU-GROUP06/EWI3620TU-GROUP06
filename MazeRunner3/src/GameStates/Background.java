package GameStates;

import java.awt.*;
import java.awt.image.*;

import javax.imageio.*;
import javax.media.opengl.GLAutoDrawable;

import Main.Game;

public class Background {
	
	private BufferedImage img;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private double moveScale;
	
	public Background(String path, double ms){
		try{
			img = ImageIO.read(getClass().getResourceAsStream(path)); 
			moveScale = ms;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y){
		this.x = (x*moveScale) % Game.getScreenWidth(); 
		this.y = (y*moveScale) % Game.getScreenHeight(); 
	}
	
	public void setMove(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}

	public void update(){
		x += dx;
		y += dy;
	}
	
	public void draw(GLAutoDrawable g){
		
//		g.drawImage(img,(int) x, (int) y, null);
//		
//		if (x < 0){
//			g.drawImage(img,(int)x + Game.getScreenWidth(), (int)y, null);
//		}
//		if (x > 0){
//			g.drawImage(img,(int)x- Game.getScreenWidth(), (int)y, null);
//		}
		
	}
}
