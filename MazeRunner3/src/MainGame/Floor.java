package MainGame;

import javax.vecmath.Vector3f;

public class Floor extends MazeObject {
	
	public Floor(float width, float x, float z){
		super();
		addVertex(new Vector3f(x, 0, z));
		addVertex(new Vector3f(x, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z));

		int face0[] = {0,1,2,3};
		addFace(face0);
		
		restitution = 0.1f;
	}
	

}