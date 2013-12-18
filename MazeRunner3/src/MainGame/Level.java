package MainGame;

import java.util.ArrayList;

import GameObjects.MoveableBox;
import GameObjects.PowerUp;

public class Level {
	private Maze maze;
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	private ArrayList<MoveableBox> moveableBoxes = new ArrayList<MoveableBox>();
	
	public Level(Maze mz){
		this.maze = mz;
	}
	
	public void addPowerUp(PowerUp pU){
		this.powerUps.add(pU);
	}
	
	public ArrayList<PowerUp> getPowerUps(){
		return this.powerUps;
	}
	
	public void addMoveableBox(MoveableBox mB){
		this.moveableBoxes.add(mB);
	}
	
	public ArrayList<MoveableBox> getMoveableBoxes(){
		return this.moveableBoxes;
	}
	
	public Maze getMaze(){
		return this.maze;
	}
	

}
