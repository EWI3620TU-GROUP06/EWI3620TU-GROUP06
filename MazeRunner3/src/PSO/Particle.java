package PSO;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3f;

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
	private MazeObject previousTile = null;
	
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
		
		Vector3f oldvelocity = new Vector3f(velocity.x, 0f, velocity.z);
		Vector3f newvelocity = new Vector3f(velocity.x, 0f, velocity.z);
		
		newvelocity = new Vector3f(swarm.getInertiaWeight()*velocity.x,
				0f,
				swarm.getInertiaWeight()*velocity.z);
		
		newvelocity.add(new Vector3f(swarm.getCognitive()*((float)Math.random())*(localbest.x - location.x),
				0f,
				swarm.getCognitive()*((float)Math.random())*(localbest.z - location.z)));
		
		newvelocity.add(new Vector3f(swarm.getSocial()*((float)Math.random())*(swarm.getGlobalBest().x - location.x),
				0f,
				swarm.getSocial()*((float)Math.random())*(swarm.getGlobalBest().z - location.z)));
		
		
		
		oldvelocity.normalize();
		int squaresize = swarm.getMaze().SQUARE_SIZE;
		double mazesize = swarm.getMaze().getSize();
		
		Vector3f nextTileVect = new Vector3f(location.x + squaresize*oldvelocity.x, 
				0, 
				location.z + squaresize*oldvelocity.z); //TODO:think about this one
		
		if((double)location.x < (mazesize - squaresize) &&
				(double)location.x > squaresize &&
				(double)location.z < (mazesize - squaresize) &&
				(double)location.z > squaresize){
			
			MazeObject currentTile = swarm.getMaze().get((int)(location.x/squaresize), 
					(int)(location.z/squaresize));
			
			MazeObject nextTile = swarm.getMaze().get((int)(nextTileVect.x/squaresize),
					(int)(nextTileVect.z/squaresize));
			
			if(!(nextTile instanceof Floor) && 
					!(nextTile instanceof Ramp)){
				
				MazeObject[] neighbourTiles = getNeighbourTiles(squaresize);
				
				if(neighbourTiles[0].getCode() == 0 && !(neighbourTiles[0].equals(previousTile))){ //"up" tile
					velocity.add(new Vector3f(-10f, 0f, 0f)); 
				}
				else if(neighbourTiles[1].getCode() == 0 && !(neighbourTiles[1].equals(previousTile))){ //"left" tile
					velocity.add(new Vector3f(0f, 0f, -10f));
				}
				else if(neighbourTiles[3].getCode() == 0 && !(neighbourTiles[2].equals(previousTile))){ //"right" tile
					velocity.add(new Vector3f(0f, 0f, 10f));
				}
				else if(neighbourTiles[2].getCode() == 0 && !(neighbourTiles[3].equals(previousTile))){ //"down" tile
					velocity.add(new Vector3f(10f, 0f, 0f));
				}
			}
			if(!(nextTile.equals(previousTile))){
				velocity = new Vector3f(newvelocity.x, 0f, newvelocity.y);
			}
			previousTile = currentTile; //TODO:think about this one
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
