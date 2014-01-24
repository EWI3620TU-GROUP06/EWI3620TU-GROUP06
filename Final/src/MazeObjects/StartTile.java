package MazeObjects;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import LevelHandling.Maze;

import com.sun.opengl.util.texture.Texture;

/**
 * This Mazeobject defines the start position of the player, and its rotation defines the orientation of the 
 * player ate the start of the level.
 *  
 *  <p>
 * 
 * Like all MazeObjects, it implements a translate method that returns a copy of the maze object moved to a 
 * different location, and a very weak equals method that acts as a somewhat more specific version 
 * of the instanceof operator.
 *
 */

public class StartTile extends MazeObject {

	private static Texture texture;

	public StartTile(float width, float x, float y, float z)
	{
		super(false);
		this.yMin = y;
		this.width = width;
		this.height = width;
		addVertex(new Vector3f(x, 0, z));
		addVertex(new Vector3f(x, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z));
		addVertex(new Vector3f(x+width, y + width, z));
		addVertex(new Vector3f(x, y + width, z));
		addVertex(new Vector3f(x+width, y + width, z+width));
		addVertex(new Vector3f(x, y + width, z+width));
		
		int face0[] = {0,1,2,3};
		addFace(face0);

		restitution = 0.1f;
	}
	
	public StartTile(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces)
	{
		super(vertices, texVertices, faces);
		width = Maze.SQUARE_SIZE;
		calculateYMin();
		calculateHeight();
		restitution = 0.1f;
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
		StartTile res = (StartTile)this.clone();
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
		
		StartTile res = new StartTile(vertices, this.texVertices, faces);
		this.cloneNormals(res);
		return res;
	}
	
	public boolean equals(Object other)
	{
		return other instanceof StartTile;
	}
	
	//The startTile cannot be rotated around the x and z axes
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
