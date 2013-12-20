package Physics;

import java.util.ArrayList;
import java.util.Calendar;

import javax.vecmath.Vector3f;

import Audio.Audio;

import com.bulletphysics.ContactProcessedCallback;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.util.ObjectArrayList;

public class Contact extends ContactProcessedCallback {
	
	private boolean firstrun = true;
	private long prevTime;
	private long prevTimeWall;
	private long prevTimePart;
	ObjectArrayList<CollisionObject > walls = Physics.getWalls();
	ArrayList<RigidBody> particles = Physics.getParticles();

	@Override
	public boolean contactProcessed(ManifoldPoint arg0, Object arg1, Object arg2) {
		
		RigidBody body1 = (RigidBody) arg1;
		RigidBody body2 = (RigidBody) arg2;
		if(body1 == Physics.getPlayerBody() || body2 == Physics.getPlayerBody()){
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if(firstrun){
				prevTime = currentTime;
				firstrun = false;
			}
			int deltaTime = (int)(currentTime - prevTime);
			if(deltaTime > 100){
				Vector3f out = new Vector3f();
				Physics.getPlayerBody().getLinearVelocity(out);
				Audio.setVolume("tick",out.length()-20f);
				Audio.playSound("tick");
			}
			prevTime = currentTime;
		}
		
		if((walls.contains(body1) && body2 == Physics.getPlayerBody()) || (walls.contains(body2) && body1 == Physics.getPlayerBody())){
			long currentTimeWall = Calendar.getInstance().getTimeInMillis();
			if(firstrun){
				prevTimeWall = currentTimeWall;
				firstrun = false;
			}
			int deltaTimeWall = (int)(currentTimeWall - prevTimeWall);
			if(deltaTimeWall > 100){
				Vector3f out = new Vector3f();
				Physics.getPlayerBody().getLinearVelocity(out);
				Audio.setVolume("tick", out.length()-20f);
				Audio.playSound("tick");
			}
			prevTimeWall = currentTimeWall;
		}
		
		if((particles.contains(body1) && body2 != Physics.getPlayerBody()) || (particles.contains(body2) && body1 != Physics.getPlayerBody())){
			long currentTimePart = Calendar.getInstance().getTimeInMillis();
			if(firstrun){
				prevTimePart = currentTimePart;
				firstrun = false;
			}
			int deltaTimePart = (int)(currentTimePart - prevTimePart);
			if(deltaTimePart > 100){
//				Vector3f out = new Vector3f();
				//find the appropriate particles' velocity here and adjust according to distance as well
//				particles.get(particles.indexOf(body1)).getLinearVelocity(out);
				
//				Audio.setVolume("tick", out.length()-20f);
//				Audio.playSound("tick");
			}
			prevTimePart = currentTimePart;
		}
		
		return false;
	}

}
