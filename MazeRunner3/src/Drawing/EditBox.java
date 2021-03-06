package Drawing;

import javax.media.opengl.GL;

import Listening.EditModeCommand;

import com.sun.opengl.util.texture.Texture;

/**
 * the editbox class lets us put editboxes these are used in the editor state.
 *
 */
public class EditBox extends ClickBox {
	
	Texture pressed, notPressed;
	
	boolean isPressed;
	
	public EditBox(int x, int y, int screenWidth, int screenHeight, int buttonSize, boolean clickable)
	{
		super(x, y, screenWidth, screenHeight, buttonSize, buttonSize, clickable);
		isPressed = false;
	}
	
	public void setTextures(Texture pressedTexture, Texture notPressedTexture)
	{
		this.pressed = pressedTexture;
		this.notPressed = notPressedTexture;
	}
	
	public void reshape(int screenWidth, int screenHeight){
		ClickBox.screenHeight = screenHeight; 
		ClickBox.screenWidth = screenWidth; 
	}
	
	public void setButtonSize(int buttonSize)
	{
	setBounds(this.getLocation()[0], 
			this.getLocation()[0] + buttonSize, 
			this.getLocation()[1] + buttonSize, 
			this.getLocation()[1]);
	}
	
	public void drawTexture(GL gl)
	{
		Texture activeTexture;
		if(isPressed)
			activeTexture = pressed;
		else
			activeTexture = notPressed;
		activeTexture.enable();
		activeTexture.bind();
		DrawingUtil.boxOnScreen(gl, location[0], location[1], Bounds[1] - Bounds[0], Bounds[2] - Bounds[3]);
		activeTexture.disable();
		
	}
	
	public void setPressed(boolean pressed)
	{
		isPressed = pressed;
	}
	
	public boolean hasEditModeCommand()
	{
		return command instanceof EditModeCommand;
	}

}
