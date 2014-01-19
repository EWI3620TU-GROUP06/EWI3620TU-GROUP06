package GameObjects;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import Audio.Audio;
import Drawing.ErrorMessage;
import Drawing.VisibleObject;
import MainGame.MazeRunner;
import MazeObjects.CustomMazeObject;
import MazeObjects.MazeObject;

/**
 * the powerup class enables us to place powerups in the game some of the methods are described below
 */

public class PowerUp extends GameObject implements VisibleObject {

	public static final byte SPEED = 0;
	public static final byte JUMP = 1;
	public static final byte COIN = 2;

	private float size = 0.75f;
	private int time = 10000;
	private boolean activated;
	private boolean used = false;
	private static ArrayList<MazeObject> sprites = new ArrayList<MazeObject>();
	private MazeObject sprite;
	
	private float rotationSpeed = 0.1f;
	private byte type;
	private Player player;
	private MazeRunner mazeRunner;
	
	/**
	 * in this constructor we give a power up a sprite and we give this sprite a location
	 * @param pos	the position of the powerup
	 * @param type	the type of the powerup
	 */
	public PowerUp(Vector3d pos, byte type)
	{
		super(pos);
		sprite = sprites.get(type).translate((float)location.x, (float) location.y + 0.75f, (float)location.z);
		this.type = type;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public void setMazeRunner(MazeRunner mzr)
	{
		mazeRunner = mzr;
	}

	/**
	 * in this update method we determine if the powerup is gatherd by the the player and we let the sprit that
	 * is used to represent the powerup turn around.
	 * @param deltaTime		used to set the length of the powerup
	 * @param playerPos		used to determine if the powerup is picked
	 */
	public void update (int deltaTime, Vector3d playerPos)
	{
		if(activated)
		{
			time -= deltaTime;
			if(time < 0)
			{
				activate(false);
			}
		}
		if(!used)
		{
			Vector3d dif = new Vector3d();
			dif.sub(playerPos, this.location);
			if(dif.length() < 1 + size / 2f && !activated)
			{
				activate(true);
				used = true;
			}
		}
		sprite.rotateVerticesY(deltaTime * rotationSpeed, ((float)location.x + 0.5f * size), ((float)location.z + 0.5f * size));
	}

	public boolean isActive()
	{
		return activated;
	}
	
	/**
	 * this method is used to activate the power-up and give it the functionality it has in the game
	 * @param active	Boolean that is used to either activate or deactivate the power-up.
	 */
	private void activate(boolean active)
	{
		float factor = 3;
		boolean coinpicked = false;
		if(active && !activated)
		{
			coinpicked = true;
		}

		if(!active)
		{
			factor = (float)1 / factor;
			if(activated)
				player.setColour(new float[] {1, 1, 1, 1});
		}
		activated = active;
		switch(type){
			case SPEED: player.multiplySpeed(factor);player.setColour(new float[] {1f, 0.5f, 0.5f, 1f});player.multiplyJump((float)1 / factor);if(active){Audio.playSound("speedup");};break;
			case JUMP: player.multiplyJump(factor);player.setColour(new float[] {0.5f, 1f, 0.5f, 1f});if(active){Audio.playSound("jumpup");};break;
			case COIN: if(coinpicked){mazeRunner.addScore(10);Audio.playSound("coin");};break;
			default:;
		}
	}

	public void display(GL gl)
	{
		if(!used)
		{
			sprite.draw(gl, new float[]{1, 1, 1, 1});
		}
	}

	public void write(PrintWriter wr)
	{
		try{
			wr.write(location.x + " "  + location.y + " " + location.z + " " + type + "\n");
		}
		catch(Exception e)
		{
			ErrorMessage.show("Exception while writing power-up.\n" + e.toString());
		}
	}

	public static PowerUp read(String line)
	{
		try{
			String[] elements = line.split("[ ]");
			if(!elements[0].isEmpty() && elements.length == 4){
				double x = Double.parseDouble(elements[0]);
				double y = Double.parseDouble(elements[1]);
				double z = Double.parseDouble(elements[2]);
				byte type = Byte.parseByte(elements[3]);
				return new PowerUp(new Vector3d(x, y, z), type);
			}
			else
				return null;

		}
		catch(Exception e)
		{
			ErrorMessage.show("Exception while reading power-up.\n" + e.toString());
			return null;
		}
	}
	
	public static void initSprites()
	{
		sprites.clear();
		sprites.add(CustomMazeObject.readFromOBJ(new File("src/Objects/krijt_01.obj")));
		sprites.add(CustomMazeObject.readFromOBJ(new File("src/Objects/krijt_02.obj")));
		sprites.add(CustomMazeObject.readFromOBJ(new File("src/Objects/coin.obj")));
	}
	
	public static void initTextures(GL gl){
		for(MazeObject sprite : sprites)
		{
			((CustomMazeObject) sprite).setTexture(gl);
		}
	}
	
	public void setTextNum()
	{
		((CustomMazeObject) sprite).setTexNum(((CustomMazeObject) sprites.get(type)).getTexNum());
	}

}
