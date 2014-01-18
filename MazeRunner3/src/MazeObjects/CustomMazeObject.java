package MazeObjects;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import Drawing.DrawingUtil;

import com.sun.opengl.util.texture.Texture;

/**
 * the class customMazeObject is used to place custom objects in the maze these objects are imported from
 * a .obj file
 */

public class CustomMazeObject extends MazeObject{

	private File file;
	private static ArrayList<Texture> textures = new ArrayList<Texture>();
	private boolean hasTexture = false;
	private int texNum;

	public CustomMazeObject()
	{
		super(true);
	}

	public CustomMazeObject(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces, int texNum, File file, boolean hasTexture)
	{
		super(vertices, texVertices, faces);
		this.texNum = texNum;
		this.file = file;
		this.hasTexture = hasTexture;
	}
	
	/**
	 * the read method gives the oppertunity to load custom objects from a file in this file are vertices for the
	 * points in the of the object, the faces of the object and vertices used for the textures. the texture is also 
	 * placed on the object in this method.
	 */
	public static CustomMazeObject readFromOBJ(File file)
	{
		CustomMazeObject res = new CustomMazeObject();

		try{
			Scanner sc = new Scanner(file);

			while(sc.hasNextLine())
			{
				String line = sc.nextLine();
				if(line.startsWith("vt"))
				{
					String coordinates[] = line.split("[ ]");
					float x = Float.parseFloat(coordinates[1]);
					float y = 1 - Float.parseFloat(coordinates[2]);
					res.texVertices.add(new Vector2f(x, y));
					res.hasTexture = true;
				}
				else if(line.startsWith("v"))
				{
					String coordinates[] = line.split("[ ]");
					float x = Float.parseFloat(coordinates[1]);
					float y = Float.parseFloat(coordinates[2]);
					float z = Float.parseFloat(coordinates[3]);
					res.vertices.add(new Vector3f(x, y, z));
				}
				else if(line.startsWith("f"))
				{
					String points[] = line.split("[ ]");
					int [] faceArray = new int[points.length - 1];
					int[] texArray = new int[points.length - 1];
					for(int i  = 1; i < points.length; i++)
					{
						String[] point = points[i].split("[/]"); 
						faceArray[i - 1] = Integer.parseInt(point[0]) - 1;
						if(point.length > 1)
						{
							if(!point[1].isEmpty()){
								texArray[i-1] = Integer.parseInt(point[1]) - 1;
							}
							else
								res.hasTexture = false;
						}
					}
					if(res.hasTexture)
						res.addFace(faceArray, texArray);					
					else
						res.addFace(faceArray);
				}
			}
			//res.removeRedundantVertices();

			res.file = file;
			
			res.rotateVerticesX(90, 0, 0);
			
			if(!res.hasTexture)
			{
				res.texVertices.add(new Vector2f(1, 0));
				res.texVertices.add(new Vector2f(1, 1));
				res.texVertices.add(new Vector2f(0, 1));
				res.texVertices.add(new Vector2f(0, 0));
			}
			
			res.calculateYMin();
			res.calculateHeight();
			
			sc.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}

		return res;
	}

	public static void addTexture(Texture t)
	{
		textures.add(t);
	}
	
	public static void clearTextures()
	{
		textures.clear();
	}

	public Texture getTexture()
	{
		return textures.get(texNum);
	}
	
	/**
	 * used to set a texture on the custom object
	 */
	public void setTexture(GL gl)
	{
		if(hasTexture)
		{
			String fileName = file.getName();
			String[] name = fileName.split("[.]");
			texNum = textures.size();
			addTexture(DrawingUtil.initTexture(gl, name[0]));
		}
		else{
			addTexture(DrawingUtil.initTexture(gl, "wall"));
		}
	}
	
	public void setTexNum(int n)
	{
		texNum = n;
	}
	
	public int getTexNum()
	{
		return texNum;
	}

	public File getFile()
	{
		return file;
	}

	/**
	 * translate and clone same as in box
	 */
	public MazeObject translate(float x, float y, float z)
	{
		CustomMazeObject res = (CustomMazeObject)this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x, y, z));
		res.calculateYMin();
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
		return new CustomMazeObject(vertices, this.texVertices, faces, this.texNum, file, hasTexture);
	}
	
	public boolean equals(Object other)
	{
		if(other instanceof CustomMazeObject)
		{
			CustomMazeObject that = (CustomMazeObject) other;
			return this.file.getAbsolutePath().equals(that.file.getAbsolutePath());
		}
		return false;
	}


}
