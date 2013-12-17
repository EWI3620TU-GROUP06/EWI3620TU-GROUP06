package GameObjects;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.opengl.impl.GLUquadricImpl;
import com.sun.opengl.util.texture.Texture;

import Drawing.DrawingUtil;
import Drawing.VisibleObject;
import Listening.Control;
import MainGame.Maze;
import MainGame.Physics;

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
public class Player extends GameObject implements VisibleObject {	
	private double horAngle, verAngle;
	Physics physics = null;

	private double dX = 0, dZ = 0;
	private double orientation;
	private double totalRotation;
	
	private float powerFactor = 10;
	private float jumpFactor = 40;
	private int diff;

	private Texture sphereTexture;
	GLUquadricImpl sphere;

	private Control control = null;
	float ballColour[] = {1.0f, 1.0f, 1.0f, 1.0f};

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
	public Player( Vector3d pos, double h, double v, Maze maze, Physics p, int difficulty) {
		// Set the initial position and viewing direction of the player.
		super( pos );
		horAngle = h;
		verAngle = v;
		//speed = new Vector3f(0.0f, 0.0f, 0.0f);
		//TODO: juiste startpositie bepalen
		physics = p;
		this.diff = difficulty;
	}

	public void init(GL gl)
	{
		sphere = new GLUquadricImpl();
		sphere.setTextureFlag(true);
		sphere.setDrawStyle(GLU.GLU_FILL);
		sphere.setOrientation(0);
		sphereTexture = DrawingUtil.initTexture(gl, "ball");
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
	
	public void setColour(float[] colour)
	{
		ballColour = colour;
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
	
	public void multiplySpeed(float f)
	{
		powerFactor = powerFactor * f;
	}
	
	public void multiplyJump(float f)
	{
		jumpFactor = jumpFactor * f;
	}


	/**
	 * Updates the physical location and orientation of the player
	 * @param deltaTime The time in milliseconds since the last update.
	 */
	public void update(int deltaTime)
	{
		control.update();

		// Rotate the player, according to control
		int dX = control.getdX();
		int dY = control.getdY();
		// Set the new angles according to path length = r*phi, phi = pathlength/r, r=1.
		setHorAngle(horAngle - (double) dX/10);
		if(verAngle - (double) dY/10 > -60 && verAngle - (double) dY/10 < 60){
			setVerAngle(verAngle - (double) dY/10);
		}

		physics.update(deltaTime);
		
		Vector3f newPos = physics.getPlayerPosition();
		Vector3f d = new Vector3f(location);
		d.sub(newPos);
		float[] pos = new float[3];
		d.get(pos);
		this.dX = pos[0];
		this.dZ = pos[2];
		if(this.dX !=0 || this.dZ != 0)
			orientation = Math.atan2(this.dZ,this.dX);
		newPos.get(pos);
		location = new Vector3d(pos[0], pos[1], pos[2]);
		
		double cos = Math.cos(Math.toRadians(this.getHorAngle()));
		double sin = Math.sin(Math.toRadians(this.getHorAngle()));

		float power = deltaTime*powerFactor; 

		if (control.getRight())
		{
			physics.applyForce(power*(float)cos , 0, -power* (float)sin);
		}
		if (control.getLeft())
		{
			physics.applyForce(-power*(float)cos , 0, power*(float)sin);
		}
		if (control.getBack())
		{
			physics.applyForce(power*(float)sin , 0, power*(float)cos);
		}
		if (control.getForward())
		{
			physics.applyForce(-power*(float)sin , 0, -power*(float)cos);
		}
		if (control.getJump())
		{	
			if(physics.getLowerContact()){
				physics.clearForces();

				physics.applyForce(0, power*(jumpFactor-8*diff), 0);
			}
		}
	}

	public void display(GL gl) {
		GLU glu = new GLU();
		
		sphereTexture.enable(); // Enable the ball texture
		sphereTexture.bind(); 
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, ballColour, 0);
		gl.glPushMatrix();
		double pos[] = new double[3];
		location.get(pos);
		gl.glTranslated(pos[0], pos[1], pos[2]);
		
		gl.glRotated(-Math.toDegrees(orientation), 0, 1, 0);
		totalRotation += Math.sqrt(dX*dX + dZ*dZ);
		gl.glRotated(Math.toDegrees(totalRotation), 0, 0, 1);
	

		glu.gluSphere(sphere, 1.0, 20, 20);
		sphereTexture.disable();
		
		
		gl.glPopMatrix();

	}
	
	public void pause()
	{
		dX = 0;
		dZ = 0;
	}
	
}
