package MazeObjects;


import javax.media.opengl.*;

import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

/**
 *	Class that contains the a number of points, represented by vectors, and faces,
 *	to create three dimensional objects that are in the maze.
 *
 */
public abstract class MazeObject {

	protected ArrayList<Vector3f> vertices;
	protected ArrayList<Face> faces;
	protected ArrayList<Vector2f> texVertices;
	public float width;
	public float height;
	public int[] rotation = {0, 0, 0};

	float restitution;

	public MazeObject(boolean foldedTexture)
	{
		vertices = new ArrayList<Vector3f>();
		faces = new ArrayList<Face>();
		texVertices = new ArrayList<Vector2f>();
		if(!foldedTexture)
		{
			texVertices.add(new Vector2f(1, 0));
			texVertices.add(new Vector2f(1, 1));
			texVertices.add(new Vector2f(0, 1));
			texVertices.add(new Vector2f(0, 0));
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

	public int getNumVertices()
	{
		return vertices.size();
	}
	
	public int[] getRotation()
	{
		return rotation;
	}

	public int getNumFaces()
	{
		return faces.size();
	}

	/**
	 * Add a face to the list of faces. The face must contain at least three numbers of the vertices it connects, 
	 * and those vertices must already be in the list of vertices.
	 * @param faceArray	array of the numbers of the vertices the face connects
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

	/**
	 * Add a face to the list of faces. The face must contain at least three numbers of the vertices it connects, 
	 * and those vertices must already be in the list of vertices. Also, it must contain a equal number of texture 
	 * vertex numbers, and those vertices must also already be in the texture vertices array.
	 * 
	 * @param faceArray	array of the numbers of the vertices the face connects
	 * @param texArray	array of the numbers of texture vertices the face connects
	 */
	
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
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_SPECULAR, wallColour, 0);
		gl.glMateriali( GL.GL_FRONT, GL.GL_SHININESS, 50);

		for(int j = 0; j < faces.size(); j++)
		{
			Face face = faces.get(j);
			Texture texture = getTexture();
			if (texture != null)
			{
				texture.enable();
				texture.bind();
			}
			Vector3f normal = face.getNormal();

			gl.glNormal3d(normal.x, normal.y, normal.z);
			gl.glBegin(GL.GL_POLYGON);

			for(int i = 0; i < face.getLength(); i++)
			{
				if(texture != null)
				{
					Vector2f texVertex = texVertices.get(face.getTexVertex(i));
					gl.glTexCoord2f(texVertex.x, texVertex.y);
				}
				Vector3f position = vertices.get(face.getVertex(i));
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
	 * Rotates all vertices around the X axis in a given point.
	 * @param angle		Angle with which needs to be rotated
	 * @param yRotate	Y coordinate of the point around which is to be rotated
	 * @param zRotate	Z coordinate of the point around which is to be rotated
	 */

	public void rotateVerticesX(float angle, double yRotate, double zRotate)
	{
		for(int i = 0; i < vertices.size(); i++)
		{
			Vector3f vertex = vertices.get(i);
			float y = vertex.y;
			float z = vertex.z;
			double cos = Math.cos(Math.toRadians(angle));
			double sin = Math.sin(Math.toRadians(angle));
			vertex.z=((float)(z*cos - y * sin - zRotate * cos + yRotate * sin + zRotate));
			vertex.y = ((float)(z*sin + y * cos - yRotate * cos - zRotate * sin + yRotate));
		}
		
		for(Face face: faces){
			calculateNormal(face);
		}
		rotation[0] += angle;
	}
	
	/**
	 * Rotates all vertices around the Y axis in a given point.
	 * @param angle		Angle with which needs to be rotated
	 * @param xRotate	X coordinate of the point around which is to be rotated
	 * @param zRotate	Z coordinate of the point around which is to be rotated
	 */
	
	public void rotateVerticesY(float angle, double xRotate, double zRotate)
	{
		for(int i = 0; i < vertices.size(); i++)
		{
			Vector3f vertex = vertices.get(i);
			float x = vertex.x;
			float z = vertex.z;
			double cos = Math.cos(Math.toRadians(angle));
			double sin = Math.sin(Math.toRadians(angle));
			vertex.x = ((float)(x*cos - z * sin - xRotate * cos + zRotate * sin + xRotate));
			vertex.z = ((float)(x*sin + z * cos - zRotate * cos - xRotate * sin + zRotate));
		}
		
		for(Face face: faces){
			calculateNormal(face);
		}
		rotation[1] += angle;
	}
	
	/**
	 * Rotates all vertices around the Z axis in a given point.
	 * @param angle		Angle with which needs to be rotated
	 * @param xRotate	X coordinate of the point around which is to be rotated
	 * @param yRotate	Y coordinate of the point around which is to be rotated
	 */
	
	public void rotateVerticesZ(float angle, double xRotate, double yRotate)
	{
		for(int i = 0; i < vertices.size(); i++)
		{
			Vector3f vertex = vertices.get(i);
			float y = vertex.y;
			float x = vertex.x;
			double cos = Math.cos(Math.toRadians(angle));
			double sin = Math.sin(Math.toRadians(angle));
			vertex.y =((float)(y*cos - x * sin - yRotate * cos + xRotate * sin + yRotate));
			vertex.x = ((float)(y*sin + x * cos - xRotate * cos - yRotate * sin + xRotate));
		}
		
		for(Face face: faces){
			calculateNormal(face);
		}
		rotation[2] += angle;
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
	
	/**
	 * Checks if a certain face has a horizontal normal vector,
	 * @param index	Number of the face to be checked
	 * @return		boolean that represents whether the face has a horizontal normal. 
	 */

	public boolean isNormalHorizontal(int index)
	{
		Vector3f normal = faces.get(index).getNormal();
		float[] norm = new float[3];
		normal.get(norm);
		return norm[1] == 0;
	}
	
	/**
	 * Removes all vertices that are not connected to other vertices in a face. 
	 */

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
	public abstract MazeObject translate(float x, float y, float z);
	public abstract MazeObject clone();
	public abstract boolean equals(Object other);

	/**
	 * Gets all vertices that are connected by a certain face.
	 * @param index	number of the face to be checked
	 * @return		arraylist of vectors that that face connects.
	 */

	public ArrayList<Vector3f> getFaceVertices(int index)
	{
		ArrayList<Vector3f> res = new ArrayList<Vector3f>();
		Face face = faces.get(index);
		for(int i = 0; i < face.getLength(); i++)
		{
			res.add(vertices.get(face.getVertex(i)));
		}
		return res;
	}
	
	/**
	 * Removes all faces that this MazeObject and another Maze object share: 
	 * all faces that have the same set of Vertices are removed.
	 * @param that
	 */

	public void removeRedunantFaces(MazeObject that)
	{
		for (int i  = 0; i < this.faces.size(); i++)
		{
			ArrayList<Vector3f> thisFacePoints = this.getFaceVertices(i);
			for(int j  = 0; j < that.faces.size(); j++)
			{
				ArrayList<Vector3f> thatFacePoints = that.getFaceVertices(j);
				@SuppressWarnings("unchecked")
				ArrayList<Vector3f> commonVertices = (ArrayList<Vector3f>) thisFacePoints.clone();
				commonVertices.retainAll(thatFacePoints);
				if(commonVertices.size() == thatFacePoints.size() && commonVertices.size() == thisFacePoints.size())
				{
					this.faces.remove(i);
					that.faces.remove(j);
					break;
				}
			}
		}
	}

	public Vector3f getPos()
	{
		Vector3f corner = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		for(Vector3f vertex : vertices)
		{
			if(vertex.length() < corner.length())
				corner = vertex;
		}
		return corner;
	}
}
