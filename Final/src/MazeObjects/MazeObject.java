package MazeObjects;


import javax.media.opengl.*;

import java.util.ArrayList;

import javax.vecmath.Matrix3f;
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
	protected float width;
	protected float height;
	protected float yMin;

	protected Vector3f normalX = new Vector3f(1,0,0);
	protected Vector3f normalY = new Vector3f(0,1,0);
	protected Vector3f normalZ = new Vector3f(0,0,1);
	
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

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getYMin() {
		return yMin;
	}

	public float getYMax(){
		return yMin + height;
	}

	protected void calculateYMin(){
		float yMin = Float.MAX_VALUE;
		for (Vector3f vertex : this.vertices)
		{
			if(vertex.y < yMin)
				yMin = vertex.y;
		}
		this.yMin = yMin;
	}

	protected void calculateHeight(){
		float yMax = Float.MIN_VALUE;
		for (Vector3f vertex : this.vertices)
		{
			if(vertex.y > yMax)
				yMax = vertex.y;
		}
		this.height = yMax - yMin;
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
	
	/**
	 * The rotation of the MazeObject is found by comparing its three normals to the three unity vectors after 
	 * rotating the same amount as the object.
	 * @return	rotation around the x, y and z axis stored in an array.
	 */

	public int[] getRotation()
	{	
		int x = 0, y = 0, z = 0;
		Vector3f unityX = new Vector3f(1,0,0);
		Vector3f unityY = new Vector3f(0,1,0);
		Vector3f unityZ = new Vector3f(0,0,1);
		for(x = 0; x < 4; x++){
			for(y = 0; y < 4; y++){
				for(z=0; z < 4; z++){
					if(isEqual(normalX, unityX) && 
							isEqual(normalY, unityY) &&
							isEqual(normalZ, unityZ)){
						return new int[]{x,y,z};
					}
					rotateVectorZ(unityX);
					rotateVectorZ(unityY);
					rotateVectorZ(unityZ);
				}
				rotateVectorY(unityX);
				rotateVectorY(unityY);
				rotateVectorY(unityZ);
			}
			rotateVectorX(unityX);
			rotateVectorX(unityY);
			rotateVectorX(unityZ);
		}
		return new int[]{0,0,0};
		//return rotation;
	}
	
	/**
	 * Compare is two vectors are very close to the same as each other.
	 * @param one
	 * @param other
	 * @return
	 */

	private boolean isEqual(Vector3f one, Vector3f other)
	{
		Vector3f dif = new Vector3f();
		dif.sub(one, other);
		return dif.length() < 0.0001;
	}
	
	/**
	 * Rotates the given vector 90 degrees around the X axis.
	 * @param toRot
	 */

	private void rotateVectorX(Vector3f toRot)
	{
		Matrix3f rot = new Matrix3f();
		rot.rotX((float) Math.toRadians(90));
		rot.transform(toRot);
	}
	
	/**
	 * Rotates the given vector 90 degrees around the Y axis.
	 * @param toRot
	 */


	private void rotateVectorY(Vector3f toRot)
	{
		Matrix3f rot = new Matrix3f();
		rot.rotY((float) Math.toRadians(90));
		rot.transform(toRot);
	}
	
	/**
	 * Rotates the given vector 90 degrees around the X axis.
	 * @param toRot
	 */


	private void rotateVectorZ(Vector3f toRot)
	{
		Matrix3f rot = new Matrix3f();
		rot.rotZ((float) Math.toRadians(90));
		rot.transform(toRot);
	}

	public int getNumFaces()
	{
		return faces.size();
	}
	
	/**
	 * Enables or disables the top face(s) of the mazeObject, if it has one. A top face is defined as a face that 
	 * only connects vertices that are (almost) as high as the highest point of the MazeObject.
	 * @param enabled	Boolean representing whether the top faces should be enabled or disabled.
	 */

	public void setTop(boolean enabled)
	{
		for(int i = 0; i < faces.size(); i++)
		{
			ArrayList<Vector3f> vertices = getFaceVertices(i);
			boolean isTop = true;
			for(Vector3f vertex : vertices)
			{
				if(Math.abs(vertex.y - (yMin + height))> 0.01)
				{
					isTop = false;
					break;
				}
			}
			if(isTop)
			{
				faces.get(i).setEnabled(enabled);
			}
		}
	}

	public boolean hasBottom(float bottomHeight){
		boolean res = false;
		for(int i = 0; i < faces.size(); i++)
		{
			ArrayList<Vector3f> vertices = getFaceVertices(i);
			int count = 0;
			for(Vector3f vertex : vertices)
			{
				if(Math.abs(vertex.y - bottomHeight)< 0.1)
				{
					count++;
				}
			}
			if(count == 4)
			{
				res = true;
				break;
			}
		}
		return res;
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

		if(this instanceof Pit){
			gl.glDisable(GL.GL_CULL_FACE);
		}

		for(int j = 0; j < faces.size(); j++)
		{
			Face face = faces.get(j);
			if(face.isEnabled())
			{
				Texture texture = getTexture();
				if (texture != null)
				{
					texture.enable();
					texture.bind();
				}
				Vector3f normal = face.getNormal();


				gl.glBegin(GL.GL_POLYGON);

				for(int i = 0; i < face.getLength(); i++)
				{
					if(texture != null)
					{
						Vector2f texVertex = texVertices.get(face.getTexVertex(i));
						gl.glTexCoord2f(texVertex.x, texVertex.y);
					}
					gl.glNormal3d(normal.x, normal.y, normal.z);
					Vector3f position = vertices.get(face.getVertex(i));
					gl.glVertex3f(position.x, position.y, position.z);
				}
				if(this instanceof Pit){
					gl.glEnable(GL.GL_CULL_FACE);
				}
				gl.glEnd();
				if(texture != null)
					texture.disable();
			}
		}
	}

	/**
	 * Calculate the normal of a given face
	 * @param face	Face of which the normal is the be calculated
	 * @return	Vector representing the normal.
	 */

	protected void calculateNormal(Face face)
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

		Matrix3f rot = new Matrix3f();
		rot.rotX((float) Math.toRadians(angle));
		rot.transform(normalX);
		rot.transform(normalY);
		rot.transform(normalZ);

		for(Face face: faces){
			calculateNormal(face);
		}
		calculateYMin();
		calculateHeight();
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

		Matrix3f rot = new Matrix3f();
		rot.rotY((float) Math.toRadians(angle));
		rot.transform(normalX);
		rot.transform(normalY);
		rot.transform(normalZ);

		for(Face face: faces){
			calculateNormal(face);
		}
		calculateYMin();
		calculateHeight();
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

		Matrix3f rot = new Matrix3f();
		rot.rotZ((float) Math.toRadians(angle));
		rot.transform(normalX);
		rot.transform(normalY);
		rot.transform(normalZ);

		for(Face face: faces){
			calculateNormal(face);
		}
		calculateYMin();
		calculateHeight();
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
	@Override
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
				ArrayList<Vector3f> commonVertices = retainAll(thisFacePoints, thatFacePoints);
				if(commonVertices.size() == thatFacePoints.size() && commonVertices.size() == thisFacePoints.size())
				{
					if(!(this.height == 0 || this instanceof StartTile))
						this.faces.remove(i);
					if(!(that.height == 0 || that instanceof StartTile))
						that.faces.remove(j);
					break;
				}
			}
		}
	}

	public ArrayList<Vector3f> retainAll(ArrayList<Vector3f> l1, ArrayList<Vector3f> l2)
	{
		ArrayList<Vector3f> res = new ArrayList<Vector3f>();

		for(Vector3f v1 : l1)
		{
			for(Vector3f v2 : l2)
			{
				if(Math.abs(v1.x - v2.x) < 0.01 &&Math.abs(v1.y - v2.y) < 0.01 && Math.abs(v1.z - v2.z) < 0.01)
				{
					if(!res.contains(v1))
						res.add(v1);
				}
			}
		}

		return res;
	}

	public Vector3f getPos()
	{
		Vector3f corner = new Vector3f(1000, 1000, 1000);
		Vector3f minPoint = new Vector3f(-1000, -1000, -1000);
		for(Vector3f vertex : vertices)
		{
			Vector3f dif1 = new Vector3f();
			Vector3f dif2 = new Vector3f();
			dif1.sub(vertex, minPoint);
			dif2.sub(corner, minPoint);

			if(dif1.length() < dif2.length())
				corner = vertex;
		}
		return corner;
	}

	public void cloneNormals(MazeObject that)
	{
		that.normalX = (Vector3f) this.normalX.clone();
		that.normalY = (Vector3f) this.normalY.clone();
		that.normalZ = (Vector3f) this.normalZ.clone();
	}

}
