package GameObjects;

import java.io.File;
import java.io.PrintWriter;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import Audio.Audio;
import Drawing.VisibleObject;
import MainGame.MazeRunner;
import MazeObjects.Box;
import MazeObjects.CustomMazeObject;
import MazeObjects.MazeObject;

public class PowerUp extends GameObject implements VisibleObject {

	public static final byte SPEED = 0;
	public static final byte JUMP = 1;
	public static final byte COIN = 2;

	private float size = 0.75f;
	private int time = 10000;
	private boolean activated;
	private boolean used = false;
	private MazeObject sprite;
	private float rotationSpeed = 0.25f;
	private byte type;
	private Player player;
	private MazeRunner mazeRunner;

	public PowerUp(Vector3d pos, byte type)
	{
		super(pos);
		if(type == COIN){
			sprite = CustomMazeObject.readFromOBJ(new File("src/Objects/coin.obj"));
			sprite = sprite.translate((float)location.x, (float) location.y + 0.75f, (float)location.z);	
		}
		else{
			sprite = CustomMazeObject.readFromOBJ(new File("src/Objects/mushroom.obj"));
			sprite = sprite.translate((float)location.x, (float) location.y + 0.75f, (float)location.z);
			}
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

	private void activate(boolean active)
	{
		float factor = 3;
		if(active && !activated)
		{
			player.setColour(new float[] {0.5f, 1, 0.5f, 1});
			if(type == COIN){
				mazeRunner.addScore(10);
			}
			Audio.playSound("test");
		}
		if(!active)
		{
			factor = (float)1 / factor;
			if(activated)
				player.setColour(new float[] {1, 1, 1, 1});
		}
		activated = active;
		switch(type){
		case SPEED: player.multiplySpeed(factor); player.multiplyJump((float)1 / factor);break;
		case JUMP: player.multiplyJump(factor); break;
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
			e.printStackTrace();
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
			e.printStackTrace();
			return null;
		}
	}
	
	public void initTextures(GL gl){
		if(type == COIN){
		((CustomMazeObject) sprite).setTexture(gl);
		}
		
	}

}
