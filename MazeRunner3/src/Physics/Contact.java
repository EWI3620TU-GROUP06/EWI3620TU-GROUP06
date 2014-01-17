package Physics;

import java.util.ArrayList;
import java.util.Calendar;

//import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import Audio.Audio;
import LevelHandling.Maze;
//import MainGame.MazeRunner;

import com.bulletphysics.ContactProcessedCallback;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.util.ObjectArrayList;

public class Contact extends ContactProcessedCallback {
	
	private boolean firstrun = true;
	private long prevTime;
	private long prevTimeWall;
	ObjectArrayList<CollisionObject> walls = Physics.getWalls();
	ArrayList<RigidBody> particles = Physics.getParticles();
	private long[] prevTimeParticles = new long[particles.size()];
	private long[] prevTimePartWalls = new long[particles.size()];
	private long prevTimePartPlayer;
	private int diff = Physics.getDiff();
	private float multiplier;
	private int Mazesize = (Maze.MAZE_SIZE_X + Maze.MAZE_SIZE_X)/ 2 *Maze.SQUARE_SIZE;

	public void initMultiplier(){
		switch(diff){
		default: multiplier = 2.5f;break;
		case 1: multiplier = 3.25f;break;
		case 2: multiplier = 4f; break;
		}
	}
	
	@Override
	public boolean contactProcessed(ManifoldPoint arg0, Object arg1, Object arg2) {
		
		RigidBody body1 = (RigidBody) arg1;
		RigidBody body2 = (RigidBody) arg2;
		
		boolean body1IsPlayer = (body1 == Physics.getPlayerBody());
		boolean body1IsWall = walls.contains(body1);
		boolean body1IsParticle = particles.contains(body1);
		
		boolean body2IsPlayer = (body2 == Physics.getPlayerBody());
		boolean body2IsWall = walls.contains(body2);
		boolean body2IsParticle = particles.contains(body2);
		
		if((body1IsPlayer && !body2IsWall && !body2IsParticle) || (body2IsPlayer && !body1IsWall && !body1IsParticle))
		{	
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if(firstrun){
				prevTime = currentTime;
				firstrun = false;
			}
			int deltaTime = (int)(currentTime - prevTime);
			if(deltaTime > 100){
				Vector3f out = new Vector3f();
				Physics.getPlayerBody().getLinearVelocity(out);
				Audio.setVolume("balldrop",(out.length()/2)-20f);
				Audio.playSound("balldrop");
			}
			prevTime = currentTime;
		}
		
		if((body1IsPlayer && body2IsParticle) || (body2IsPlayer && body1IsParticle))
		{
			long currentTimePartPlayer = Calendar.getInstance().getTimeInMillis();
			int id = particles.indexOf(body1);
			if(id == -1){
				id = particles.indexOf(body2);
			}
			if(firstrun){
				prevTimePartPlayer = currentTimePartPlayer;
				firstrun = false;
			}
			int deltaTime = (int)(currentTimePartPlayer - prevTimePartPlayer);
			if(deltaTime > 100){
				Vector3f out = new Vector3f();
				Vector3f outPart = new Vector3f();
				Physics.getPlayerBody().getLinearVelocity(out);
				particles.get(id).getLinearVelocity(outPart);
				Audio.setVolume("ballcollide",((out.length()+outPart.length())/2)-20f);
				Audio.playSound("ballcollide");
			}
			prevTimePartPlayer = currentTimePartPlayer;
		}
		
		//Onderstaande afzonderlijke wall-checker moet er zijn, anders komt de deltaTime nooit > 100 voor muren!
		if((body1IsWall && body2IsPlayer) || (body2IsWall && body1IsPlayer))
		{
			long currentTimeWall = Calendar.getInstance().getTimeInMillis();
			if(firstrun){
				prevTimeWall = currentTimeWall;
				firstrun = false;
			}
			int deltaTimeWall = (int)(currentTimeWall - prevTimeWall);
			if(deltaTimeWall > 100){
				Vector3f out = new Vector3f();
				Physics.getPlayerBody().getLinearVelocity(out);
				Audio.setVolume("wallcollide", out.length()-20f);
				Audio.playSound("wallcollide");
			}
			prevTimeWall = currentTimeWall;
		}
		
		if((body1IsParticle && !body2IsWall && !body2IsPlayer) || (body2IsParticle && !body1IsWall && !body1IsPlayer))
		{
			long currentTimePart = Calendar.getInstance().getTimeInMillis();
			int id = particles.indexOf(body1);
			
			if(id == -1){
				id = particles.indexOf(body2);
			}
			
			if(firstrun){
				prevTimeParticles[id] = currentTimePart;
				firstrun = false;
			}
			
			int deltaTimePart = (int)(currentTimePart - prevTimeParticles[id]);
			if(deltaTimePart > 100){
				Vector3f out = new Vector3f();
				Vector3f dist = new Vector3f();
				Vector3f playPos =  new Vector3f();
				
				//find the appropriate particles' velocity here and adjust according to distance as well
				particles.get(id).getLinearVelocity(out);
				particles.get(id).getCenterOfMassPosition(dist);
				Physics.getPlayerBody().getCenterOfMassPosition(playPos);
				
				//now here partPos
				dist.sub(playPos);
				if(dist.length() < Mazesize*multiplier/10){
					Audio.setVolume("balldrop", out.length() + Mazesize*multiplier/(10*dist.length())-30f);
					Audio.playSound("balldrop");
				}	
			}
			prevTimeParticles[id] = currentTimePart;
		}
		
		
		//Aparte wall checker again, alleen dan voor particles
		if((body1IsWall && body2IsParticle) || (body2IsWall && body1IsParticle)){
			long currentTimePartWall = Calendar.getInstance().getTimeInMillis();
			int id = particles.indexOf(body1);
			if(id == -1){
				id = particles.indexOf(body2);
			}
			if(firstrun){
				prevTimePartWalls[id] = currentTimePartWall;
				firstrun = false;
			}
			int deltaTimePartWall = (int)(currentTimePartWall - prevTimePartWalls[id]);
			if(deltaTimePartWall > 100){
				Vector3f out = new Vector3f();
				Vector3f dist = new Vector3f();
				Vector3f playPos =  new Vector3f();
				
				//find the appropriate particles' velocity here and adjust according to distance as well
				particles.get(id).getLinearVelocity(out);
				particles.get(id).getCenterOfMassPosition(dist);
				Physics.getPlayerBody().getCenterOfMassPosition(playPos);
				
				//now here partPos
				dist.sub(playPos);
				if(dist.length() < Mazesize*multiplier/10){
					Audio.setVolume("wallcollide", out.length() + Mazesize*multiplier/(10*dist.length())-30f);
					Audio.playSound("wallcollide");
				}
			}
			prevTimePartWalls[id] = currentTimePartWall;
		}
		
		if(body1IsParticle && body2IsParticle)
		{
			long currentTimePartPlayer = Calendar.getInstance().getTimeInMillis();
			int id1 = particles.indexOf(body1);
			int	id2 = particles.indexOf(body2);
			
			if(firstrun){
				prevTimePartPlayer = currentTimePartPlayer;
				firstrun = false;
			}
			int deltaTime = (int)(currentTimePartPlayer - prevTimePartPlayer);
			if(deltaTime > 100){
				Vector3f out = new Vector3f();
				Vector3f outPart = new Vector3f();
				Vector3f distanceToPlayer = new Vector3f();
				Vector3f collisionLocation = new Vector3f();
				particles.get(id1).getCenterOfMassPosition(collisionLocation);
				Physics.getPlayerBody().getCenterOfMassPosition(distanceToPlayer);
				distanceToPlayer.sub(collisionLocation);
				particles.get(id1).getLinearVelocity(out);
				particles.get(id2).getLinearVelocity(outPart);
				Audio.setVolume("ballcollide",((out.length()+outPart.length())/2) - distanceToPlayer.length() - 30f);
				Audio.playSound("ballcollide");
				System.out.println("particle vs particle");
			}
			prevTimePartPlayer = currentTimePartPlayer;
		}
		
		/*if(body1.equals(Physics.camera)|| body2.equals(Physics.camera))
		{
			Vector3f posPlayer = new Vector3f();
			Vector3f posCam = new Vector3f();
			Physics.camera.getCenterOfMassPosition(posCam);
			Physics.getPlayerBody().getCenterOfMassPosition(posPlayer);
			double x = posCam.x + (posPlayer.x - posCam.x)*0.5;
			double y = posCam.y;
			double z = posCam.z + (posPlayer.z - posCam.z)*0.5;
			MazeRunner.camera.setLocation(new Vector3d(x,y,z));
			MazeRunner.camColl = true;
			System.out.println("iets gedaan");
		}*/
		
		return false;
	}
}
