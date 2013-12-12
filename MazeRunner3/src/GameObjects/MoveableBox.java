package GameObjects;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import Drawing.VisibleObject;
import MainGame.Physics;
import MazeObjects.Box;

public class MoveableBox extends GameObject implements VisibleObject {
	
	private Box box;
	static private int idCount;
	private int id;
	private Physics physics;
	
	public MoveableBox(Vector3d pos, int squareSize, int height, Physics physics)
	{
		this.location = pos;
		box = new Box(squareSize, height, (float)location.x, (float)location.z);
		id = idCount;
		idCount += 1;
		this.physics = physics;
		physics.addBox(new Vector3f((float)pos.x, (float)pos.y, (float)pos.z), squareSize, height);
	}

	@Override
	public void display(GL gl) {
		box.draw(gl, new float[] {1f, 1f, 1f, 1f});
	}
	
	public void update(Vector3d newPosition)
	{
		location = newPosition; 
		Vector3f newLocation = new Vector3f((float)location.x, (float)location.y, (float)location.z);
		physics.moveBox(id, newLocation);
		
		newLocation.sub(new Vector3f(2.5f, 2.5f, 2.5f));
		box.moveTo(newLocation);
		
	}
	
	public static void resetIDs()
	{
		idCount = 0;
	}

}
