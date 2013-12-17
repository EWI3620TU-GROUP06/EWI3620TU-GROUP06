package GameObjects;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import Drawing.VisibleObject;
import MazeObjects.Box;
import MazeObjects.MazeObject;

public class PowerUp extends GameObject implements VisibleObject {

	public static final int SPEED = 0;
	public static final int JUMP = 1;

	private float size;
	private int time = 10000;
	private boolean activated;
	private boolean used = false;
	private MazeObject sprite;
	private float rotationSpeed = 0.25f;
	private int type;
	private Player player;

	public PowerUp(Vector3d pos, float size, Player player, int type)
	{
		super(pos);
		this.size = size;
		sprite = new Box( size, size, (float)location.x, (float) location.y, (float)location.z);
		this.player = player;
		this.type = type;
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
			if(dif.length() < 1 && !activated)
			{
				activate(true);
				used = true;
			}
		}
		sprite.rotateVerticesY(deltaTime * rotationSpeed, (float)location.x + 0.5f * size, (float)location.z + 0.5f * size);
	}

	public boolean isActive()
	{
		return activated;
	}

	private void activate(boolean active)
	{
		activated = active;
		float factor = 3;
		if(active)
		{
			player.setColour(new float[] {0.5f, 1, 0.5f, 1});
		}
		if(!active)
		{
			factor = (float)1 / factor;
			player.setColour(new float[] {1, 1, 1, 1});
		}
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

}
