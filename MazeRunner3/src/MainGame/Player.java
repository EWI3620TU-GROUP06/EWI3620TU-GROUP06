package MainGame;
/**
 * Player represents the actual player in MazeRunner.
 * <p>
 * This class extends GameObject to take advantage of the already implemented location 
 * functionality. Furthermore, it also contains the orientation of the Player, ie. 
 * where it is looking at and the player's speed. 
 * <p>
 * For the player to move, a reference to a Control object can be set, which can then
 * be polled directly for the most recent input. 
 * <p>
 * All these variables can be adjusted freely by MazeRunner. They could be accessed
 * by other classes if you pass a reference to them, but this should be done with 
 * caution.
 * 
 * @author Bruno Scheele
 *
 */
public class Player extends GameObject {	
	private double horAngle, verAngle;
	private Vector3f speed;
	private final double acceleration = 0.000075;
	private final double friction = 0.0075;	
	
	private Control control = null;
	
	/**
	 * The Player constructor.
	 * <p>
	 * This is the constructor that should be used when creating a Player. It sets
	 * the starting location and orientation.
	 * <p>
	 * Note that the starting location should be somewhere within the maze of 
	 * MazeRunner, though this is not enforced by any means.
	 * 
	 * @param x		the x-coordinate of the location
	 * @param y		the y-coordinate of the location
	 * @param z		the z-coordinate of the location
	 * @param h		the horizontal angle of the orientation in degrees
	 * @param v		the vertical angle of the orientation in degrees
	 */
	public Player( double x, double y, double z, double h, double v ) {
		// Set the initial position and viewing direction of the player.
		super( x, y, z );
		horAngle = h;
		verAngle = v;
		speed = new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	/**
	 * Sets the Control object that will control the player's motion
	 * <p>
	 * The control must be set if the object should be moved.
	 * @param input
	 */
	public void setControl(Control control)
	{
		this.control = control;
	}
	
	/**
	 * Gets the Control object currently controlling the player
	 * @return
	 */
	public Control getControl()
	{
		return control;
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
	
	/**
	 * Returns the speed.
	 * @return the speed
	 */
	public Vector3f getSpeed() {
		return speed;
	}
	/**
	 * Sets the speed.
	 * @param speed the speed to set
	 */
	public void setSpeed(Vector3f speed) {
		this.speed = speed;
	}


	/**
	 * Updates the physical location and orientation of the player
	 * @param deltaTime The time in milliseconds since the last update.
	 */
	public void update(int deltaTime)
	{
		if (control != null)
		{
			control.update();

			// Rotate the player, according to control
                        int dX = control.getdX();
                        int dY = control.getdY();
                        // Set the new angles according to path length = r*phi, phi = pathlength/r, r=1.
                        setHorAngle(horAngle - (double) dX/10);
                        setVerAngle(verAngle - (double) dY/10);

                      //Update speed according to friction
            			
            			speed.mul( 1 - (float) friction);
            			
            			// Move the player according to speed
            			
            			this.setLocationX(this.getLocationX() + deltaTime * speed.getX());
            			this.setLocationZ(this.getLocationZ() + deltaTime * speed.getZ());
            			
            			// Update speed according to control
            			double cos = Math.cos(Math.toRadians(this.getHorAngle()));
            			double sin = Math.sin(Math.toRadians(this.getHorAngle()));
            			
            			if (control.getRight())
            			{
            				speed.setX(speed.getX() + (float)(acceleration*cos));
            				speed.setZ(speed.getZ() - (float)(acceleration*sin));
            			}
            			if (control.getLeft())
            			{
            				speed.setX(speed.getX() - (float)(acceleration*cos));
            				speed.setZ(speed.getZ() + (float)(acceleration*sin));
            			}
            			if (control.getBack())
            			{
            				speed.setX(speed.getX() + (float)(acceleration*sin));
            				speed.setZ(speed.getZ() + (float)(acceleration*cos));
            			}
            			if (control.getForward())
            			{
            				speed.setX(speed.getX() - (float)(acceleration*sin));
            				speed.setZ(speed.getZ() - (float)(acceleration*cos));
            			}
		}
	}
}