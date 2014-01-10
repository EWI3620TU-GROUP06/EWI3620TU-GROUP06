package MazeObjects;

import java.util.ArrayList;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

/**
 * the class box is used to put the maze object box in the maze. the box used to create walls.
 */

public class Box extends MazeObject{
	
	private static Texture texture;
	
	/**
	 * the object box has an given height and position. when the box is made it gets is faces which are used 
	 * by the physics to detect collision.
	 * @param width		width of the box	
	 * @param height	height of the box 
	 * @param x			x position in the maze
	 * @param y			y position in the maze
	 * @param z			z position in the maze
	 */
	public Box(float width, float height, float x, float y, float z)
	{
		super(false);
		this.width = width;
		this.height = height;
		this.yMin = y;
		addVertex(new Vector3f(x, y, z));
		addVertex(new Vector3f(x+width, y, z));
		addVertex(new Vector3f(x+width, y + height, z));
		addVertex(new Vector3f(x, y + height, z));
		addVertex(new Vector3f(x, y, z+width));
		addVertex(new Vector3f(x+width, y, z+width));
		addVertex(new Vector3f(x+width, y + height, z+width));
		addVertex(new Vector3f(x, y + height, z+width));
		
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
	
	public Box(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces, float width, float height)
	{
		super(vertices, texVertices, faces);
		restitution = 0.1f;
		this.width = width;
		this.height = height;
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
	
	/**
	 * when the object box is translated it is cloned first and then we move the box to a different place in the maze
	 */
	
	public MazeObject translate(float x, float y, float z)
	{
		Box res = (Box)this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x, y, z));
		return res;
	}
	
	/**
	 * the method clone is used by the translate method 
	 */
	public MazeObject clone()
	{
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		for(Vector3f vertex : this.vertices)
		{
			vertices.add((Vector3f)vertex.clone());
		}
		@SuppressWarnings("unchecked")
		ArrayList<Face> faces = (ArrayList<Face>) this.faces.clone();
		return new Box(vertices, this.texVertices, faces, this.width, this.height);
	}
	
	public boolean equals(Object other)
	{
		if( other instanceof Box)
		{
			Box that = (Box) other;
			return this.height == that.height && this.width == that.width;
		}
		return false;
	}

}
