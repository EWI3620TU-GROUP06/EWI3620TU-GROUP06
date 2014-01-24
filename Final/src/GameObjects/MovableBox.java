package GameObjects;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import Audio.Audio;
import Drawing.ErrorMessage;
import Drawing.VisibleObject;
import LevelHandling.Maze;
import MazeObjects.Box;
import MazeObjects.CustomMazeObject;
import Physics.Physics;

/**
 * Moveable Box is a class that handles a cube-shaped maze object which can move through the level. 
 * 
 * @author Tom Hogervorst
 *
 */

public class MovableBox extends GameObject implements VisibleObject {

	private Box box;
	private static CustomMazeObject button = CustomMazeObject.readFromOBJ(new File("src/Objects/button_01.obj"));
	private CustomMazeObject thisButton;
	private int id;
	private Vector3f direction = new Vector3f();
	private Physics physics;
	private ArrayList<Integer> pathTime;
	private ArrayList<Vector3f> pathPoints;
	private int time = 0;
	private int count = -1;
	private int activationTileX, activationTileY, activationTileZ;
	private int move_once = 0;
	private boolean isActivated = false;
	private long previousTime;
	private boolean firstrun = true;

	/**
	 * Creates a new movable box of the given size on the given location.
	 * @param pos			Position of the new movable box
	 * @param squareSize	Width of new the movable box
	 * @param height		height of the movable box
	 */

	public MovableBox(Vector3d pos, float squareSize, float height)
	{
		this.location = pos;
		box = new Box(squareSize, height, (float)location.x, (float) location.y, (float)location.z);

		clearPath();
		activationTileX = -1;
		activationTileY = -1;
		activationTileZ = -1;
	}

	/**
	 * Creates a moveable box in the physics world, and sets the id of this box to the id of the 
	 * created box in the physics world
	 * @param physics	Instance of physics used.
	 */

	public void setPhysics(Physics physics)
	{
		this.physics = physics;
		if(thisButton != null)
			physics.addButton(thisButton);
		id = physics.addBox(new Vector3f((float)location.x, (float)location.y, (float)location.z), box.getWidth(), box.getHeight());
	}

	/**
	 * Sets the activation tile of this box. When the player touches this tile, the box starts moving.
	 * @param x	x coordinate of the new activation tile.
	 * @param y	y coordinate of the new activation tile.
	 * @param z	z coordinate of the new activation tile.
	 */

	public void setActivationTile(int x, float y, int z, int move_once)
	{
		this.move_once = move_once;
		thisButton = (CustomMazeObject) button.translate(x*Maze.SQUARE_SIZE, y, z*Maze.SQUARE_SIZE);
		activationTileX = x;
		activationTileY = (int)y;
		activationTileZ = z;
		count = 0;
	}
	
	public void removeActivationTile(float x, float z)
	{
		if(activationTileX == x && activationTileZ == z)
		{
			activationTileX =-1;
			activationTileY =-1;
			activationTileZ =-1;
			count = -1;
			thisButton = null;
		}
	}
	
	public int[] getActivationTile()
	{
		return new int[]{activationTileX, activationTileY, activationTileZ, move_once};
	}

	public boolean isActivationTile(double x, double y, double z)
	{
		return  (x > (activationTileX*Maze.SQUARE_SIZE)) && 
				(x < (activationTileX*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE)) && 
				(z > (activationTileZ*Maze.SQUARE_SIZE)) &&
				(z < (activationTileZ*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE)) &&
				(Math.abs(y - activationTileY - 1) < button.getHeight() + 0.1);
	}

	public void activate(double x, double y, double z){
		if (isActivationTile(x,y,z))
		{
			long time = Calendar.getInstance().getTimeInMillis();
			int deltaTime;
			if(firstrun){
				deltaTime = 1001;
				firstrun = false;
			}
			else{
				deltaTime = (int)(time - previousTime);
			}
			if(deltaTime > 1000)
			{	
				//TODO: fixen dat pad inkort ofzo
				if(!isActivated && move_once == 0){
					count = -1;
					isActivated = true;
				}
				else if(!isActivated && move_once == 1){
					count = 1;
					isActivated = true;
				}
				else if(isActivated && move_once == 0){
					count = 0;
					isActivated = false;
				}
				else if(isActivated && move_once == 1){
					Collections.reverse(pathPoints);
					count = 1;
				}
				Audio.playSound("button");
			}
			previousTime = time;
		}
	}

	public static void initTextures(GL gl)
	{
		button.setTexture(gl);
	}

	public void setTextNum()
	{
		if(thisButton != null)
			thisButton.setTexNum(button.getTexNum());
	}

	public void addToPath(int time, Vector3f point)
	{
		if(pathTime.size() > 0)
			time += pathTime.get(pathTime.size() - 1);
		pathPoints.add(point);
		pathTime.add(time);
	}

	@Override
	public void display(GL gl) {
		box.draw(gl, new float[] {1f, 1f, 1f, 1f});
		if(thisButton != null)
			if(!isActivated){
				thisButton.draw(gl, new float[] {0.5f, 0.5f, 0.5f, 1f});
			}
			else{
				thisButton.draw(gl, new float[] {1f, 1f, 1f, 1f});
			}
	}

	public void update(int deltaTime)
	{
		if(physics != null)
		{
			if(count == 0){
				Vector3f newLocation = physics.moveBox(id, new Vector3f(0, 0, 0));
				newLocation.sub(new Vector3f(2.5f, 2.5f, 2.5f));
				box.moveTo(newLocation);
				return;
			}
			
			if(pathTime.size() > 0)
			{
				time += deltaTime;
				if(time > pathTime.get(pathTime.size() - 1)){
					time -= pathTime.get(pathTime.size() - 1);
					if(count > 0)
						count--;
				}
				if(count != 0)
				{
					int i = 0;
					while(i < pathTime.size() - 1 && time > pathTime.get(i) ){
						i++;
					}
					if((time - pathTime.get(i))< -100 || direction.equals(new Vector3f()))
					{
						direction = new Vector3f();
						direction.sub(box.getPos(), pathPoints.get(i));

						direction.scale(1f /((float)(time - pathTime.get(i)) / 1000f));
					}

					Vector3f newLocation = physics.moveBox(id, direction);
					newLocation.sub(new Vector3f(2.5f, 2.5f, 2.5f));
					box.moveTo(newLocation);
				}
			}
			else
			{
				Vector3f newLocation = physics.getBoxLocation(id);
				newLocation.sub(new Vector3f(2.5f, 2.5f, 2.5f));
				box.moveTo(newLocation);
				System.out.println(newLocation.toString());
			}
		}
	}

	public void clearPath()
	{
		pathTime = new ArrayList<Integer>();
		pathPoints = new ArrayList<Vector3f>();
	}

	public void setCount(int c)
	{
		count = c;
	}

	public void write(PrintWriter wr)
	{
		try{
			int previousTime = 0;
			wr.write(location.x + " " + location.y + " " + location.z + " " + box.getWidth() + " " + box.getHeight() + " " + count + " " + activationTileX + " " + activationTileY + " " + activationTileZ + " " + move_once + " " + "\n");
			for(int i = 0; i < pathPoints.size(); i++)
			{
				wr.write(pathTime.get(i) - previousTime + "," + pathPoints.get(i).x + "," + pathPoints.get(i).y + "," + pathPoints.get(i).z + ";");
				previousTime = pathTime.get(i);
			}
			wr.write("\n");
		}
		catch(Exception e)
		{
			ErrorMessage.show("Exception while writing movable box.\n" + e.toString());
		}
	}

	public static MovableBox read(Scanner sc)
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
			int moveonce = sc.nextInt();
			MovableBox res = new MovableBox(new Vector3d(x, y, z), width, height);
			if(!(aX < 0 || aY < 0 || aZ < 0))
				res.setActivationTile(aX, aY, aZ, moveonce);
			res.setCount(count);
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
			ErrorMessage.show("Exception while reading movable box.\n" + e.toString());
			return null;
		}
	}

}
