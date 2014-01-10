package MazeObjects;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class Ramp extends MazeObject{
	
	private static Texture texture;
	
	public Ramp(float width, float height, float x, float y, float z)
	{
		super(false);
		this.width = width;
		this.height = height;
		addVertex(new Vector3f(x, y, z));
		addVertex(new Vector3f(x+width, y, z));
		addVertex(new Vector3f(x+width, y + height, z));
		addVertex(new Vector3f(x, y + height, z));
		addVertex(new Vector3f(x, y, z+width));
		addVertex(new Vector3f(x+width, y, z+width));
		
		int[] face0 = {0, 3, 2, 1};
		addFace(face0);
		
		int[] face1 = {1, 2, 5};
		addFace(face1);
		
		int[] face2 = {0, 1, 5, 4};
		addFace(face2);
		
		int[] face3 = {0, 4, 3};
		addFace(face3);
		
		int[] face4 = {2, 3, 4, 5};
		addFace(face4);
		
		this.yMin = y;
		
		restitution = 0.0f;
	}
	
	public Ramp(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces, float width, float height)
	{
		super(vertices, texVertices, faces);
		restitution = 0.0f;
		
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
	
	public MazeObject translate(float x, float y, float z)
	{
		Ramp res = (Ramp)this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x, y, z));
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
		return new Ramp(vertices, this.texVertices, faces, this.width, this.height);
	}
	
	public boolean equals(Object other)
	{
		if( other instanceof Ramp)
		{
			Ramp that = (Ramp) other;
			return this.height == that.height && this.width == that.width;
		}
		return false;
	}
	
}
