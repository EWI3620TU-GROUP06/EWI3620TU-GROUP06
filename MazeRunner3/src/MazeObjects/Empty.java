package MazeObjects;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class Empty extends MazeObject {
	
	public Empty (float height, float x, float y, float z)
	{
		super(true);
		addVertex(new Vector3f(x, y, z));
		addVertex(new Vector3f(x, y + height, z));
		
		yMin = y;
		this.height = height;
	}

	public Empty(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices,
			ArrayList<Face> faces, float height) {
		super(vertices, texVertices, faces);
		this.height = height;
	}

	@Override
	public Texture getTexture() {
		return null;
	}

	@Override
	public MazeObject translate(float x, float y, float z) {
		Empty res = (Empty) this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x,y,z));
		res.calculateYMin();
		return res;
	}

	@Override
	public MazeObject clone() {
		ArrayList<Vector3f> vertices = new  ArrayList<Vector3f> ();
		vertices.addAll(this.vertices);
		return new Empty(vertices, texVertices, faces, height);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Empty;
	}
	
	//empty cannot be rotated.
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
