package Audio;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent.Type;

/**
 * the class audio listener enables us to play several sounds simultaneously 
 *
 */
class AudioListener implements LineListener {
	private boolean done = false;
	@Override public synchronized void update(LineEvent event) {
		Type eventType = event.getType();
		if (eventType == Type.STOP || eventType == Type.CLOSE) {
			done = true;
			notifyAll();
		}
	}
	/*
	 * the method waitUnilDone lets the game play a sound only one time and then waits for this sound to stop.
	 */
	public synchronized void waitUntilDone() throws InterruptedException {
		while (!done) { wait();}
	}
}
