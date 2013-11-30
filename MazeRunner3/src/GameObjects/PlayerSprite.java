package GameObjects;


import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3d;

import Drawing.DrawingUtil;
import Drawing.VisibleObject;

import com.sun.opengl.impl.GLUquadricImpl;
import com.sun.opengl.util.texture.Texture;

/**
 * Class containing the texture, shape and position of the sprite that represents the player. 
 *
 */

public class PlayerSprite implements VisibleObject {

	private Vector3d location;

	private double dX, dZ;
	private double orientation;
	private double totalRotation;

	private Texture sphereTexture;
	GLUquadricImpl sphere;

	public PlayerSprite(float squareSize, Vector3d pos, float angle)
	{
		location = pos;

		orientation = angle;
		totalRotation = 0;

	}
	
	/**
	 * Initialize the shape and the textures of the sprite
	 * @param gl	instance of opengl
	 */

	public void init(GL gl)
	{
		sphere = new GLUquadricImpl();
		sphere.setTextureFlag(true);
		sphere.setDrawStyle(GLU.GLU_FILL);
		sphere.setOrientation(0);
		sphereTexture = DrawingUtil.initTexture(gl, "ball");
	}


	@Override
	public void display(GL gl) {
		GLU glu = new GLU();
		float ballColour[] = {1.0f, 1.0f, 1.0f, 1.0f};
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
	
	/**
	 * Updates the location of the sprite
	 * @param x	new X coordinate of the sprite
	 * @param z	new Z coordinate of the sprite
	 */
	public void update(Vector3d newPos)
	{
		Vector3d d = new Vector3d(location);
		d.sub(newPos);
		double pos[] = new double[3];
		d.get(pos);
		dX = pos[0];
		dZ = pos[2];
		if(dX !=0 || dZ != 0)
			orientation = Math.atan2(dZ,dX);

		location = newPos;
	}
	
	public void pause()
	{
		dX = 0;
		dZ = 0;
	}

}

