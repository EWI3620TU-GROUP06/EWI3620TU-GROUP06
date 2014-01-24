package MazeObjects;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

/**
 * The pit objects is the abyss like MazeObject placed under the lowest visible block of the Maze. It makes it 
 * seem as if the maze is standing on it from somewhere below.
 * 
 * <p>
 * 
 * Like all MazeObjects, it implements a translate method that returns a copy of the maze object moved to a 
 * different location, and a very weak equals method that acts as a somewhat more specific version 
 * of the instanceof operator.
 *
 */

public class Pit extends MazeObject {
	
	private static Texture texture;
	
	public Pit(float width, float height, float x, float y, float z)
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
		
		restitution = 0.4f;
	}

	public Pit(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices,
			ArrayList<Face> faces, float width, float height) {
		super(vertices, texVertices, faces);
		this.width = width;
		this.height = height;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	public static void addTexture(Texture texture)
	{
		Pit.texture = texture;
	}

	@Override
	public MazeObject translate(float x, float y, float z) {
		Pit res = (Pit)this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x, y, z));
		res.calculateYMin();
		return res;
	}

	@Override
	public MazeObject clone() {
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		for(Vector3f vertex : this.vertices)
		{
			vertices.add((Vector3f)vertex.clone());
		}
		ArrayList<Face> faces = new ArrayList<Face>();
		for(Face face : this.faces)
		{
			faces.add(face.clone());
		}
		return new Pit(vertices, this.texVertices, faces, this.width, this.height);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Pit;
	}
	
	//The pit cannot be rotated.
	@Override
	public void rotateVerticesX(float angle, double y, double z)
	{
		// Do Nothing
	}
	
	@Override
	public void rotateVerticesY(float angle, double x, double z)
	{
		// Do Nothing
	}
	
	@Override
	public void rotateVerticesZ(float angle, double x, double y)
	{
		// Do Nothing
	}

}
