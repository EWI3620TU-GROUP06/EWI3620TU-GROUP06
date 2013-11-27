package MazeObjects;

import javax.vecmath.Vector3f;

public class Ramp extends MazeObject{
	
	protected int orientation;
	protected float height;
	
	public Ramp(float width, float height, int orientation, float x, float z)
	{
		super();
		addVertex(new Vector3f(x, 0, z));
		addVertex(new Vector3f(x+width, 0, z));
		addVertex(new Vector3f(x+width, height, z));
		addVertex(new Vector3f(x, height, z));
		addVertex(new Vector3f(x, 0, z+width));
		addVertex(new Vector3f(x+width, 0, z+width));
		
		int[] face0 = {0, 3, 2, 1};
		addFace(face0);
		
		int[] face1 = {1, 2, 5};
		addFace(face1);
		
		int[] face2 = {0, 4, 5, 1};
		addFace(face2);
		
		int[] face3 = {0, 4, 3};
		addFace(face3);
		
		int[] face4 = {2, 3, 4, 5};
		addFace(face4);
		
		this.rotateVerticesY(orientation, 2.5 + x, 2.5 + z);
		
		this.orientation = orientation;
		this.height =  height;
		
		restitution = 0.0f;
	}

}
