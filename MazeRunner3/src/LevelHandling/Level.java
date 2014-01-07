package LevelHandling;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import Drawing.VisibleObject;
import GameObjects.MoveableBox;
import GameObjects.Player;
import GameObjects.PowerUp;
import MainGame.MazeRunner;
import PSO.Swarm;
import Physics.Physics;

public class Level {
	private Maze maze;
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	private ArrayList<MoveableBox> moveableBoxes = new ArrayList<MoveableBox>();
	private Swarm swarm;
	private boolean added = false;
	
	public Level(Maze mz){
		this.maze = mz;
	}
	
	public void addSwarm(Swarm swarm)
	{
		this.swarm = swarm;
	}
	
	public Swarm getSwarm()
	{
		return swarm;
	}
	
	public void addPowerUp(PowerUp pU){
		this.powerUps.add(pU);
		added = true;
	}
	
	public ArrayList<PowerUp> getPowerUps(){
		return this.powerUps;
	}
	
	public void addMoveableBox(MoveableBox mB){
		this.moveableBoxes.add(mB);
		added = true;
	}
	
	public ArrayList<MoveableBox> getMoveableBoxes(){
		return this.moveableBoxes;
	}
	
	public Maze getMaze(){
		return this.maze;
	}
	
	public void init(GL gl)
	{
		Maze.initTextures(gl);
		for(PowerUp powerUp : powerUps)
			powerUp.initTextures(gl);
		swarm.init(gl);
	}
	
	public void update(int deltaTime, Vector3d playerPos)
	{
		if(swarm != null)
			swarm.update(deltaTime);
		for(MoveableBox moveBox : moveableBoxes)
			moveBox.update(deltaTime);
		for(PowerUp powerUp : powerUps)
			powerUp.update(deltaTime, playerPos);
	}
	
	public void pause()
	{
		swarm.pause();
	}
	
	public void addToVisible(ArrayList<VisibleObject> visibleObjects)
	{
		visibleObjects.add(maze);
		if(swarm != null)
			swarm.AddToVisible(visibleObjects);
		for(MoveableBox moveBox : moveableBoxes)
			visibleObjects.add(moveBox);
		for(PowerUp powerUp : powerUps)
			visibleObjects.add(powerUp);
	}
	
	public boolean addedSomething()
	{
		boolean res = added;
		added = false;
		return res;
	}
	
	public void removeFromVisible(ArrayList<VisibleObject> visibleObjects)
	{
		visibleObjects.remove(maze);
		for(MoveableBox moveBox : moveableBoxes)
			visibleObjects.remove(moveBox);
		for(PowerUp powerUp : powerUps)
			visibleObjects.remove(powerUp);
	}
	
	public void setAttributes(Player player, Physics physics, MazeRunner mazeRunner)
	{
		for(MoveableBox moveBox : moveableBoxes)
			moveBox.setPhysics(physics);;
		for(PowerUp powerUp : powerUps)
		{
			powerUp.setPlayer(player);
			powerUp.setMazeRunner(mazeRunner);
		}
	}
	
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
			for(MoveableBox moveBox :  moveableBoxes){
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
				System.out.println("Added power up");
			}
			while(sc.hasNext()){
				MoveableBox newMoveBox = MoveableBox.read(sc);
				if(newMoveBox != null)
					res.moveableBoxes.add(newMoveBox);
				System.out.println("Added moving box");
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
