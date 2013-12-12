package PSO;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.sun.opengl.impl.GLUquadricImpl;
import com.sun.opengl.util.texture.Texture;

import Drawing.DrawingUtil;
import Drawing.VisibleObject;
import GameObjects.GameObject;
import MazeObjects.*;

public class Particle extends GameObject implements VisibleObject{
	
	private Vector3f location;
	private Vector3f velocity;
	private Vector3f localbest;
	private Swarm swarm;
	private float dX, dZ;
	
	private double orientation;
	private double totalRotation;

	private Texture sphereTexture;
	GLUquadricImpl sphere;
	
	public Particle(Vector3f location, Swarm swarm){
		this.swarm = swarm;
		this.location = location;
		this.velocity = new Vector3f(0,0,0);
		this.localbest = location;
	}
	
	public Vector3f getLoc(){
		return location;
	}
	
	public void setLoc(Vector3f l){
		this.dX = location.x - l.x;
		this.dZ = location.z - l.z;
		this.location = l;
	}
	
	public Vector3f getVelocity(){
		return velocity;
	}
	
	public void setVelocity(Vector3f v){
		this.velocity = v;
	}
	
	public Vector3f getBest(){
		return localbest;
	}
	
	public void update(){
		
		if(dX != 0 && dZ != 0){
			orientation = Math.atan2(dZ,dX);
		}
		
		Vector3f fitnessVect = swarm.getPhysics().getPlayerPosition();
		fitnessVect.sub(location);
		
		Vector3f localbestFitnessVect = swarm.getPhysics().getPlayerPosition();
		localbestFitnessVect.sub(localbest);
		if(fitnessVect.length() < localbestFitnessVect.length()){
			localbest = location;
		}
		
		Vector3f globalbest = swarm.getGlobalBest();
		Vector3f difference = new Vector3f(globalbest.x - location.x, globalbest.y - location.y, globalbest.z - location.z);
		
		//Normalize manually and multiply by 2, to make sure ray is outside the spheres.
		difference = new Vector3f(difference.x/difference.length(), difference.y/difference.length(), difference.z/difference.length());
		
		//Do this to not overwrite the actual globalbest and location with the .add() method
		Vector3f partLocation = new Vector3f(location.x, location.y, location.z);
		
		partLocation.add(difference);
		
		RigidBody LoSbreaker = (RigidBody) swarm.getPhysics().getLineofSight(partLocation, globalbest);
		
		if((LoSbreaker.getCollisionShape() instanceof SphereShape)){
			velocity = new Vector3f(swarm.getInertiaWeight()*velocity.x,
					0f,
					swarm.getInertiaWeight()*velocity.z);
			
			velocity.add(new Vector3f(swarm.getCognitive()*((float)Math.random())*(localbest.x - location.x),
					0f,
					swarm.getCognitive()*((float)Math.random())*(localbest.z - location.z)));
			
			velocity.add(new Vector3f(swarm.getSocial()*((float)Math.random())*(globalbest.x - location.x),
					0f,
					swarm.getSocial()*((float)Math.random())*(globalbest.z - location.z)));
		}
		else if (!(LoSbreaker.getCollisionShape() instanceof SphereShape)){
			//Stand still for now
		}
	}
	
	public MazeObject[] getNeighbourTiles(int squaresize){
		return swarm.getMaze().getNeighbourTiles((int)(location.x/squaresize), (int)(location.z/squaresize));
	}
	
	public void init(GL gl)
	{
		sphere = new GLUquadricImpl();
		sphere.setTextureFlag(true);
		sphere.setDrawStyle(GLU.GLU_FILL);
		sphere.setOrientation(0);
		sphereTexture = DrawingUtil.initTexture(gl, "ball");
	}
	
	public void pause(){
		dX = 0;
		dZ = 0;
	}

	@Override
	public void display(GL gl) {
		GLU glu = new GLU();
		float ballColour[] = {1.0f, 1.0f, 1.0f, 1.0f};
		sphereTexture.enable(); // Enable the ball texture
		sphereTexture.bind(); 
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, ballColour, 0);
		gl.glPushMatrix();
		float pos[] = new float[3];
		location.get(pos);
		gl.glTranslated(pos[0], pos[1], pos[2]);

		gl.glRotated(-Math.toDegrees(orientation), 0, 1, 0);
		totalRotation += Math.sqrt((double)(dX*dX + dZ*dZ));
		gl.glRotated(Math.toDegrees(totalRotation), 0, 0, 1);
		glu.gluSphere(sphere, 1.0, 20, 20);
		sphereTexture.disable();

		gl.glPopMatrix();	
	}

}
