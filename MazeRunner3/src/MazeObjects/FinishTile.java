package MazeObjects;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import LevelHandling.Maze;

import com.sun.opengl.util.texture.Texture;

/**
 * the finishTile is the finish of the game when the player is on this tile the game is finished. 
 */
public class FinishTile extends MazeObject {
	private static Texture texture;
	private static CustomMazeObject custom = CustomMazeObject.readFromOBJ(new File("src\\Objects\\hole_01.obj"));
	
	public FinishTile(float width, float x, float y, float z){
		super(true);
		custom = (CustomMazeObject) custom.translate(x, y, z);
		custom = (CustomMazeObject) custom.translate(0, -custom.height, 0);
		this.vertices = custom.vertices;
		this.texVertices = custom.texVertices;
		this.faces = custom.faces;
		this.yMin = y;
		height = 0;
	}
	
	public FinishTile(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces)
	{
		super(vertices, texVertices, faces);
		width = Maze.SQUARE_SIZE;
		calculateYMin();
		calculateHeight();
		yMin = yMin + height;
		height = Maze.SQUARE_SIZE;
		restitution = 0.8f;
	}
	
	public static void addTexture(Texture t)
	{
		texture = t;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	/**
	 * same as box
	 */
	public MazeObject translate(float x, float y, float z)
	{
		FinishTile res = (FinishTile)this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x, y, z));
		calculateYMin();
		calculateHeight();
		yMin = yMin + height;
		height = Maze.SQUARE_SIZE;
		return res;
	}

	public MazeObject clone()
	{
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		for(Vector3f vertex : this.vertices)
		{
			vertices.add((Vector3f)vertex.clone());
		}
		ArrayList<Face> faces = new ArrayList<Face>();
		for(Face face : this.faces)
		{
			faces.add(face.clone());
		}
		for(Face face: faces){
			calculateNormal(face);
		}
		return new FinishTile(vertices, this.texVertices, faces);
	}
	
	public boolean equals(Object other)
	{
		return other instanceof FinishTile;
	}
	
	//The pit cannot be rotated.
		@Override
		public void rotateVerticesX(float angle, double y, double z)
		{
			// Do Nothing
		}
		
		@Override
		public void rotateVerticesY(float angle, double x, double z)
		{
			// Do Nothing
		}
		
		@Override
		public void rotateVerticesZ(float angle, double x, double y)
		{
			// Do Nothing
		}

}
