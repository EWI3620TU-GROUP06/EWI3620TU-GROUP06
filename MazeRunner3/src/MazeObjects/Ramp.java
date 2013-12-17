package MazeObjects;

import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class Ramp extends MazeObject{
	
	protected int orientation;
	protected float height;
	private static Texture texture;
	
	public Ramp(float width, float height, int orientation, float x, float y, float z)
	{
		super(false);
		addVertex(new Vector3f(x, y, z));
		addVertex(new Vector3f(x+width, y, z));
		addVertex(new Vector3f(x+width, y + height, z));
		addVertex(new Vector3f(x, y + height, z));
		addVertex(new Vector3f(x, y, z+width));
		addVertex(new Vector3f(x+width, y, z+width));
		
		int[] face0 = {0, 3, 2, 1};
		addFace(face0);
		
		int[] face1 = {1, 2, 5};
		addFace(face1);
		
		int[] face2 = {0, 4, 5, 1};
		addFace(face2);
		
		int[] face3 = {0, 4, 3};
		addFace(face3);
		
		int[] face4 = {2, 3, 4, 5};
		addFace(face4);
		
		this.rotateVerticesY(orientation, 2.5 + x, 2.5 + z);
		
		this.orientation = orientation;
		this.height =  height;
		
		restitution = 0.0f;
	}

	public static void addTexture(Texture t)
	{
		texture = t;
	}
	
	public Texture getTexture()
	{
		return texture;
	}
	
}
