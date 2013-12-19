package MainGame;

import java.util.Calendar;

import Audio.Audio;

import com.bulletphysics.ContactProcessedCallback;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.dynamics.RigidBody;

public class Contact extends ContactProcessedCallback {
	
	private long prevTime = Calendar.getInstance().getTimeInMillis();

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
		return false;
	}

}
