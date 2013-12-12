package MazeObjects;

import javax.vecmath.Vector3f;

public class Face {

	private int[] vertices;
	private int[] texVertices;
	private Vector3f normal;

	public Face(int[] vertices)
	{
		this.vertices = vertices;
		this.texVertices = new int[]{0, 1, 2, 3};
	}

	public Face(int[] vertices, int[] texVertices)
	{
		this.vertices = vertices;
		this.texVertices = texVertices;
	}

	public int getVertex(int index)
	{
		return vertices[index];
	}
	
	public int getTexVertex(int index)
	{
		return texVertices[index];
	}
	
	public void setNormal(Vector3f normal)
	{
		this.normal = normal;
	}
	
	public Vector3f getNormal()
	{
		return normal;
	}
	
	public int getLength()
	{
		return vertices.length;
	}
	
	public int[] getVertices()
	{
		return vertices;
	}

}
