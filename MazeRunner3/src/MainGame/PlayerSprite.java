package MainGame;

import java.io.InputStream;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import Drawing.VisibleObject;

import com.sun.opengl.impl.GLUquadricImpl;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

/**
 * Class containing the texture, shape and position of the sprite that represents the player. 
 *
 */

public class PlayerSprite implements VisibleObject {

	private double posX, posY, posZ;

	private double dX, dZ;
	private double orientation;
	private double totalRotation;

	private Texture sphereTexture;
	GLUquadricImpl sphere;

	public PlayerSprite(float squareSize, double x, double y, double z, float angle)
	{
		posX = x;
		posY = y;
		posZ = z;

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
		try{
			InputStream stream = getClass().getResourceAsStream("ball.jpg");
			TextureData data = TextureIO.newTextureData(stream, false, "jpg");
			this.sphereTexture = TextureIO.newTexture(data);
			stream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public void display(GL gl) {
		GLU glu = new GLU();
		float ballColour[] = {1.0f, 1.0f, 1.0f, 1.0f};
		sphereTexture.enable(); // Enable the ball texture
		sphereTexture.bind(); 
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, ballColour, 0);
		gl.glPushMatrix();
		gl.glTranslated(posX, posY, posZ);


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
	public void update(double x, double y, double z)
	{
		dX = posX - x;
		dZ = posZ - z;
		if(dX !=0 || dZ != 0)
			orientation = Math.atan2(dZ,dX);

		posX = x;
		posY = y;
		posZ = z;
	}
	


}

