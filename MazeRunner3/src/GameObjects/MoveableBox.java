package GameObjects;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import Drawing.VisibleObject;
import LevelHandling.Maze;
import MazeObjects.Box;
import Physics.Physics;

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
	private int activationTileX, activationTileY, activationTileZ;

	public MoveableBox(Vector3d pos, float squareSize, float height)
	{
		this.location = pos;
		box = new Box(squareSize, height, (float)location.x, (float) location.y, (float)location.z);
		id = idCount;
		idCount ++;

		clearPath();
		activationTileX = -1;
		activationTileY = -1;
		activationTileZ = -1;
	}

	public void setPhysics(Physics physics)
	{
		this.physics = physics;
		physics.addBox(new Vector3f((float)location.x, (float)location.y, (float)location.z), box.width, box.height);
	}

	public void setActivationTile(int x, int y, int z)
	{
		activationTileX = x;
		activationTileY = y;
		activationTileZ = z;
		count = 0;
		isMoving = false;
	}

	public boolean isActivationTile(float x, float y, float z)
	{
		return ((int)(x/Maze.SQUARE_SIZE) == activationTileX) && ((int)(z/Maze.SQUARE_SIZE) == activationTileZ) &&
				(Math.abs(y - activationTileY) < 0.1);
	}

	public void activate(float x, float y, float z){
		if (isActivationTile(x,y,z))
		{
			count = -1;
			isMoving = true;
		}
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
		if(physics != null)
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
			else
			{
				Vector3f newLocation = physics.getBoxLocation(id);
				newLocation.sub(new Vector3f(2.5f, 2.5f, 2.5f));
				box.moveTo(newLocation);
			}
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
		if(c != 0)
			isMoving = true;
	}

	public void setMoving(boolean moving)
	{
		isMoving =  moving;
	}

	public void write(PrintWriter wr)
	{
		try{
			int previousTime = 0;
			wr.write(location.x + " " + location.y + " " + location.z + " " + box.width + " " + box.height + " " + count + " " + activationTileX + " " + activationTileY + " " + activationTileZ + "\n");
			for(int i = 0; i < pathDirection.size(); i++)
			{
				wr.write(pathTime.get(i) - previousTime + "," + pathDirection.get(i).x + "," + pathDirection.get(i).y + "," + pathDirection.get(i).z + ";");
				previousTime = pathTime.get(i);
			}
			wr.write("\n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static MoveableBox read(Scanner sc)
	{
		try{
			String text = sc.next();
			double x = Double.parseDouble(text);
			text = sc.next();
			double y = Double.parseDouble(text);
			text = sc.next();
			double z = Double.parseDouble(text);
			text = sc.next();
			float width = Float.parseFloat(text);
			text = sc.next();
			float height = Float.parseFloat(text);
			int count = sc.nextInt();
			int aX = sc.nextInt();
			int aY = sc.nextInt();
			int aZ = sc.nextInt();
			MoveableBox res = new MoveableBox(new Vector3d(x, y, z), width, height);
			res.setCount(count);
			res.setActivationTile(aX, aY, aZ);
			sc.nextLine();
			String line =  sc.nextLine();
			String[] path = line.split("[;]");
			for(String pathElement : path)
			{
				String[] pathSteps = pathElement.split("[,]");
				if(pathSteps.length > 3){
					Vector3f direction = new Vector3f(Float.parseFloat(pathSteps[1]), Float.parseFloat(pathSteps[2]), Float.parseFloat(pathSteps[3]));
					res.addToPath(Integer.parseInt(pathSteps[0]), direction);
				}
			}
			return res;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
