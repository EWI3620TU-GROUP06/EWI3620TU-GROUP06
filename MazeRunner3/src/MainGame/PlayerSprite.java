package MainGame;

import java.io.InputStream;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.impl.GLUquadricImpl;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Single;

public class PlayerSprite implements VisibleObject {

	private double posX, posZ;

	private double dX, dZ;
	private double orientation;
	private double totalRotation;

	private Texture sphereTexture;
	GLUquadricImpl sphere;

	public PlayerSprite(float squareSize, double x, double z, float angle)
	{
		posX = x;
		posZ = z;

		orientation = angle;
		totalRotation = 0;

	}

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
		//GLUT glut = new GLUT();
		float ballColour[] = {1.0f, 1.0f, 1.0f, 1.0f};
		sphereTexture.enable(); // Enable the ball texture
		sphereTexture.bind(); 
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, ballColour, 0);
		gl.glPushMatrix();
		gl.glTranslated(posX + 2.5, 1, posZ + 2.5);


		gl.glRotated(-Math.toDegrees(orientation), 0, 1, 0);
		totalRotation += Math.sqrt(dX*dX + dZ*dZ);
		gl.glRotated(Math.toDegrees(totalRotation), 0, 0, 1);
		glu.gluSphere(sphere, 1.0, 20, 20);
		//glut.glutWireSphere(1.0, 20, 20);
		sphereTexture.disable();

		gl.glPopMatrix();

	}

	public void update(double x, double z, double angle)
	{
		dX = posX - x;
		dZ = posZ - z;
		if(dX !=0 || dZ != 0)
			orientation = Math.atan2(dZ,dX);

		posX = x;
		posZ = z;
	}

}

