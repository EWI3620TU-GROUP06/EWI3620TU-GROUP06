package Drawing;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import Listening.Command;

import com.sun.opengl.util.j2d.TextRenderer;

public class ClickBox {
	
	private int[] location;
	private int[] Bounds;
	private int screenWidth;
	private int screenHeight;
	private TextRenderer renderer;
	private int textScale;
	private String Font;
	private int fontStyle; // 0 = plain; 1 = Bold; 2 = italic
	private String Text;
	private boolean clickable;
	private float[] color;
	private Command command;

	public ClickBox(int x, int y, int screenWidth, int screenHeight, 
			int textScale, String Font, int fontStyle, String Text, 
			float red, float green, float blue, float alpha,
			boolean clickable){
		this.color = new float[]{red, green, blue, alpha};
		this.textScale = textScale;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.Font = Font;
		renderer = new TextRenderer(new Font(Font, fontStyle, this.screenWidth/textScale));
		this.Text = Text;
		Rectangle2D temp = renderer.getBounds(Text);
		this.location = new int[]{x - (int)(temp.getWidth()/2),y};
		this.Bounds = new int[]{location[0]+(int)temp.getX(), 
				location[0]+(int)temp.getX()+(int)temp.getWidth(), 
				location[1]+(int)temp.getHeight(), 
				location[1]};
		this.clickable = clickable;
	}
	
	public int[] getLocation(){
		return location;
	}
	
	public int[] getBounds(){
		return Bounds;
	}
	
	public void setLocation(int x, int y){
		location = new int[]{x,y};
	}
	
	public void setBounds(int l, int r, int up, int low){
		this.Bounds = new int[]{l,r,up,low};
	}
	
	public void reshape(int screenWidth, int screenHeight){
		int oldWidth = this.screenWidth;
		int oldHeight = this.screenHeight;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		setLocation((int)(this.getLocation()[0]*(float)(this.screenWidth)/(float)(oldWidth)),
				(int)(this.getLocation()[1]*(float)(this.screenHeight)/(float)(oldHeight)));
		renderer = new TextRenderer(new Font(Font, fontStyle, this.screenWidth/textScale));
		Rectangle2D temp = renderer.getBounds(Text);
		temp = temp.getBounds2D();
		setBounds(this.getLocation()[0]+(int)temp.getX(), 
				this.getLocation()[0]+(int)temp.getX()+(int)temp.getWidth(), 
				this.getLocation()[1]+(int)temp.getHeight(), 
				this.getLocation()[1]);
	}
	
	public void drawText(){
		renderer.beginRendering(this.screenWidth, this.screenHeight);
		renderer.setColor(color[0], color[1], color[2], color[3]);
		renderer.draw(Text,location[0],location[1]);
		renderer.flush();
		renderer.endRendering();
	}
	
	public boolean isClickable(){
		return clickable;
	}
	
	public void setClickable(){
		this.clickable = true;
	}
	
	public void setColor(float red, float green, float blue, float alpha){
		this.color = new float[]{red,green,blue,alpha};
	}
	
	public void setNotClickable(){
		this.clickable = false;
	}
	
	public void setCommand(Command command){
		this.command = command;
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
	
	public void execute(){
		command.execute();
	}
}
