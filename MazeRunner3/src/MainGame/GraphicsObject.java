package MainGame;


import javax.media.opengl.*;

import java.util.ArrayList;

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
	
	public void addVertex(Vector3f point)
	{
		vertices.add(point);
	}
	
	public int getNumVertices()
	{
		return vertices.size();
	}
	
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
				gl.glVertex3f(pos.getX(), pos.getY(), pos.getZ());
			}
			gl.glEnd();
		}
	}
	
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
	
	protected void rotateVerticesY(float angle)
	{
		for(int i = 0; i < vertices.size(); i++)
		{
			Vector3f vertex = vertices.get(i);
			float x = vertex.getX();
			float z = vertex.getZ();
			double cos = Math.cos(Math.toRadians(angle));
			double sin = Math.sin(Math.toRadians(angle));
			vertex.setX((float)(x*cos - z * sin - 2.5 * cos + 2.5 * sin + 2.5));
			vertex.setZ((float)(x*sin + z * cos - 2.5 * cos - 2.5 * sin + 2.5));
		}
	}

}
