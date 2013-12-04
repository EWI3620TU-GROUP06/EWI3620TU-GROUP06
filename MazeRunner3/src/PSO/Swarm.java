package PSO;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.vecmath.Vector3f;

import Drawing.VisibleObject;
import MainGame.Maze;
import MainGame.Physics;
import MazeObjects.Floor;

public class Swarm {
	
	private ArrayList<Particle> swarm;
	private Vector3f globalbest = new Vector3f();
	private Maze maze;
	private Physics physics;
	private float c_cog;
	private float c_soc;
	private float inertiaWeight;
	private static int numberofParticles;
	
	
	//TODO: Voor de bound moet(!) de maze_size mee gepasst worden
	public Swarm(Physics physics, Maze maze, int n){
		numberofParticles = n;
		this.physics = physics;
		this.maze = maze;
		this.swarm = new ArrayList<Particle>(numberofParticles);
		this.globalbest = physics.getPlayerPosition();
	}
	
	public void generate(int numberOfParticles){
		int n = 0;
		while(n != (numberOfParticles)){
			Vector3f temploc = genLocation((float)maze.MAZE_SIZE);
			swarm.add(new Particle(temploc, this));
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
			return new Vector3f(maze.SQUARE_SIZE*randX, 1.0f, maze.SQUARE_SIZE*randZ);
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
	
	public Physics getPhysics(){
		return physics;
	}
	
	public void update(){
		int index = 0;
		for(Particle s: swarm){
			s.setLoc(physics.getParticleLocation(index));
			s.update();
			
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
