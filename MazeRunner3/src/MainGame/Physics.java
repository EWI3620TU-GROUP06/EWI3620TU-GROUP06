package MainGame;

import java.util.ArrayList;

import Audio.Audio;
import MazeObjects.MazeObject;
import PSO.Particle;
import PSO.Swarm;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.dispatch.CollisionWorld.RayResultCallback;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class Physics {

	private float angularDamping = 10f;

	private float mass = 25f;

	boolean previous = false;
	
	private CollisionShape ballShape;
	private Vector3f ballInertia;
	private CollisionShape partBallShape;
	private int diff;
	private float partradius;
	
	ArrayList<RigidBody> particles;
	ArrayList<RigidBody> movingBoxes = new ArrayList<RigidBody>();

	ObjectArrayList<CollisionObject> walls = new ObjectArrayList<CollisionObject>();
	ObjectArrayList<CollisionObject> floors = new ObjectArrayList<CollisionObject>();
	/**
	 * The container for the JBullet physics world. This represents the collision data and motion data, as well as the
	 * algorithms for collision detection and reaction.
	 */
	private static DiscreteDynamicsWorld dynamicsWorld;
	/**
	 * The red spherical rigid body that can be pulled towards the camera by pressing the left mouse button.
	 */
	private static RigidBody playerBall;

	public Physics(Maze maze, int difficulty)
	{		
		this.diff = difficulty;
		Transform DEFAULT_BALL_TRANSFORM = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f((float)maze.getStart()[0], (float) maze.getStart()[1], (float)maze.getStart()[2]), 1.0f));
		/**
		 * The object that will roughly find out whether bodies are colliding.
		 */
		BroadphaseInterface broadphase = new DbvtBroadphase();
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		/**
		 * The object that will accurately find out whether, when, how, and where bodies are colliding.
		 */
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
		/**
		 * The object that will determine what to do after collision.
		 */
		ConstraintSolver solver = new SequentialImpulseConstraintSolver();
		// Initialise the JBullet world.
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		// Set the gravity to 10 metres per second squared (m/s^2). Gravity affects all bodies with a mass larger than
		// zero.
		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
		// Initialise 'groundShape' to a static plane shape on the origin facing upwards ([0, 1, 0] is the normal).
		// 0.25 metres is an added buffer between the ground and potential colliding bodies, used to prevent the bodies
		// from partially going through the floor. It is also possible to think of this as the plane being lifted 0.25m.
		// CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 0.25f);
		for(int i = 0; i < maze.MAZE_SIZE; i++)
			for(int j = 0; j < maze.MAZE_SIZE; j++)
			{

				MazeObject mazeObject = maze.get(i, j);
				for(int k = 0; k < mazeObject.getNumFaces(); k++)
				{
					ConvexHullShape thisShape = new ConvexHullShape(new ObjectArrayList<Vector3f>());
					int[] face = mazeObject.getFace(k);
					for(int l = 0; l < face.length; l++)
					{
						thisShape.addPoint(mazeObject.getVertex(face[l]));
					}
					MotionState faceMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1),
							new Vector3f(0, 0, 0), 1.0f)));
					RigidBodyConstructionInfo faceConstructionInfo = new RigidBodyConstructionInfo(0, faceMotionState, thisShape, new Vector3f(0, 0, 0));

					faceConstructionInfo.restitution = mazeObject.getRestitution();
					RigidBody faceRigidBody = new RigidBody(faceConstructionInfo);
					if(mazeObject.isNormalHorizontal(k))
						walls.add(faceRigidBody);
					else
						floors.add(faceRigidBody);
					dynamicsWorld.addRigidBody(faceRigidBody);
				}

			}

		// Initialise 'ballShape' to a sphere with a radius of 3 metres.
		ballShape = new SphereShape(1);
		// Initialise 'ballMotion' to a motion state that assigns a specified location to the ball.
		MotionState ballMotion = new DefaultMotionState(DEFAULT_BALL_TRANSFORM);
		// Calculate the ball's inertia (resistance to movement) using its mass (2.5 kilograms).
		ballInertia = new Vector3f(0, 0, 0);
		ballShape.calculateLocalInertia(mass, ballInertia);
		// Composes the ball's construction info of its mass, its motion state, its shape, and its inertia.
		RigidBodyConstructionInfo ballConstructionInfo = new RigidBodyConstructionInfo(mass, ballMotion, ballShape, ballInertia);
		// Set the restitution, also known as the bounciness or spring, to 0.5. The restitution may range from 0.0
		// not bouncy) to 1.0 (extremely bouncy).
		ballConstructionInfo.restitution = 0.4f;
		ballConstructionInfo.angularDamping = angularDamping;
		// Initialise 'controlBall', the final variable representing the controlled ball, to a rigid body with the
		// previously assigned construction information.
		playerBall = new RigidBody(ballConstructionInfo);
		// Disable 'sleeping' for the controllable ball.
		playerBall.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
		// Add the control ball to the JBullet world.
		dynamicsWorld.addRigidBody(playerBall);
	}
	
	public void initParticles(Swarm swarm){
		switch(diff){
			default:
				partBallShape = new SphereShape(0.75f);
				partradius = 0.75f;
				break;
			case(1):
				partBallShape = new SphereShape(1f);
				partradius = 1.0f;
				break;
			case(2):
				partBallShape = new SphereShape(1.25f);
				partradius = 1.25f;
				break;
		}
		int n = Swarm.getNumOfParticles();
		particles = new ArrayList<RigidBody>(n);
		
		for(Particle s: swarm.getSwarm()){
			Transform transform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), s.getLoc(), 1.0f));
			MotionState pietmotion = new DefaultMotionState(transform);
			RigidBodyConstructionInfo partConstrInfo = new RigidBodyConstructionInfo(mass, pietmotion, partBallShape, ballInertia);
			partConstrInfo.angularDamping = 10f;
			partConstrInfo.restitution = 0.4f;
			RigidBody piet = new RigidBody(partConstrInfo);
			dynamicsWorld.addRigidBody(piet);
			piet.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
			piet.setLinearVelocity(s.getVelocity());
			particles.add(piet);
		}
	}

	public void update(int deltaTime)
	{
		// Runs the JBullet physics simulation for the specified time in seconds.
		dynamicsWorld.stepSimulation(deltaTime/1000f);
				
		//TODO:Dit stuk hieronder is de grote boosdoener voor FPS. Dit doen we voor geluid
//		boolean contact = getContact();
//		if(contact && !previous)
//		{
//			Audio.playSound("tick");
//		}
//		previous = contact;
	}

	public void applyForce(float x, float y, float z)
	{
		Vector3f force = new Vector3f(x, y, z);
		// Wake the controllable ball if it is sleeping.
		playerBall.activate(true);
		// Apply the force to the controllable ball.
		playerBall.applyCentralForce(force);
	}
	
	public Vector3f getParticleLocation(int index){
		Vector3f out = new Vector3f();
		particles.get(index).getCenterOfMassPosition(out);
		return out;
	}
	
	public Vector3f getParticleVelocity(int index){
		Vector3f out = new Vector3f();
		particles.get(index).getLinearVelocity(out);
		return out;
	}
	
	public void applyParticleForce(int index, Vector3f v){
		particles.get(index).applyCentralForce(new Vector3f(10*v.x, 10*v.y, 10*v.z));
	}

	public Vector3f getPlayerPosition()
	{
		Vector3f res = new Vector3f();
		playerBall.getCenterOfMassPosition(res);
		return res;
	}

	public boolean getLowerContact(){
		Vector3f toVect = getPlayerPosition();
		toVect.sub(new Vector3f(0,1f,0));
		RayResultCallback a = new CollisionWorld.ClosestRayResultCallback(getPlayerPosition(), toVect);
		dynamicsWorld.rayTest(getPlayerPosition(), toVect, a);
		return a.hasHit();
	}
	
	public boolean getLowerParticleContact(Vector3f particlelocation){
		Vector3f toVect = new Vector3f(particlelocation.x, particlelocation.y, particlelocation.z);
		toVect.sub(new Vector3f(0,partradius,0));
		RayResultCallback a = new CollisionWorld.ClosestRayResultCallback(getPlayerPosition(), toVect);
		dynamicsWorld.rayTest(particlelocation, toVect, a);
		return a.hasHit();
	}
	
	public CollisionObject getLineofSight(Vector3f fromVect, Vector3f toVect){
		RayResultCallback a = new CollisionWorld.ClosestRayResultCallback(fromVect, toVect);
		dynamicsWorld.rayTest(fromVect, toVect, a);
		return a.collisionObject;
	}

	public boolean getContact(){
		for(double theta = 0; theta <= 2*Math.PI; theta += 0.25*Math.PI)
		{
			Vector3f toVect = getPlayerPosition();
			toVect.add(new Vector3f((float)Math.cos(theta),0,(float)Math.sin(theta)));
			RayResultCallback a = new CollisionWorld.ClosestRayResultCallback(getPlayerPosition(), toVect);
			dynamicsWorld.rayTest(getPlayerPosition(), toVect, a);
			if(a.hasHit()){
				return true;
			}
		}
		return false;
	}

	public void clearForces(){
		dynamicsWorld.clearForces();
	}
	
	public void addBox(Vector3f position, float size, float height)
	{
		size =  size / 2;
		height = height / 2;
		position.add(new Vector3f(size, height, size));
		CollisionShape boxShape = new BoxShape(new Vector3f(size, height, size));
		boxShape.setMargin(0.1f);
		MotionState boxMotion = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1),
				position, 1.0f)));
	
		RigidBodyConstructionInfo boxConstructionInfo = new RigidBodyConstructionInfo(100, boxMotion, boxShape, new Vector3f(0, 0, 0));

		boxConstructionInfo.restitution = 0.3f;
		boxConstructionInfo.angularDamping = 10f;

		RigidBody newBox = new RigidBody(boxConstructionInfo);
		movingBoxes.add(newBox);
		dynamicsWorld.addRigidBody(newBox);
	}
	
	public Vector3f moveBox(int index, Vector3f speed)
	{
		RigidBody toBeMoved = movingBoxes.get(index);
		toBeMoved.activate();
		toBeMoved.setLinearVelocity(speed);
		Vector3f currentPosition = new Vector3f();
		toBeMoved.getCenterOfMassPosition(currentPosition);
		return currentPosition;
	}
	
	public Vector3f getBoxLocation(int index)
	{
		Vector3f currentPosition = new Vector3f();
		movingBoxes.get(index).getCenterOfMassPosition(currentPosition);
		return currentPosition;
	}
}
