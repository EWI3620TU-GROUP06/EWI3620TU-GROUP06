package MazeObjects;

import javax.media.opengl.GL;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import Drawing.DrawingUtil;
import Drawing.VisibleObject;

import com.sun.opengl.util.texture.Texture;

public class SkyBox extends MazeObject implements VisibleObject{
	
	private static Texture texture;

	public SkyBox(float width, float height, float x, float y, float z)
	{
		super(true);
		addVertex(new Vector3f(x, y, z));
		texVertices.add(new Vector2f(0.5f, 2f/3f));
		addVertex(new Vector3f(x+width, y, z));
		texVertices.add(new Vector2f(0.5f, 1f/3f));
		addVertex(new Vector3f(x+width, y + height, z));
		texVertices.add(new Vector2f(0.25f, 1f/3f));
		addVertex(new Vector3f(x, y + height, z));
		texVertices.add(new Vector2f(0.25f, 2f/3f));
		addVertex(new Vector3f(x, y, z+width));
		texVertices.add(new Vector2f(0.75f, 2f/3f));
		addVertex(new Vector3f(x+width, y, z+width));
		texVertices.add(new Vector2f(0.75f, 1f/3f));
		addVertex(new Vector3f(x+width, y + height, z+width));
		texVertices.add(new Vector2f(1f, 1f/3f));
		addVertex(new Vector3f(x, y + height, z+width));
		texVertices.add(new Vector2f(1f, 2f/3f));
		
		texVertices.add(new Vector2f(0.5f, 0f));
		texVertices.add(new Vector2f(0.25f, 0f));
		texVertices.add(new Vector2f(0f, 1f/3f));
		texVertices.add(new Vector2f(0f, 2f/3f));
		texVertices.add(new Vector2f(0.25f, 1f));
		texVertices.add(new Vector2f(0.5f, 1f));
		
		int[] face0 = {0, 4, 5, 1};
		int[] tex0 = {0, 1, 2, 3};
		addFace(face0, tex0);
		
		int[] face1 = {1, 5, 6, 2};
		int[] tex1 = {1, 2, 9, 8};
		addFace(face1, tex1);
		
		int[] face2 = {0, 1, 2, 3};
		int[] tex2 = {0, 1, 5, 4};
		addFace(face2, tex2);
		
		int[] face3 = {4, 0, 3, 7};
		int[] tex3 = {13, 0, 3, 12};
		addFace(face3, tex3);
		
		int[] face4 = {5, 4, 7, 6};
		int[] tex4 = {3, 2, 10, 11};
		addFace(face4, tex4);
		
		int[] face5 = {3, 2, 6, 7};
		int[] tex5 = {4, 5, 6, 7};
		addFace(face5, tex5);
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
}
