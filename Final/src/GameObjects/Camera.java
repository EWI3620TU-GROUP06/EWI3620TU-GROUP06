package GameObjects;

import javax.vecmath.Vector3d;

/**
 * Camera represents the camera player in MazeRunner.
 * <p>
 * This class extends GameObject to take advantage of the already implemented location 
 * functionality. Furthermore, it also contains the orientation of the Camera, ie. 
 * where it is looking at. 
 * <p>
 * All these variables can be adjusted freely by MazeRunner. They could be accessed
 * by other classes if you pass a reference to them, but this should be done with 
 * caution.
 * 
 * @author Bruno Scheele
 *
 */
public class Camera extends GameObject {
	private double horAngle, verAngle;
	private Vector3d vrp;
	private Vector3d vuv;

	public Camera( Vector3d pos, double h, double v ) {
		// Set the initial position and viewing direction of the player.
		super(pos);
		horAngle = h;
		verAngle = v;

		// Calculate a likely view reference point.
		calculateVRP();

		// Set the view up vector to be parallel to the y-axis of the world.
		vuv = new Vector3d(0, 1, 0);
	}

	/**
	 * Calculates the View Reference Point (VRP) of the camera.
	 * 
	 * The VRP of the camera is set to be just 1 in front of the current orientation 
	 * of the camera. This makes it easy to create a first-person view setting, since it 
	 * always looks in front of the player.
	 */
	public void calculateVRP() {
		vrp = new Vector3d( -Math.sin( Math.PI * horAngle / 180 ) * Math.cos( Math.PI * verAngle / 180 ),
				 Math.sin( Math.PI * verAngle / 180),
				 -Math.cos( Math.PI * horAngle / 180 ) * Math.cos( Math.PI * verAngle / 180 ));
		vrp.add(location);
	}

	/**
	 * Returns the horizontal angle of the orientation.
	 * @return the horAngle
	 */
	public double getHorAngle() {
		return horAngle;
	}

	/**
	 * Sets the horizontal angle of the orientation.
	 * @param horAngle the horAngle to set
	 */
	public void setHorAngle(double horAngle) {
		this.horAngle = horAngle;
	}

	/**
	 * Returns the vertical angle of the orientation.
	 * @return the verAngle
	 */
	public double getVerAngle() {
		return verAngle;
	}

	/**
	 * Sets the vertical angle of the orientation.
	 * @param verAngle the verAngle to set
	 */
	public void setVerAngle(double verAngle) {
		this.verAngle = verAngle;
	}
	
	public Vector3d getVrp(){
		return vrp;
	}
	
	public void setVrp(Vector3d vrp){
		this.vrp = vrp;
	}
	
	public Vector3d getVuv(){
		return vuv;
	}
	
	public void setVuv(Vector3d vuv){
		this.vuv = vuv;
	}
}
