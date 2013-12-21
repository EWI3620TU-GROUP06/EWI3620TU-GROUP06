package Drawing;

import java.util.ArrayList;

import javax.media.opengl.GL;

import EditorModes.*;
import GameObjects.Editor;
import Listening.*;
import MainGame.Level;

import com.sun.opengl.util.texture.Texture;

public class EditBoxManager extends ClickBoxManager {

	private static final int numStandardButtons = 5;
	private static final int numAddButtons = 8;
	private int numButtons;
	private int buttonSize;
	private ArrayList<EditBox> addBoxes;

	public EditBoxManager(Level level, Editor editor, int screenWidth, int screenHeight)
	{
		super();
		addBoxes = new ArrayList<EditBox>();
		
		ArrayList<Command> commands = new ArrayList<Command>();
		commands.add(new SwitchMenuModeCommand(this));
		commands.add(new ResizeCommand(level.getMaze(), 1));
		commands.add(new ResizeCommand(level.getMaze(), -1));
		commands.add(new SaveCommand(editor));
		commands.add(new OpenCommand(editor));

		for (int i = 0; i < numStandardButtons; i++)
		{
			EditBox newBox = new EditBox(i*buttonSize, screenHeight - buttonSize, screenWidth, screenHeight, buttonSize, true);
			newBox.setCommand(commands.get(i));
			this.AddBox(newBox);
		}
		
		commands.clear();
		commands.add(new EditModeCommand(editor, new AddStatic(level, AddMode.ADD_FLOOR)));
		commands.add(new EditModeCommand(editor, new AddStatic(level, AddMode.ADD_BOX)));
		commands.add(new EditModeCommand(editor, new AddStatic(level, AddMode.ADD_LOW_BOX)));
		commands.add(new EditModeCommand(editor, new AddStart(level)));
		commands.add(new EditModeCommand(editor, new AddFinish(level)));
		commands.add(new EditModeCommand(editor, new AddRotating(level, AddMode.ADD_RAMP)));
		commands.add(new EditModeCommand(editor, new AddRotating(level, AddMode.ADD_LOW_RAMP)));
		commands.add(new CustomCommand(editor));
		
		for(int i = 0; i < numAddButtons; i++)
		{
			EditBox newBox = new EditBox(i*buttonSize, screenHeight - buttonSize, screenWidth, screenHeight, buttonSize, true);
			newBox.setCommand(commands.get(i));
			addBoxes.add(newBox);
		}
		
		this.Boxes.addAll(addBoxes);
		numButtons = Boxes.size();
		setButtonSize(numButtons, screenWidth, screenHeight);
	}
	
	public void initTextures(GL gl)
	{
		for(int i = 0; i < numStandardButtons; i++)
		{
			Texture pressedTexture = DrawingUtil.initTexture(gl, "standard button " + i + " pressed");
			Texture notPressedTexture = DrawingUtil.initTexture(gl, "standard button " + i + " not pressed");
			((EditBox)Boxes.get(i)).setTextures(pressedTexture, notPressedTexture);
		}
		
		for(int i = 0; i < numAddButtons; i++)
		{
			Texture pressedTexture = DrawingUtil.initTexture(gl, "add button " + i + " pressed");
			Texture notPressedTexture = DrawingUtil.initTexture(gl, "add button " + i + " not pressed");
			((EditBox)addBoxes.get(i)).setTextures(pressedTexture, notPressedTexture);
		}
	}

	public boolean isHoovering()
	{
		boolean hoovering = false;
		for(ClickBox a: Boxes){
			if(a.isInBounds(control.getMouseX(), control.getMouseY())){
				hoovering = true;
			}
		}
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
					if(e.hasEditModeCommand())
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
