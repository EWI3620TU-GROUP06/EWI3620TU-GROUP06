package MazeObjects;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class Ramp extends MazeObject{
	
	private static Texture texture;
	
	public Ramp(float width, float height, float x, float y, float z)
	{
		super(true);
		this.width = width;
		this.height = height;
		addVertex(new Vector3f(x, y, z));
		addVertex(new Vector3f(x+width, y, z));
		addVertex(new Vector3f(x+width, y + height, z));
		addVertex(new Vector3f(x, y + height, z));
		addVertex(new Vector3f(x, y, z+width));
		addVertex(new Vector3f(x+width, y, z+width));
		texVertices.add(new Vector2f(0,0));
		texVertices.add(new Vector2f(0,1));
		texVertices.add(new Vector2f(1,1));
		texVertices.add(new Vector2f(1,0));
		texVertices.add(new Vector2f(0,height / width));
		texVertices.add(new Vector2f(1,height / width));
		
		int[] texFaceBottom = {0,1,2,3};
		int[] texFaceSide1 = {0,4,3};
		int[] texFaceSide2 = {0,5,3};
		
		int[] face0 = {0, 3, 2, 1};
		addFace(face0, texFaceBottom);
		
		int[] face1 = {1, 2, 5};
		addFace(face1, texFaceSide1);
		
		int[] face2 = {0, 1, 5, 4};
		addFace(face2, texFaceBottom);
		
		int[] face3 = {4, 3, 0};
		addFace(face3, texFaceSide2);
		
		int[] face4 = {2, 3, 4, 5};
		addFace(face4, texFaceBottom);
		
		this.yMin = y;
		
		restitution = 0.0f;
	}
	
	public Ramp(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces, float width, float height, int[] rotation)
	{
		super(vertices, texVertices, faces, new int[]{rotation[0], rotation[1], rotation[2]});
		restitution = 0.0f;
		
		this.width = width;
		this.height = height;
		calculateYMin();

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
		return new Ramp(vertices, this.texVertices, faces, this.width, this.height, this.rotation);
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
