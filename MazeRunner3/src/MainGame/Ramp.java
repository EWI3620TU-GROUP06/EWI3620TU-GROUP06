package MainGame;

public class Ramp extends GraphicsObject{
	
	public Ramp(float width, float height, int orientation)
	{
		super();
		addVertex(new Vector3f(0, 0, 0));
		addVertex(new Vector3f(width, 0, 0));
		addVertex(new Vector3f(width, height, 0));
		addVertex(new Vector3f(0, height, 0));
		addVertex(new Vector3f(0, 0, width));
		addVertex(new Vector3f(width, 0, width));
		
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
		
		this.rotateVerticesY(orientation * 90 + 180, 2.5, 2.5);
	}

}
