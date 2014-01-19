package LevelHandling;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import Drawing.VisibleObject;
import GameObjects.MovableBox;
import GameObjects.Player;
import GameObjects.PowerUp;
import MainGame.MazeRunner;
import MazeObjects.CustomMazeObject;
import PSO.Swarm;
import Physics.Physics;

/**
 * the level class makes an object level which contains information about the maze, powerups and swarm in each level
 *
 */

public class Level {
	private Maze maze;
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	private ArrayList<MovableBox> movableBoxes = new ArrayList<MovableBox>();
	private ArrayList<MovableBox> removedBoxes = new ArrayList<MovableBox>();
	private ArrayList<PowerUp> removedPowerUps = new ArrayList<PowerUp>();
	private Swarm swarm;
	private boolean changed = false;
	
	public Level(Maze mz){
		this.maze = mz;
		maze.selectedAll();
		PowerUp.initSprites();
	}
	
	public void addSwarm(Swarm swarm)
	{
		this.swarm = swarm;
	}
	
	public Swarm getSwarm()
	{
		return swarm;
	}
	
	public PowerUp getPowerUp(int mazeX, int mazeZ)
	{
		for(PowerUp pU : powerUps)
		{
			Vector3d l = pU.getLocation();
			if(l.x > mazeX * Maze.SQUARE_SIZE && l.x < (mazeX + 1) * Maze.SQUARE_SIZE &&
					l.z > mazeZ * Maze.SQUARE_SIZE && l.z < (mazeZ + 1) * Maze.SQUARE_SIZE
					&& l.y > maze.getHeight(mazeX,  mazeZ))
				return pU;
		}
		return null;
	}
	
	public void addPowerUp(int mazeX, int mazeZ, byte type){
		Vector3d l = new Vector3d(mazeX * Maze.SQUARE_SIZE + 2.5, 0, mazeZ * Maze.SQUARE_SIZE+2.5);
		if(l.x > 0 && l.x < maze.getSizeX() && l.z > 0  && l.z < maze.getSizeZ() && getPowerUp(mazeX, mazeZ) == null&& getMovableBox(mazeX, mazeZ) == null){
			l.y = maze.getHeight(mazeX, mazeZ) + 0.5f * Maze.SQUARE_SIZE;
			this.powerUps.add(new PowerUp(l, type));
			changed = true;
		}
	}
	
	public ArrayList<PowerUp> getPowerUps(){
		return this.powerUps;
	}
	
	public MovableBox getMovableBox(int mazeX, int mazeZ)
	{
		for(MovableBox mB : movableBoxes)
		{
			Vector3d l = mB.getLocation(); 
			if(Math.abs(l.x - mazeX * Maze.SQUARE_SIZE) < 0.1 && Math.abs(l.z - mazeZ * Maze.SQUARE_SIZE) < 0.1 &&
					l.y >= maze.getHeight(mazeX,  mazeZ))
				return mB;
		}
		return null;
	}
	
	public void addMoveableBox(int mazeX, int mazeZ){
		Vector3d l = new Vector3d(mazeX * Maze.SQUARE_SIZE, 0, mazeZ * Maze.SQUARE_SIZE);
		if(l.x >= 0 && l.x < maze.getSizeX() && l.z >= 0  && l.z < maze.getSizeZ() && getMovableBox(mazeX, mazeZ) == null && getPowerUp(mazeX, mazeZ) == null){
			l.y = maze.getHeight(mazeX, mazeZ);
			this.movableBoxes.add(new MovableBox(l, Maze.SQUARE_SIZE, Maze.SQUARE_SIZE));
			changed = true;
		}
	}
	
	public void setButton(int mazeX, int mazeZ){
		if(movableBoxes.size() > 0 && mazeX >= 0 && mazeX < maze.getSizeX() 
				&& mazeZ >= 0  && mazeZ < maze.getSizeZ() && getMovableBox(mazeX, mazeZ) == null
				&& getPowerUp(mazeX, mazeZ) == null){
			float y = maze.getHeight(mazeX, mazeZ);
			this.movableBoxes.get(movableBoxes.size() - 1).setActivationTile(mazeX, y, mazeZ);
			changed = true;
		}
	}
	
	public ArrayList<MovableBox> getMoveableBoxes(){
		return this.movableBoxes;
	}
	
	public Maze getMaze(){
		return this.maze;
	}
	
	public void init(GL gl)
	{
		CustomMazeObject.clearTextures();
		maze.initTextures(gl);
		maze.setCustomTextures(gl);
		PowerUp.initTextures(gl);
		MovableBox.initTextures(gl);
		for(PowerUp powerUp : powerUps)
			powerUp.setTextNum();
		for(MovableBox moveBox : movableBoxes)
			moveBox.setTextNum();
		if(swarm != null)
			swarm.init(gl);
	}
	
	public void update(int deltaTime, Vector3d playerPos)
	{
		if(swarm != null)
			swarm.update(deltaTime);
		for(MovableBox moveBox : movableBoxes){
			moveBox.update(deltaTime);
			moveBox.activate(playerPos.x, playerPos.y, playerPos.z);
		}
		for(PowerUp powerUp : powerUps)
			powerUp.update(deltaTime, playerPos);
	}
	
	public void pause()
	{
		if(swarm != null)
			swarm.pause();
	}
	
	/**
	 * the method addToVisibleObjects adds all the maze, swarm and the powerups of each level to the visible 
	 * objects wich will displayed.
	 */
	
	public void addToVisible(ArrayList<VisibleObject> visibleObjects)
	{
		visibleObjects.add(maze);
		if(swarm != null)
			swarm.AddToVisible(visibleObjects);
		for(MovableBox moveBox : movableBoxes)
			visibleObjects.add(moveBox);
		for(PowerUp powerUp : powerUps)
			visibleObjects.add(powerUp);
	}
	
	public void resize(int x, int z)
	{
		maze.setSize(x, z);
		
		for(int i = 0; i < powerUps.size(); i++)
		{
			PowerUp pU = powerUps.get(i);
			Vector3d l = pU.getLocation();
			if( l.x > maze.getSizeX() || l.z > maze.getSizeZ()){
				removedPowerUps.add(pU);
				changed = true;
			}
		}
		
		for(int i = 0; i < movableBoxes.size(); i++)
		{
			MovableBox mB = movableBoxes.get(i);
			Vector3d l = mB.getLocation();
			if( l.x >= maze.getSizeX() || l.z >= maze.getSizeZ()){
				removedBoxes.add(mB);
			}
			int aX = mB.getActivationTile()[0];
			int aZ = mB.getActivationTile()[2];
			if(aX > x || aZ > z)
				mB.removeActivationTile(aX, aZ);
			changed = true;
		}
	}
	
	public boolean changedSomething()
	{
		boolean res = changed;
		changed = false;
		return res;
	}
	
	/**
	 * the method removeFromVisibleObjects removes the maze, swarm and the powerups of each level from the visible 
	 * objects wich will displayed.
	 */
	
	public void removeFromVisible(ArrayList<VisibleObject> visibleObjects)
	{
		visibleObjects.remove(maze);
		for(MovableBox moveBox : movableBoxes)
			visibleObjects.remove(moveBox);
		for(PowerUp powerUp : powerUps)
			visibleObjects.remove(powerUp);
		movableBoxes.removeAll(removedBoxes);
		removedBoxes.clear();
		powerUps.removeAll(removedPowerUps);
		removedPowerUps.clear();
	}
	
	public void removeTop(int x, int z)
	{
		float height = maze.getHeight(x, z);
		PowerUp pU = getPowerUp(x,z);
		MovableBox mB = getMovableBox(x,z);
		for(MovableBox mb : movableBoxes)
		{
			mb.removeActivationTile(x, z);
		}
		if(pU != null && pU.getLocation().y > height && pU.getLocation().y < height + 5)
		{
			removedPowerUps.add(pU);
		}
		else if(mB != null && Math.abs(mB.getLocation().y - height) < 0.1)
		{
			removedBoxes.add(mB);
		}
		else
		{
			maze.removeTop(x, z);
		}
		changed = true;
	}
	
	/**
	 * the setattrributes method sets the all the interactive components in the game to interact with the player
	 * the physics.
	 */
	
	public void setAttributes(Player player, Physics physics, MazeRunner mazeRunner)
	{
		for(MovableBox moveBox : movableBoxes)
			moveBox.setPhysics(physics);;
		for(PowerUp powerUp : powerUps)
		{
			powerUp.setPlayer(player);
			powerUp.setMazeRunner(mazeRunner);
		}
	}
	
	/**
	 * the save level method saves a whole level in a file including the movable boxes, powerups and maze
	 * @param file
	 */
	
	public void saveLevel(File file)
	{
		try{
			PrintWriter wr = new PrintWriter(file);
			maze.write(wr);
			wr.write("PowerUps:\n");
			for(PowerUp powerUp:  powerUps){
				powerUp.write(wr);
			}
			wr.write("MoveableBoxes:\n");
			for(MovableBox moveBox :  movableBoxes){
				moveBox.write(wr);
			}
			wr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Level readLevel(File file)
	{
		try{
			Scanner sc = new Scanner(file);
			Maze maze =  Maze.read(sc);
			Level res = new Level(maze);
			while(!sc.nextLine().equals("PowerUps:")&& sc.hasNextLine()){
				//loop
			}
			String line = "";
			while(sc.hasNextLine()){
				if((line = sc.nextLine()).equals("MoveableBoxes:"))
					break;
				PowerUp newPower = PowerUp.read(line);
				if(newPower != null)
					res.powerUps.add(newPower);
			}
			while(sc.hasNext()){
				MovableBox newMoveBox = MovableBox.read(sc);
				if(newMoveBox != null)
					res.movableBoxes.add(newMoveBox);
			}
			sc.close();	
			return res;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
