package MazeObjects;

import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class Floor extends MazeObject {
	
	private static Texture texture;
	
	public Floor(float width, float x, float z){
		super(false);
		this.width = width;
		height = 0;
		addVertex(new Vector3f(x, 0, z));
		addVertex(new Vector3f(x, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z));

		int face0[] = {0,1,2,3};
		addFace(face0);
		
		restitution = 0.8f;
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
