package GameObjects;

import java.util.ArrayList;

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
	private ArrayList<Integer> pathTime;
	private ArrayList<Vector3f> pathDirection;
	private int time = 0;
	private boolean isMoving = true;
	private int count = -1;

	public MoveableBox(Vector3d pos, int squareSize, int height, Physics physics)
	{
		this.location = pos;
		box = new Box(squareSize, height, (float)location.x, (float)location.z);
		id = idCount;
		idCount ++;
		this.physics = physics;
		physics.addBox(new Vector3f((float)pos.x, (float)pos.y, (float)pos.z), squareSize, height);
		clearPath();
	}

	public void addToPath(int time, Vector3f direction)
	{
		if(pathTime.size() > 0)
			time += pathTime.get(pathTime.size() - 1);
		pathDirection.add(direction);
		pathTime.add(time);
	}

	@Override
	public void display(GL gl) {
		box.draw(gl, new float[] {1f, 1f, 1f, 1f});
	}

	public void update(int deltaTime)
	{
		if(isMoving && pathTime.size() > 0)
		{
			time += deltaTime;
			if(time > pathTime.get(pathTime.size() - 1)){
				time -= pathTime.get(pathTime.size() - 1);
				if(count > 0)
					count--;
				if(count == 0){
					isMoving = false;
					Vector3f newLocation = physics.moveBox(id, new Vector3f(0, 0, 0));
					newLocation.sub(new Vector3f(2.5f, 2.5f, 2.5f));
					box.moveTo(newLocation);
					return;
				}
			}
			int i = 0;
			while(i < pathTime.size() - 1 && time > pathTime.get(i) ){
				i++;
			}
			Vector3f newLocation = physics.moveBox(id, pathDirection.get(i));
			newLocation.sub(new Vector3f(2.5f, 2.5f, 2.5f));
			box.moveTo(newLocation);
		}
	}

	public static void resetIDs()
	{
		idCount = 0;
	}
	
	public void clearPath()
	{
		pathTime = new ArrayList<Integer>();
		pathDirection = new ArrayList<Vector3f>();
	}
	
	public void setCount(int c)
	{
		count = c;
	}
	
	public void setMoving(boolean moving)
	{
		isMoving =  moving;
	}

}
