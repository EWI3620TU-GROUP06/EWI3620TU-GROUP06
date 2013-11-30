package GameObjects;

import javax.vecmath.Vector3d;

/**
 * GameObject is the superclass for all the objects in the game that need a location.
 * <p>
 * Used mostly as a convenience superclass, it helps to avoid tedious retyping of 
 * the get and set methods for the location variables. This class should be inhereted 
 * by all objects having at least a position.
 * 
 * @author Bruno Scheele
 *
 */
public class GameObject {
	protected Vector3d location;

	/**
	 * The default GameObject constructor. 
	 */
	public GameObject() {}
	/**
	 * GameObject constructor with a defined starting position.
	 * 
	 * @param x		the x-coordinate of the location
	 * @param y		the y-coordinate of the location
	 * @param z		the z-coordinate of the location
	 */
	public GameObject( Vector3d pos) {
		location = pos;
	}

	public void setLocation(Vector3d v)
	{
		location = v;
	}
	
	public Vector3d getLocation()
	{
		return location;
	}
}
