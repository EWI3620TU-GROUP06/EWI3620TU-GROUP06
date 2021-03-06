package Drawing;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;

import com.sun.opengl.util.j2d.TextRenderer;

/**
 * the textbox class is used to render all the texts used in the menu's and all the other differnt states and gives
 * us the opportunity for instance to change the alignment of each text. 
 *
 */

public class TextBox extends ClickBox {

	protected TextRenderer renderer;
	protected int textScale;
	protected String Font;
	protected int fontStyle; // 0 = plain; 1 = Bold; 2 = italic
	protected String Text;
	public static final byte ALIGN_MIDDLE = 0;
	public static final byte ALIGN_LEFT = 1;
	public static final byte ALIGN_RIGHT = 2;
	protected byte alignment;
	protected float relativePos[];
	private boolean changable;
	protected float[] color;

	public TextBox(float x, float y, int screenWidth, int screenHeight, 
			int textScale, String Font, int fontStyle, String Text, 
			float red, float green, float blue, float alpha,
			boolean clickable, byte alignment, boolean changable){
		super (0, 0, screenWidth, screenHeight, 0, 0, clickable);
		this.color = new float[]{red, green, blue, alpha};
		this.textScale = textScale;
		this.Font = Font;
		
		//This section provides custom fonts
		GraphicsEnvironment ge = GraphicsEnvironment
	            .getLocalGraphicsEnvironment(); 
		Font[] systemfonts = ge.getAllFonts();
		boolean contains = false;
		for(Font f: systemfonts){
			if(f.getFontName().equalsIgnoreCase(Font)){
				contains = true;
			}
		}
		if(!contains){
			String res = "/Fonts/" + Font + ".ttf";
			try{
				InputStream Stream = DrawingUtil.class.getResourceAsStream(res);
				Font fnt = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, Stream);
				Stream.close();
				ge.registerFont(fnt);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		renderer = new TextRenderer(new Font(Font, fontStyle, screenWidth/textScale));
		this.Text = Text;
		this.alignment = alignment;
		this.relativePos = new float[]{x, y};
		
		updateLocation();
		
		this.changable = changable;
		this.clickable = clickable;
	}


	public void reshape(int screenWidth, int screenHeight){
		ClickBox.screenWidth = screenWidth;
		ClickBox.screenHeight = screenHeight;

		renderer = new TextRenderer(new Font(Font, fontStyle, screenWidth/textScale));
		
		updateLocation();
	}

	public void drawText(int deltaTime){
		renderer.beginRendering(screenWidth, screenHeight);
		renderer.setColor(color[0], color[1], color[2], color[3]);
		renderer.draw(Text,location[0],location[1]);
		renderer.flush();
		renderer.endRendering();
	}

	public void setColor(float red, float green, float blue, float alpha){
		this.color = new float[]{red,green,blue,alpha};
	}
	
	public void setTextSize(int size)
	{
		textScale = size;
		reshape(screenWidth, screenHeight);
	}
	
	protected void updateLocation()
	{
		Rectangle2D temp = renderer.getBounds(Text);
		if(alignment == ALIGN_MIDDLE){
			this.location = new int[]{(int)(relativePos[0] * screenWidth) - (int)(temp.getWidth()/2), (int)(relativePos[1] * screenHeight)};
		}
		else if(alignment == ALIGN_RIGHT){
			this.location = new int[]{(int)(relativePos[0] * screenWidth) - (int)(temp.getWidth()), (int)(relativePos[1] * screenHeight)};
		}
		else{
			this.location = new int[]{(int)(relativePos[0] * screenWidth), (int)(relativePos[1] * screenHeight)};
		}
		this.Bounds = new int[]{location[0]+(int)temp.getX(), 
				location[0]+(int)temp.getX()+(int)temp.getWidth(), 
				location[1]+(int)temp.getHeight(), 
				location[1]};
	}
	
	public void setText(String text)
	{
		this.Text = text;
	}
	
	public boolean isChangable(){
		return changable;
	}
	
	public static TextBox createTitle(float x, float y, int screenWidth, int screenHeight, int titleScale, String title)
	{
		return new TextBox(x, y, screenWidth, screenHeight, titleScale, "Easy 3D", 0, title, 
				1.0f, 0.2f, 0.2f, 1f, false, ALIGN_MIDDLE, false); 
	}
	
	public static TextBox createMenuBox(float x, float y, int screenWidth, int screenHeight, int textScale, String caption)
	{
		return new TextBox(x, y, screenWidth, screenHeight, textScale, "Easy 3D", 0, caption, 
				1f, 1f, 1f, 1f, true, TextBox.ALIGN_MIDDLE, false);
	}
	
	public static TextBox createHighscoreBox(float x, float y, int screenWidth, int screenHeight, int textScale, 
			String caption, float[] colour)
	{
		return new TextBox( x, y, screenWidth, screenHeight, textScale, "Easy 3D", 0, caption, 
				colour[0], colour[1], colour[2], colour[3], false,TextBox.ALIGN_LEFT, false);
	}

}
