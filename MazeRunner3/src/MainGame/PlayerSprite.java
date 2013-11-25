package MainGame;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class PlayerSprite implements VisibleObject {
	
	//private StartArrow arrow;
	private double posX, posZ;
	
	public PlayerSprite(float squareSize, double x, double z, float angle)
	{
		//arrow = new StartArrow(squareSize, (float) angle);
		posX = x;
		posZ = z;
		
	}
	

	@Override
	public void display(GL gl) {
		// TODO Auto-generated method stub
		GLUT glut = new GLUT();
		//float arrowColour[] = {1.0f, 0.0f, 0.0f, 1.0f };
		float ballColour[] = {1.0f, 0.0f, 0.0f, 1.0f};
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, ballColour, 0);
		gl.glPushMatrix();
		//gl.glTranslated(posX, 0, posZ);
		//arrow.draw(gl, arrowColour);
		gl.glTranslated(posX + 2.5, 1, posZ + 2.5);
		glut.glutSolidSphere(1.0, 20, 20);
		
		gl.glPopMatrix();
		
	}
	
	public void update(double x, double z, double angle)
	{
		//arrow.setAngle((float) angle);
		posX = x;
		posZ = z;
	}

}
