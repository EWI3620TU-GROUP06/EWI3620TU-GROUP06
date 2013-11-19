package MainGame;

public class Box extends GraphicsObject{
	
	public Box(float width, float height)
	{
		super();
		addVertex(new Vector3f(0, 0, 0));
		addVertex(new Vector3f(width, 0, 0));
		addVertex(new Vector3f(width, height, 0));
		addVertex(new Vector3f(0, height, 0));
		addVertex(new Vector3f(0, 0, width));
		addVertex(new Vector3f(width, 0, width));
		addVertex(new Vector3f(width, height, width));
		addVertex(new Vector3f(0, height, width));
		
		int[] face0 = {0, 3, 2, 1};
		addFace(face0);
		
		int[] face1 = {1, 2, 6, 5};
		addFace(face1);
		
		int[] face2 = {0, 1, 5, 4};
		addFace(face2);
		
		int[] face3 = {0, 4, 7, 3};
		addFace(face3);
		
		int[] face4 = {3, 7, 6, 2};
		addFace(face4);
		
		int[] face5 = {5, 6, 7, 4};
		addFace(face5);
	}

}
