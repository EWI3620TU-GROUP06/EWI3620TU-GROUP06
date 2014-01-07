package Drawing;

import java.util.ArrayList;

import javax.media.opengl.GL;

import EditorModes.*;
import GameObjects.Editor;
import Listening.*;

import com.sun.opengl.util.texture.Texture;

public class EditBoxManager extends ClickBoxManager {

	private static final int numStandardButtons = 5;
	private static final int numAddButtons = 8;
	private static final int numChangeButtons = 4;
	private int numButtons;
	private int buttonSize;
	private ArrayList<ClickBox> addBoxes;
	private ArrayList<ClickBox> changeBoxes;

	public EditBoxManager(Editor editor, int screenWidth, int screenHeight)
	{
		super();
		addBoxes = new ArrayList<ClickBox>();
		changeBoxes = new ArrayList<ClickBox>();

		ArrayList<Command> commands = new ArrayList<Command>();
		commands.add(new SwitchMenuModeCommand(this));
		commands.add(new ResizeCommand(editor.getLevel().getMaze(), 1));
		commands.add(new ResizeCommand(editor.getLevel().getMaze(), -1));
		commands.add(new SaveCommand(editor));
		commands.add(new OpenCommand(editor));

		addButtons(numStandardButtons, commands, Boxes, screenWidth, screenHeight);

		commands.clear();
		commands.add(new EditModeCommand(editor, new AddStatic(editor.getLevel(), AddMode.ADD_FLOOR)));
		commands.add(new EditModeCommand(editor, new AddStatic(editor.getLevel(), AddMode.ADD_BOX)));
		commands.add(new EditModeCommand(editor, new AddStatic(editor.getLevel(), AddMode.ADD_LOW_BOX)));
		commands.add(new EditModeCommand(editor, new AddStart(editor.getLevel())));
		commands.add(new EditModeCommand(editor, new AddFinish(editor.getLevel())));
		commands.add(new EditModeCommand(editor, new AddRotating(editor.getLevel(), AddMode.ADD_RAMP)));
		commands.add(new EditModeCommand(editor, new AddRotating(editor.getLevel(), AddMode.ADD_LOW_RAMP)));
		commands.add(new CustomCommand(editor));

		addButtons(numAddButtons, commands, addBoxes, screenWidth, screenHeight);

		commands.clear();
		commands.add(new EditModeCommand(editor, new MoveObject(editor.getLevel())));
		commands.add(new EditModeCommand(editor, new RotateObject(editor.getLevel(), 0)));
		commands.add(new EditModeCommand(editor, new RotateObject(editor.getLevel(), 1)));
		commands.add(new EditModeCommand(editor, new RotateObject(editor.getLevel(), 2)));

		addButtons(numChangeButtons, commands, changeBoxes, screenWidth, screenHeight);

		this.Boxes.addAll(addBoxes);
		numButtons = Boxes.size();
		setButtonSize(numButtons, screenWidth, screenHeight);
	}

	private void addButtons(int num, ArrayList<Command> commands, ArrayList<ClickBox> boxes, int screenWidth, int screenHeight)
	{
		for(int i = 0; i < num; i++)
		{
			EditBox newBox = new EditBox(i*buttonSize, screenHeight - buttonSize, screenWidth, screenHeight, buttonSize, true);
			newBox.setCommand(commands.get(i));
			boxes.add(newBox);
		}
	}


	public void initTextures(GL gl)
	{
		setTextures(gl, numStandardButtons, Boxes, "standard button ");
		setTextures(gl, numAddButtons, addBoxes, "add button ");
		setTextures(gl, numChangeButtons, changeBoxes, "change button ");
	}

	private void setTextures(GL gl, int num, ArrayList<ClickBox> boxes, String buttonName)
	{
		for(int i = 0; i < num; i++){
			Texture pressedTexture = DrawingUtil.initTexture(gl, buttonName + i + " pressed");
			Texture notPressedTexture = DrawingUtil.initTexture(gl, buttonName + i + " not pressed");
			((EditBox)boxes.get(i)).setTextures(pressedTexture, notPressedTexture);
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
		for(int i = 0; i < numButtons; i++)
		{
			EditBox t = (EditBox) Boxes.get(i);

			t.drawTexture(gl);
		}
	}

	public void toggleMenuMode()
	{
		if(Boxes.contains(addBoxes.get(0)))
		{
			Boxes.removeAll(addBoxes);
			Boxes.addAll(changeBoxes);
		}
		else if(Boxes.contains(changeBoxes.get(0)))
		{
			Boxes.removeAll(changeBoxes);
			Boxes.addAll(addBoxes);
		}
		numButtons = Boxes.size();
		reshape(ClickBox.screenWidth, ClickBox.screenHeight);
	}

}
