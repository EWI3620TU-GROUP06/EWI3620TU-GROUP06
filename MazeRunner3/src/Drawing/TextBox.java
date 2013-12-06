package Drawing;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import com.sun.opengl.util.j2d.TextRenderer;

public class TextBox extends ClickBox {

	protected TextRenderer renderer;
	protected int textScale;
	protected String Font;
	protected int fontStyle; // 0 = plain; 1 = Bold; 2 = italic
	protected String Text;
	public static final byte ALIGN_MIDDLE = 0;
	public static final byte ALIGN_LEFT = 1;
	public static final byte ALIGN_RIGHT = 2;

	protected float[] color;

	public TextBox(int x, int y, int screenWidth, int screenHeight, 
			int textScale, String Font, int fontStyle, String Text, 
			float red, float green, float blue, float alpha,
			boolean clickable, byte alignment){
		super (0, 0, screenWidth, screenHeight, 0, 0, clickable);
		this.color = new float[]{red, green, blue, alpha};
		this.textScale = textScale;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.Font = Font;
		renderer = new TextRenderer(new Font(Font, fontStyle, this.screenWidth/textScale));
		this.Text = Text;
		Rectangle2D temp = renderer.getBounds(Text);
		if(alignment == ALIGN_MIDDLE)
		{
			this.location = new int[]{x - (int)(temp.getWidth()/2),y};
			this.Bounds = new int[]{location[0]+(int)temp.getX(), 
					location[0]+(int)temp.getX()+(int)temp.getWidth(), 
					location[1]+(int)temp.getHeight(), 
					location[1]};
		}
		else if(alignment == ALIGN_RIGHT)
		{
			this.location = new int[]{x - (int)(temp.getWidth()),y};
			this.Bounds = new int[]{location[0]+(int)temp.getX(), 
					location[0]+(int)temp.getX()+(int)temp.getWidth(), 
					location[1]+(int)temp.getHeight(), 
					location[1]};
		}
		else
		{
			this.location = new int[]{x, y};
			this.Bounds = new int[]{location[0]+(int)temp.getX(), 
					location[0]+(int)temp.getX()+(int)temp.getWidth(), 
					location[1]+(int)temp.getHeight(), 
					location[1]};
		}
		this.clickable = clickable;
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
		setBounds(this.getLocation()[0]+(int)temp.getX(), 
				this.getLocation()[0]+(int)temp.getX()+(int)temp.getWidth(), 
				this.getLocation()[1]+(int)temp.getHeight(), 
				this.getLocation()[1]);
	}

	public void drawText(int deltaTime){
		renderer.beginRendering(this.screenWidth, this.screenHeight);
		renderer.setColor(color[0], color[1], color[2], color[3]);
		renderer.draw(Text,location[0],location[1]);
		renderer.flush();
		renderer.endRendering();
	}

	public void setColor(float red, float green, float blue, float alpha){
		this.color = new float[]{red,green,blue,alpha};
	}
	
	public void setText(String text)
	{
		this.Text = text;
	}
	
	public static TextBox createTitle(int x, int y, int screenWidth, int screenHeight, int titleScale, String title)
	{
		return new TextBox(x, y, screenWidth, screenHeight, titleScale, "Impact", 0, title, 
				0.9f, 0.4f, 0.4f, 1f, false, ALIGN_MIDDLE); 
	}
	
	public static TextBox createMenuBox(int x, int y, int screenWidth, int screenHeight, int textScale, String caption)
	{
		return new TextBox(x, y, screenWidth, screenHeight, textScale, "Arial", 0, caption, 
				1f, 1f, 1f, 1f, true, TextBox.ALIGN_MIDDLE);
	}
	
	public static TextBox createHighscoreBox(int x, int y, int screenWidth, int screenHeight, int textScale, 
			String caption, float[] colour)
	{
		return new TextBox( x, y, screenWidth, screenHeight, textScale, "Arial", 0, caption, 
				colour[0], colour[1], colour[2], colour[3], false,TextBox.ALIGN_LEFT);
	}

}
