package MainGame;


import javax.media.opengl.*;

import java.util.ArrayList;

/**
 *	Class that contains the a number of points, represented by vectors, and faces, represented by arrays of numbers
 *	referencing those points, to create three dimensional objects that are in the maze.
 *
 */
public abstract class GraphicsObject {

	ArrayList<Vector3f> vertices;
	ArrayList<int[]> faces;
	ArrayList<Vector3f> normals;

	public GraphicsObject()
	{
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();
		faces = new ArrayList<int[]>();
	}
	
	/**
	 * Add a vertex to the vertices list
	 * @param point	vertex to be added
	 */

	public void addVertex(Vector3f point)
	{
		vertices.add(point);
	}
	
	/**
	 * Get the number of vertices contain in this object
	 * @return	number of vertices
	 */

	public int getNumVertices()
	{
		return vertices.size();
	}
	
	/**
	 * Add a face to the list of faces
	 * @param face	face to be added
	 */

	public void addFace(int[] face)
	{
		if(face.length < 3)
		{
			System.err.println("Faces should contain at leats three vertices");
			return;
		}
		for(int i = 0; i < face.length; i++)
		{
			if(face[i] > vertices.size())
			{
				System.err.println("Couldn't add face: index out of bounds");
				return;
			}
		}
		normals.add(calculateNormal(face));
		this.faces.add(face);

	}
	
	/**
	 * Draw the object
	 * @param gl
	 * @param wallColour	Colour the object should get
	 */

	public void draw(GL gl, float[] wallColour)
	{
		for(int j = 0; j < faces.size(); j++)
		{

			int[] face = faces.get(j);
			gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);
			Vector3f normal = normals.get(j);
			gl.glNormal3d(normal.getX(), normal.getY(), normal.getZ());
			gl.glBegin(GL.GL_POLYGON);
			for(int i = 0; i < face.length; i++)
			{
				Vector3f pos = vertices.get(face[i]);
				if( this instanceof Box)
				{
					if(i == 0)
						gl.glTexCoord2f(1, 1);
					if(i == 1)
						gl.glTexCoord2f(1, 0);
					if(i == 2)
						gl.glTexCoord2f(0, 0);
					if(i == 3)
						gl.glTexCoord2f(0, 1);
				}
				gl.glVertex3f(pos.getX(), pos.getY(), pos.getZ());
			}
			gl.glEnd();
		}
	}
	
	/**
	 * Calculate the normal of a given face
	 * @param face	Face of which the normal is the be calculated
	 * @return	Vector representing the normal.
	 */

	private Vector3f calculateNormal(int[] face)
	{
		Vector3f p1 = vertices.get(face[0]);
		Vector3f p2 =vertices.get(face[1]);
		Vector3f p3 =vertices.get(face[2]);
		Vector3f v1 = p2.sub(p1); 
		Vector3f v2 = p3.sub(p1);
		Vector3f v3 = v1.out(v2);
		v3 = v3.normalize();
		return v3;
	}
	
	/**
	 * Rotates all vertices around the vertical axis in a given point.
	 * @param angle		Angle with which needs to be rotated
	 * @param xRotate	X coordinate of the point around which is to be rotated
	 * @param zRotate	Z coordinate of the point around which is to be rotated
	 */

	protected void rotateVerticesY(float angle, double xRotate, double zRotate)
	{
		for(int i = 0; i < vertices.size(); i++)
		{
			Vector3f vertex = vertices.get(i);
			float x = vertex.getX();
			float z = vertex.getZ();
			double cos = Math.cos(Math.toRadians(angle));
			double sin = Math.sin(Math.toRadians(angle));
			vertex.setX((float)(x*cos - z * sin - xRotate * cos + zRotate * sin + xRotate));
			vertex.setZ((float)(x*sin + z * cos - zRotate * cos - xRotate * sin + zRotate));
		}
	}

}
