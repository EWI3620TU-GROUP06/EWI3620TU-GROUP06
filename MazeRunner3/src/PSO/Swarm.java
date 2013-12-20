package PSO;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.vecmath.Vector3f;

import Drawing.VisibleObject;
import MainGame.Maze;
import MazeObjects.Floor;
import Physics.Physics;

public class Swarm {
	
	private ArrayList<Particle> swarm;
	private Vector3f globalbest = new Vector3f();
	private Maze maze;
	private Physics physics;
	private float c_cog;
	private float c_soc;
	private float inertiaWeight;
	private static int numberofParticles;
	private int diff;
	private float scale;
	
	public Swarm(Physics physics, Maze maze, int n, int difficulty){
		numberofParticles = n;
		this.physics = physics;
		this.maze = maze;
		this.swarm = new ArrayList<Particle>(numberofParticles);
		this.globalbest = physics.getPlayerPosition();
		this.diff = difficulty;
		switch(diff){
		default:
			scale = 0.75f;
			break;
		case(1):
			scale = 1.0f;
			break;
		case(2):
			scale = 1.25f;
			break;
		}
	}
	
	public void generate(int numberOfParticles){
		int n = 0;
		while(n != (numberOfParticles)){
			Vector3f temploc = genLocation((float)maze.MAZE_SIZE);
			swarm.add(new Particle(temploc, this, n, diff));
			n++;
		}
	}
	
	public void AddToVisible(ArrayList<VisibleObject> a){
		for(Particle s: swarm){
			a.add(s);
		}
	}
	
	private Vector3f genLocation(float Bound){
		float randX = Bound*((float) Math.random());
		float randZ = Bound*((float) Math.random());
		
		if(!(maze.get((int)randX,(int)randZ) instanceof Floor)){
			return genLocation(Bound);
		}
		else{
			return new Vector3f(maze.SQUARE_SIZE*randX, scale, maze.SQUARE_SIZE*randZ);
		}
	}
	
	public float getCognitive(){
		return c_cog;
	}
	
	public float getSocial(){
		return c_soc;
	}
	
	public float getInertiaWeight(){
		return inertiaWeight;
	}
	
	public void setInertiaWeight(float i){
		inertiaWeight = i;
	}
	
	public void setCognitive(float c){
		c_cog = c;
	}
	
	public Vector3f getGlobalBest(){
		return globalbest;
	}
	
	public void setSocial(float s){
		c_soc = s;
	}
	
	public void init(GL gl){
		for(Particle s: swarm){
			s.init(gl);
		}
	}
	
	public ArrayList<Particle> getSwarm(){
		return swarm;
	}
	
	public static int getNumOfParticles(){
		return numberofParticles;
	}
	
	public Maze getMaze(){
		return maze;
	}
	
	public Physics getPhysics(){
		return physics;
	}
	
	public void pause(){
		for(Particle s: swarm){
			s.pause();
		}
	}
	
	public void update(int deltaTime){
		int index = 0;
		for(Particle s: swarm){
			s.setLoc(physics.getParticleLocation(index));
			s.update(deltaTime);
			
			//normally you want to change particle velocity, we want to change it to force to have actual physics
			physics.applyParticleForce(index, s.getVelocity());
			
			//This line below replaces actual PSO, never stops (always lock to player)
			globalbest = physics.getPlayerPosition();
			
			/* THIS CODE IS ACTUAL PSO. STOPS WHEN PLAYER = FOUND!!!!
			Vector3f localfitnessVect = physics.getPlayerPosition();
			localfitnessVect.sub(s.getBest());
			Vector3f globalbestFitnessVect = physics.getPlayerPosition();
			globalbestFitnessVect.sub(globalbest);
			
			if(localfitnessVect.length() < globalbestFitnessVect.length()){
				globalbest = s.getBest();
			}*/
			
			index++;
		}
	}

}
