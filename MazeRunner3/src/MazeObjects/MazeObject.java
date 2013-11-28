package MazeObjects;


import javax.media.opengl.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

/**
 *	Class that contains the a number of points, represented by vectors, and faces, represented by arrays of numbers
 *	referencing those points, to create three dimensional objects that are in the maze.
 *
 */
public abstract class MazeObject {

	ArrayList<Vector3f> vertices;
	ArrayList<int[]> faces;
	ArrayList<Vector3f> normals;
	
	float restitution;

	public MazeObject()
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
	
	public int getNumFaces()
	{
		return faces.size();
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
			
			float[] norm = new float[3];
			
			normal.get(norm);
			
			gl.glNormal3d(norm[0], norm[1], norm[2]);
			gl.glBegin(GL.GL_POLYGON);
			for(int i = 0; i < face.length; i++)
			{
				Vector3f position = vertices.get(face[i]);
				float[] pos = new float[3];
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
				position.get(pos);
				gl.glVertex3f(pos[0], pos[1], pos[2]);
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
		Vector3f p2 = vertices.get(face[1]);
		Vector3f p3 = vertices.get(face[2]);
		Vector3f v1 = new Vector3f(); 
		Vector3f v2 = new Vector3f(); 
		Vector3f v3 = new Vector3f(); 
		v1.sub(p2, p1);
		v2.sub(p3, p1);
		v3.cross(v1, v2);
		v3.normalize();
		return v3;
	}
	
	/**
	 * Rotates all vertices around the vertical axis in a given point.
	 * @param angle		Angle with which needs to be rotated
	 * @param xRotate	X coordinate of the point around which is to be rotated
	 * @param zRotate	Z coordinate of the point around which is to be rotated
	 */

	public void rotateVerticesY(float angle, double xRotate, double zRotate)
	{
		for(int i = 0; i < vertices.size(); i++)
		{
			Vector3f vertex = vertices.get(i);
			float[] vert = new float[3];
			
			vertex.get(vert);
			float x = vert[0];
			float z = vert[2];
			double cos = Math.cos(Math.toRadians(angle));
			double sin = Math.sin(Math.toRadians(angle));
			vert[0] =((float)(x*cos - z * sin - xRotate * cos + zRotate * sin + xRotate));
			vert[2] = ((float)(x*sin + z * cos - zRotate * cos - xRotate * sin + zRotate));
			
			vertex.set(vert);
		}
	}
	
	public byte getCode()
	{
		if(this instanceof Box)
		{
			Box box = (Box) this;
			if(box.height == 5)
				return 1;
			else
				return 2;
		}
		if(this instanceof Ramp)
		{
			Ramp ramp = (Ramp) this;
			if(ramp.height == 5)
				return (byte) (ramp.orientation / 90 + 4);
			else
				return (byte) (ramp.orientation / 90 + 8);
		}
		return 0;
	}
	
	public Vector3f getVertex(int index)
	{
		return vertices.get(index);
	}
	
	public int[] getFace(int index)
	{
		return faces.get(index);
	}
	
	public float getRestitution()
	{
		return restitution;
	}
	
/*	public void removeDoubleFaces(MazeObject that)
	{
		ArrayList<Vector3f> commons 
	}*/
	
	public void removeRedundantVertices()
	{
		ArrayList<Vector3f> toBeRemoved = new ArrayList<Vector3f>();
		for(int v = 0; v < vertices.size(); v++)
		{
			boolean nextVertex = false;
			for(int f = 0; f < faces.size(); f++)
			{
				int[] face = faces.get(f);
				for (int i = 0; i < face.length; i++)
				{
					if(v == face[i])
					{
						nextVertex = true;
						break;
					}
				}
				if(nextVertex)
					break;

				else if(f == faces.size() - 1)
					toBeRemoved.add(vertices.get(v));
			}
		}
		
		for(Vector3f vertex : toBeRemoved)
			vertices.remove(vertex);
	}

}
