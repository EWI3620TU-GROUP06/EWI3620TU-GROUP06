package MazeObjects;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import LevelHandling.Maze;

import com.sun.opengl.util.texture.Texture;

/**
 * The Floor maze object defines a floor tile in the maze.
 *  <p>
 * 
 * Like all MazeObjects, it implements a translate method that returns a copy of the maze object moved to a 
 * different location, and a very weak equals method that acts as a somewhat more specific version 
 * of the instanceof operator.
 *
 */

public class Floor extends MazeObject {

	private static Texture texture;

	public Floor(float width, float x, float y, float z){
		super(false);
		this.width = width;
		height = 0;
		this.yMin = y;
		addVertex(new Vector3f(x, y, z));
		addVertex(new Vector3f(x, y, z + width));
		addVertex(new Vector3f(x + width, y, z + width));
		addVertex(new Vector3f(x + width, y, z));

		int face0[] = {0,1,2,3};
		addFace(face0);

		for(Face face: faces){
			calculateNormal(face);
		}

		restitution = 0.8f;
	}

	public Floor(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces)
	{
		super(vertices, texVertices, faces);
		width = Maze.SQUARE_SIZE;
		calculateYMin();
		calculateHeight();
		restitution = 0.8f;
	}

	public static void addTexture(Texture t)
	{
		texture = t;
	}

	public Texture getTexture()
	{
		return texture;
	}

	public MazeObject translate(float x, float y, float z)
	{
		Floor res = (Floor)this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x, y, z));
		res.calculateYMin();
		return res;
	}

	public MazeObject clone()
	{
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
		for(Face face: faces){
			calculateNormal(face);
		}
		
		Floor res = new Floor(vertices, this.texVertices, faces);
		this.cloneNormals(res);
		return res;
	}

	public boolean equals(Object other)
	{
		return other instanceof Floor;
	}

	//The Floor cannot be rotated around the x and z axes
	@Override
	public void rotateVerticesX(float angle, double y, double z)
	{
		// Do Nothing
	}

	@Override
	public void rotateVerticesZ(float angle, double x, double y)
	{
		// Do Nothing
	}

}
