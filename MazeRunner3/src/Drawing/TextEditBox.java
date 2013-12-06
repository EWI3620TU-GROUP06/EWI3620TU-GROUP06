package Drawing;



import java.awt.event.KeyEvent;

import Listening.Control;

public class TextEditBox extends TextBox {
	
	private int counter;
	private int cursorPos;
	private Control control;
	private boolean confirm = false;
	
	public static final int MAX_CHARACTERS = 10; 
	
	public TextEditBox(float x, float y, int screenWidth, int screenHeight, 
			int textScale, float red, float green, float blue, float alpha)
	{
		super(x, y, screenWidth, screenHeight, textScale, "Consolas", 0, "          ", 
			red, green, blue, alpha, false, ALIGN_LEFT);
		counter = 0;
		cursorPos = 0;
	}
	
	public void setControl(Control control)
	{
		this.control = control;
	}
	
	@Override
	public void drawText(int deltaTime)
	{
		counter += deltaTime;
		if(counter > 1000)
			counter -= 1000;
		
		char[] text = Text.toCharArray();
		int key = control.getTypedKey();
		if(key != 0)
			confirm = false;
		if(key == KeyEvent.VK_ENTER)
			confirm = true;
		else if(key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z){
			text[cursorPos] = (char)(control.isUpperCase() ? key : key + 32);
			if(cursorPos < MAX_CHARACTERS - 1)
				cursorPos ++;
		}
		else if(key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9){
			text[cursorPos] = (char)key;
			if(cursorPos < MAX_CHARACTERS - 1)
				cursorPos ++;
		}
		else if(key == KeyEvent.VK_LEFT && cursorPos > 0){
				cursorPos--;
		}
		else if(key == KeyEvent.VK_RIGHT && cursorPos < MAX_CHARACTERS - 1){
			cursorPos++;
		}
		else if(key == KeyEvent.VK_BACK_SPACE && cursorPos > 0){
			for(int i = cursorPos; i < MAX_CHARACTERS - 1; i++){
				text[i] = text[i + 1];
			}
			text[MAX_CHARACTERS - 1] = ' ';
			cursorPos--;
		}
		else if(key == KeyEvent.VK_DELETE){
			for(int i = cursorPos; i < MAX_CHARACTERS - 1; i++){
				text[i] = text[i + 1];
			}
			text[MAX_CHARACTERS - 1] = ' ';
		}
			
		char[] cursor = new char[10];
		for(int i = 0; i < 10; i++)
		{
			if(i == cursorPos && counter < 500)
				cursor[i] = '_';
			else
				cursor[i] = ' ';
		}
		Text = String.copyValueOf(text);
		renderer.beginRendering(this.screenWidth, this.screenHeight);
		renderer.setColor(color[0], color[1], color[2], color[3]);
		renderer.draw(Text,location[0],location[1]);
		renderer.draw(String.copyValueOf(cursor),location[0],location[1]);
		renderer.flush();
		renderer.endRendering();
	}
	
	public String getText()
	{
		if(confirm)
		{
			String[] text = Text.split("[ ]");
			for(int i = 0; i < text.length; i++)
			{
				if(!text[i].isEmpty())
					return text[i]; 
			}
			return null;
		}
		else
			return null;
	}

}
