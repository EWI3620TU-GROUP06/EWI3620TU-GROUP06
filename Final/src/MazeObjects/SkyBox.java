package MazeObjects;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import Drawing.DrawingUtil;
import Drawing.VisibleObject;

import com.sun.opengl.util.texture.Texture;

/**
 * The SkyBox is the mazeObject on which the surounding sky texture is projected. It is always translated so 
 * that the middle of the Skybox is the player position.
 * 
 *  <p>
 * 
 * Like all MazeObjects, it implements a translate method that returns a copy of the maze object moved to a 
 * different location, and a very weak equals method that acts as a somewhat more specific version 
 * of the instanceof operator.
 * 
 */

public class SkyBox extends MazeObject implements VisibleObject{
	
	private static Texture texture;

	public SkyBox(float width, float height, float x, float y, float z)
	{
		super(true);
		float edgeScale = 0.001f;
		addVertex(new Vector3f(x, y, z));
		texVertices.add(new Vector2f(0.5f-edgeScale, 1f/3f+edgeScale));
		addVertex(new Vector3f(x+width, y, z));
		texVertices.add(new Vector2f(0.5f-edgeScale, 2f/3f-edgeScale));
		addVertex(new Vector3f(x+width, y + height, z));
		texVertices.add(new Vector2f(0.25f+edgeScale, 2f/3f-edgeScale));
		addVertex(new Vector3f(x, y + height, z));
		texVertices.add(new Vector2f(0.25f+edgeScale, 1f/3f+edgeScale));
		addVertex(new Vector3f(x, y, z+width));
		texVertices.add(new Vector2f(0.75f, 1f/3f+edgeScale));
		addVertex(new Vector3f(x+width, y, z+width));
		texVertices.add(new Vector2f(0.75f, 2f/3f-edgeScale));
		addVertex(new Vector3f(x+width, y + height, z+width));
		texVertices.add(new Vector2f(1f, 2f/3f-edgeScale));
		addVertex(new Vector3f(x, y + height, z+width));
		texVertices.add(new Vector2f(1f, 1f/3f+edgeScale));
		
		texVertices.add(new Vector2f(0.5f-edgeScale, 1f));
		texVertices.add(new Vector2f(0.25f+edgeScale, 1f));
		texVertices.add(new Vector2f(0f, 2f/3f-edgeScale));
		texVertices.add(new Vector2f(0f, 1f/3f+edgeScale));
		texVertices.add(new Vector2f(0.25f+edgeScale, 0f));
		texVertices.add(new Vector2f(0.5f-edgeScale, 0f));
		
		int[] face0 = {0, 4, 5, 1};
		int[] tex0 = {1, 2, 9, 8};
		addFace(face0, tex0);
		
		int[] face1 = {1, 5, 6, 2};
		int[] tex1 = {5, 6, 7, 4};
		addFace(face1, tex1);
		
		int[] face2 = {0, 1, 2, 3};
		int[] tex2 = {1, 5, 4, 0};
		addFace(face2, tex2);
		
		int[] face3 = {4, 0, 3, 7};
		int[] tex3 = {2, 1, 0, 3};
		addFace(face3, tex3);
		
		int[] face4 = {5, 4, 7, 6};
		int[] tex4 = {10, 2, 3, 11};
		addFace(face4, tex4);
		
		int[] face5 = {3, 2, 6, 7};
		int[] tex5 = {0, 13, 12, 3};
		addFace(face5, tex5);
		
		//rotateVerticesX(180, y, z);
	}
	
	public SkyBox(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces){
		super(vertices, texVertices, faces);
	}
	
	@Override
	public Texture getTexture() {
		return texture;
	}
	
	public static void addTexture(Texture t)
	{
		texture = t;
	}
	
	public void init(GL gl){
		texture = DrawingUtil.initTexture(gl, "skybox");
	}

	@Override
	public void display(GL gl) {
		this.draw(gl, new float[]{1f, 1f, 1f, 1f});
	}
	
	public void moveTo(Vector3f newLocation)
	{
		Vector3f change = new Vector3f();
		change.sub(newLocation, vertices.get(0));
		for(Vector3f vertex : vertices){
			vertex.add(change);
		}
	}
	
	@Override
	public void draw(GL gl, float[] wallColour)
	{
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_AMBIENT, new float[] {100f, 100f, 100f, 1f}, 0);
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, new float[] {0f, 0f, 0f, 0f}, 0);
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_SPECULAR, new float[] {0f, 0f, 0f, 0f}, 0);
		gl.glMateriali( GL.GL_FRONT, GL.GL_SHININESS, 128);
		
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
	public MazeObject translate(float x, float y, float z)
	{
		SkyBox res = (SkyBox)this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x, y, z));
		return res;
	}

	public MazeObject clone()
	{
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		for(Vector3f vertex : this.vertices)
		{
			vertices.add((Vector3f)vertex.clone());
		}
		return new SkyBox(vertices, this.texVertices, this.faces);
	}
	
	public boolean equals(Object other)
	{
		return other instanceof SkyBox;
	}
}
