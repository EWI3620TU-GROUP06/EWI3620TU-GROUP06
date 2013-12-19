package MazeObjects;

import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class FinishTile extends MazeObject {
	private static Texture texture;
	
	public FinishTile(float width, float x, float z){
		super(false);
		addVertex(new Vector3f(x, 0, z));
		addVertex(new Vector3f(x, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z + width));
		addVertex(new Vector3f(x + width, 0, z));
	}
	
	public static void addTexture(Texture t)
	{
		texture = t;
	}

	@Override
	public Texture getTexture() {
		
		return texture;
	}

}
