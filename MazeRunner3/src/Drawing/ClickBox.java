package Drawing;

import java.awt.Font;

import javax.vecmath.Vector2f;

import Listening.Command;

import com.sun.opengl.util.j2d.TextRenderer;

public class ClickBox {
	
	private Vector2f location;
	private float[] Bounds;
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

	public ClickBox(float x, float y, float l, float r, float up, float low,
			int screenWidth, int screenHeight, int textScale, String Font, int fontStyle, String Text, 
			float red, float green, float blue, float alpha,
			boolean clickable){
		this.color = new float[]{red, green, blue, alpha};
		this.textScale = textScale;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.location = new Vector2f(x,y);
		this.Bounds = new float[]{l,r,up,low};
		this.Font = Font;
		renderer = new TextRenderer(new Font(Font, fontStyle, this.screenWidth/textScale));
		this.Text = Text;
		this.clickable = clickable;
	}
	
	public Vector2f getLocation(){
		return location;
	}
	
	public float[] getBounds(){
		return Bounds;
	}
	
	public void setLocation(float x, float y){
		location = new Vector2f(x,y);
	}
	
	public void setBounds(float l, float r, float up, float low){
		this.Bounds = new float[]{l,r,up,low};
	}
	
	public void reshape(int screenWidth, int screenHeight){
		int oldWidth = this.screenWidth;
		int oldHeight = this.screenHeight;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		setBounds(Bounds[0]*(float)(this.screenWidth)/(float)(oldWidth),
				Bounds[1]*(float)(this.screenWidth)/(float)(oldWidth),
				Bounds[2]*(float)(this.screenHeight)/(float)(oldHeight),
				Bounds[3]*(float)(this.screenHeight)/(float)(oldHeight));
		setLocation(this.getLocation().x*(float)(this.screenWidth)/(float)(oldWidth),
				this.getLocation().y*(float)(this.screenHeight)/(float)(oldHeight));
		renderer = new TextRenderer(new Font(Font, fontStyle, this.screenWidth/textScale));
	}
	
	public void drawText(){
		renderer.beginRendering(this.screenWidth, this.screenHeight);
		renderer.setColor(color[0], color[1], color[2], color[3]);
		float[] coords = new float[2];
		location.get(coords);
		renderer.draw(Text,(int) coords[0],(int) coords[1]);
		renderer.flush();
		renderer.endRendering();
	}
	
	public boolean isClickable(){
		return clickable;
	}
	
	public void setClickable(){
		this.clickable = true;
	}
	
	public void setNotClickable(){
		this.clickable = false;
	}
	
	public void setCommand(Command command){
		this.command = command;
	}
	
	public boolean isInBounds(int x, int y){
		
		if((float)( x ) > Bounds[0] && (float)( x ) < Bounds[1]
				&& (float)(screenHeight - y) < Bounds[2] && (float)(screenHeight - y) > Bounds[3] ){
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
