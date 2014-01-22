package MazeObjects;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import LevelHandling.Maze;

import com.sun.opengl.util.texture.Texture;

public class Bottom extends MazeObject {

	public Bottom(float width, float x, float y, float z){
		super(false);
		this.width = width;
		height = 0;
		this.yMin = y;
		addVertex(new Vector3f(x, y, z));
		addVertex(new Vector3f(x, y, z + width));
		addVertex(new Vector3f(x + width, y, z + width));
		addVertex(new Vector3f(x + width, y, z));
		
		restitution = 0.0f;
	}
	
	public Bottom(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces)
	{
		super(vertices, texVertices, faces, new int[]{0,0,0});
		width = Maze.SQUARE_SIZE;
		calculateYMin();
		calculateHeight();
		restitution = 0.8f;
	}
	
	public Texture getTexture()
	{
		return null;
	}
	
	public MazeObject translate(float x, float y, float z)
	{
		Bottom res = (Bottom)this.clone();
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
		
		return new Bottom(vertices, this.texVertices, new ArrayList<Face>());
	}
	
	public boolean equals(Object other)
	{
		return other instanceof Bottom;
	}
	
	//The bottom cannot be rotated.
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
