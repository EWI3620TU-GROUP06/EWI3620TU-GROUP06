package Physics;

import java.util.Calendar;

import Audio.Audio;

import com.bulletphysics.ContactProcessedCallback;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.util.ObjectArrayList;

public class Contact extends ContactProcessedCallback {
	
	private long prevTime = Calendar.getInstance().getTimeInMillis();
	private long prevTimeWall = Calendar.getInstance().getTimeInMillis();
	ObjectArrayList<CollisionObject > walls = Physics.getWalls();

	@Override
	public boolean contactProcessed(ManifoldPoint arg0, Object arg1, Object arg2) {
		// TODO Sound
		
		RigidBody body1 = (RigidBody) arg1;
		RigidBody body2 = (RigidBody) arg2;
		if(body1 == Physics.getPlayerBody() || body2 == Physics.getPlayerBody()){
			long currentTime = Calendar.getInstance().getTimeInMillis();
			int deltaTime = (int)(currentTime - prevTime);
			if(deltaTime > 100){
				Audio.playSound("tick");
			}
			prevTime = currentTime;
		}
		if((walls.contains(body1) && body2 == Physics.getPlayerBody()) || (walls.contains(body2) && body1 == Physics.getPlayerBody())){
			long currentTimeWall = Calendar.getInstance().getTimeInMillis();
			int deltaTimeWall = (int)(currentTimeWall - prevTimeWall);
			if(deltaTimeWall > 100){
				Audio.playSound("tick");
			}
			prevTimeWall = currentTimeWall;
		}
		return false;
	}

}
