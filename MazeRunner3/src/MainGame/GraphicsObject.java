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
		int p1 = face[0];
		int p2 = face[1];
		int p3 = face[2];
		normals.add(calculateNormal(vertices.get(p1),vertices.get(p2),vertices.get(p3)));
		this.faces.add(face);
		
	}
	
	public void draw(GL gl, float[] wallColour)
	{
		for(int j = 0; j < faces.size(); j++)
		{
			int[] face = faces.get(j);
			//if(j == faces.size() - 1){
			//wallColour[0] = 1.0f;
			//wallColour[2] = 0.0f;
			//}
			gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);
			//System.out.println("grootte van face " + j + ": " + face.length);
			Vector3f normal = normals.get(j);
			gl.glNormal3d(normal.getX(), normal.getY(), normal.getZ());
			//System.out.println(normal.getX() + ", " + normal.getY()  + ", " + normal.getZ());
			gl.glBegin(GL.GL_POLYGON);
			for(int i = 0; i < face.length; i++)
			{
				//System.out.println("Vertex " + face[i]);
				Vector3f pos = vertices.get(face[i]);
				gl.glVertex3f(pos.getX(), pos.getY(), pos.getZ());
				//System.out.println(pos.getX() + ", " + pos.getY()  + ", " + pos.getZ());
			}
			gl.glEnd();
		}
	}
	
	private Vector3f calculateNormal(Vector3f p1, Vector3f p2, Vector3f p3)
	{
		Vector3f v1 = p2.sub(p1); 
		Vector3f v2 = p3.sub(p1);
		Vector3f v3 = v2.out(v1);
		v3 = v3.normalize();
		return v3;
	}

}
