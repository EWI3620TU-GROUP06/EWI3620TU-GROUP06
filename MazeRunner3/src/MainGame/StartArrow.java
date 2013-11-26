package MainGame;

import javax.vecmath.Vector3f;

public class StartArrow extends MazeObject {

	private float orientation;

	public StartArrow(float width, float angle, float x, float z)
	{
		super();
		addVertex(new Vector3f(x, 0, z));
		addVertex(new Vector3f(x, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z));
		addVertex(new Vector3f(x+width/2, (float)0.01, z+width/2));
		addVertex(new Vector3f(x+width / 3, (float)0.01, z+width / 3));
		addVertex(new Vector3f(x+width / 2, (float)0.01, z+width));
		addVertex(new Vector3f(x+width * 2 / 3, (float)0.01, z+width / 3));
		

		int face0[] = {0,1,2,3};
		addFace(face0);
		
		int face1[] = {4,5,6,7};
		addFace(face1);

		rotateVerticesY(angle, 2.5, 2.5);
		orientation = angle;
	}

	public void setAngle(float angle)
	{
		rotateVerticesY(angle - orientation, 2.5, 2.5);
		orientation = angle;
	}

}
