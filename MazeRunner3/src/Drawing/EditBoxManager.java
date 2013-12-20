package Drawing;

import java.util.ArrayList;

import javax.media.opengl.GL;

import GameObjects.Editor;
import LevelHandling.Maze;
import Listening.*;

import com.sun.opengl.util.texture.Texture;

public class EditBoxManager extends ClickBoxManager {

	private boolean hoovering = false;
	private static final int numButtons = 12;
	private int buttonSize;

	public EditBoxManager(Maze maze, Editor editor, int screenWidth, int screenHeight)
	{
		super();
		setButtonSize(numButtons, screenWidth, screenHeight);
		ArrayList<Command> commands = new ArrayList<Command>();
		commands.add(new ResizeCommand(maze, 1));
		commands.add(new ResizeCommand(maze, -1));
		for(byte i = 0; i < 7; i++){
			commands.add(new DrawModeCommand(editor, i));
		}
		commands.add(new SaveCommand(editor));
		commands.add(new OpenCommand(editor));
		commands.add(new CustomCommand(editor));

		for (int i = 0; i < numButtons; i++)
		{
			EditBox newBox = new EditBox(i*buttonSize, screenHeight - buttonSize, screenWidth, screenHeight, buttonSize, true);
			newBox.setCommand(commands.get(i));
			this.AddBox(newBox);
		}
	}

	public void initTextures(GL gl)
	{
		for(int i = 0; i < numButtons; i++)
		{
			Texture pressedTexture = DrawingUtil.initTexture(gl, "button " + i + " pressed");
			Texture notPressedTexture = DrawingUtil.initTexture(gl, "button " + i + " not pressed");
			((EditBox)Boxes.get(i)).setTextures(pressedTexture, notPressedTexture);
		}
	}

	public boolean isHoovering()
	{
		return hoovering;
	}

	public void AddBox(ClickBox box)
	{
		if(box instanceof EditBox){
			Boxes.add(box);
		}
	}

	public void update(){
		control.update();
		if(control.getClicked() != 0){
			for(int i = 0; i < Boxes.size(); i++){
				EditBox e = (EditBox) Boxes.get(i);
				if(e.isClickable() && e.isInBounds(control.getMouseX(), control.getMouseY())){
					if(e.hasDrawModeCommand())
					{
						e.setPressed(true);
						for(int j = 0; j < Boxes.size(); j++)
						{
							if(j != i){
								((EditBox)Boxes.get(j)).setPressed(false);
							}
						}
					}
					e.execute();
				}
			}
		}
		else{
			hoovering = false;
			for(ClickBox a: Boxes){
				if(a.isInBounds(control.getMouseX(), control.getMouseY())){
					hoovering = true;
				}
			}
		}
	}

	public void reshape(int screenWidth, int screenHeight)
	{
		setButtonSize(Boxes.size(), screenWidth, screenHeight);
		for(int i = 0; i < numButtons; i++)
		{
			EditBox t = (EditBox) Boxes.get(i);

			t.setButtonSize(buttonSize);

			t.reshape(screenWidth, screenHeight);
			t.setLocation(i * buttonSize, screenHeight - buttonSize);
			t.setBounds(t.getLocation()[0], t.getLocation()[0] + buttonSize,t.getLocation()[1] + buttonSize, t.getLocation()[1]);
		}

	}

	public void setButtonSize(int numButtons, int screenWidth, int screenHeight)
	{
		int horizontalSize = screenWidth / numButtons;
		int verticalSize = screenHeight / 10;
		buttonSize = verticalSize < horizontalSize ? verticalSize : horizontalSize;
	}

	public void drawTextures(GL gl)
	{
		for(ClickBox a :  Boxes)
		{
			EditBox t = (EditBox) a;

			t.drawTexture(gl);
		}
	}

}
