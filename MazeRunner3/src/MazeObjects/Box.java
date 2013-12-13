package MazeObjects;

import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class Box extends MazeObject{
	
	protected float height;
	private static Texture texture;
	
	public Box(float width, float height, float x, float z)
	{
		super(false);
		addVertex(new Vector3f(x, 0, z));
		addVertex(new Vector3f(x+width, 0, z));
		addVertex(new Vector3f(x+width, height, z));
		addVertex(new Vector3f(x, height, z));
		addVertex(new Vector3f(x, 0, z+width));
		addVertex(new Vector3f(x+width, 0, z+width));
		addVertex(new Vector3f(x+width, height, z+width));
		addVertex(new Vector3f(x, height, z+width));
		
		this.height= height;
		
		int[] face0 = {0, 1, 5, 4};
		addFace(face0);
		
		int[] face1 = {1, 2, 6, 5};
		addFace(face1);
		
		int[] face2 = {0, 3, 2, 1};
		addFace(face2);
		
		int[] face3 = {4, 7, 3, 0};
		addFace(face3);
		
		int[] face4 = {5, 6, 7, 4};
		addFace(face4);
		
		int[] face5 = {3, 7, 6, 2};
		addFace(face5);
		
		restitution = 0.4f;
	}
	
	public static void addTexture(Texture t)
	{
		texture = t;
	}
	
	public Texture getTexture()
	{
		return texture;
	}
	
	public void moveTo(Vector3f newLocation)
	{
		Vector3f change = new Vector3f();
		change.sub(newLocation, vertices.get(0));
		for(Vector3f vertex : vertices){
			vertex.add(change);
		}
	}

}
