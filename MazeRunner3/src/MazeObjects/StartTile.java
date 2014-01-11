package MazeObjects;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import LevelHandling.Maze;

import com.sun.opengl.util.texture.Texture;

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


	public void setAngle(float angle)
	{
		rotateVerticesY(angle - rotation[1], 2.5, 2.5);
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
		return new StartTile(vertices, this.texVertices, faces);
	}
	
	public boolean equals(Object other)
	{
		return other instanceof StartTile;
	}

}
