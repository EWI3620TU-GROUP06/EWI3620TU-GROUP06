package MazeObjects;

import javax.vecmath.Vector3f;

/**
 * the class face is used to give the all the objects its faces, these fases are used to detect collision.
 */

public class Face {

	private int[] vertices;
	private int[] texVertices;
	private Vector3f normal = new Vector3f();
	private boolean enabled = true;

	public Face(int[] vertices)
	{
		this.vertices = vertices;
		this.texVertices = new int[]{0, 1, 2, 3};
	}

	public Face(int[] vertices, int[] texVertices)
	{
		this.vertices = vertices;
		this.texVertices = texVertices;
		enabled = true;
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
	
	public void setEnabled(boolean enable)
	{
		this.enabled = enable;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	
	public Face clone()
	{
		return new Face(vertices, texVertices);
	}

}
