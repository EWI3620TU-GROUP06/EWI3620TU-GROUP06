package MazeObjects;


import javax.media.opengl.*;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

/**
 *	Class that contains the a number of points, represented by vectors, and faces, represented by arrays of numbers
 *	referencing those points, to create three dimensional objects that are in the maze.
 *
 */
public abstract class MazeObject {

	ArrayList<Vector3f> vertices;
	ArrayList<Face> faces;
	ArrayList<Vector2f> texVertices;

	float restitution;

	public MazeObject(boolean foldedTexture)
	{
		vertices = new ArrayList<Vector3f>();
		faces = new ArrayList<Face>();
		texVertices = new ArrayList<Vector2f>();
		if(!foldedTexture)
		{
			texVertices.add(new Vector2f(0.5f, 0.5f));
			texVertices.add(new Vector2f(0.5f, 0.75f));
			texVertices.add(new Vector2f(0.25f, 0.75f));
			texVertices.add(new Vector2f(0.25f, 0.5f));
		}
	}

	public MazeObject(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces)
	{
		this.vertices = vertices;
		this.texVertices = texVertices;
		this.faces = faces;
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

	public void addFace(int[] faceArray)
	{
		if(faceArray.length < 3)
		{
			System.err.println("Faces should contain at leats three vertices");
			return;
		}
		for(int i = 0; i < faceArray.length; i++)
		{
			if(faceArray[i] > vertices.size())
			{
				System.err.println("Couldn't add face: index out of bounds");
				return;
			}
		}
		Face face = new Face(faceArray);
		calculateNormal(face);
		this.faces.add(face);
	}

	public void addFace(int[] faceArray, int[] texArray)
	{
		if(faceArray.length != texArray.length)
		{
			System.out.println("Faces should have equal numbers of texture and 3d coordinates.");
			return;
		}
		if(faceArray.length < 3)
		{
			System.err.println("Faces should contain at leats three vertices");
			return;
		}
		for(int i = 0; i < faceArray.length; i++)
		{
			if(faceArray[i] > vertices.size())
			{
				System.err.println("Couldn't add face: index out of bounds");
				return;
			}
		}
		for(int i = 0; i < texArray.length; i++)
		{
			if(texArray[i] > texVertices.size())
			{
				System.err.println("Couldn't add texture: index out of bounds");
				return;
			}
		}
		Face face = new Face(faceArray, texArray);
		calculateNormal(face);
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
			Face face = faces.get(j);
			Texture texture = getTexture();
			if (texture != null)
			{
				texture.enable();
				texture.bind();
			}
			gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);
			Vector3f normal = face.getNormal();

			gl.glNormal3d(normal.x, normal.y, normal.z);
			gl.glBegin(GL.GL_POLYGON);

			for(int i = 0; i < face.getLength(); i++)
			{
				Vector3f position = vertices.get(face.getVertex(i));
				if(texture != null)
				{
					Vector2f texVertex = texVertices.get(face.getTexVertex(i));
					gl.glTexCoord2f(texVertex.x, texVertex.y);
				}
				gl.glVertex3f(position.x, position.y, position.z);
			}
			gl.glEnd();
			if(texture != null)
				texture.disable();
		}
	}

	/**
	 * Calculate the normal of a given face
	 * @param face	Face of which the normal is the be calculated
	 * @return	Vector representing the normal.
	 */

	private void calculateNormal(Face face)
	{
		Vector3f p1 = vertices.get(face.getVertex(0));
		Vector3f p2 = vertices.get(face.getVertex(1));
		Vector3f p3 = vertices.get(face.getVertex(2));
		Vector3f v1 = new Vector3f(); 
		Vector3f v2 = new Vector3f(); 
		Vector3f v3 = new Vector3f(); 
		v1.sub(p2, p1);
		v2.sub(p3, p1);
		v3.cross(v1, v2);
		v3.normalize();
		face.setNormal(v3);
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
		if(this instanceof CustomMazeObject)
			return -1;
		return 0;
	}

	public Vector3f getVertex(int index)
	{
		return vertices.get(index);
	}

	public int[] getFace(int index)
	{
		return faces.get(index).getVertices();
	}

	public float getRestitution()
	{
		return restitution;
	}

	public boolean isNormalHorizontal(int index)
	{
		Vector3f normal = faces.get(index).getNormal();
		float[] norm = new float[3];
		normal.get(norm);
		return norm[1] == 0;
	}

	public void removeRedundantVertices()
	{
		ArrayList<Vector3f> toBeRemoved = new ArrayList<Vector3f>();
		for(int v = 0; v < vertices.size(); v++)
		{
			boolean nextVertex = false;
			for(int f = 0; f < faces.size(); f++)
			{
				Face face = faces.get(f);
				for (int i = 0; i < face.getLength(); i++)
				{
					if(v == face.getVertex(i))
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

	public abstract Texture getTexture();

}
