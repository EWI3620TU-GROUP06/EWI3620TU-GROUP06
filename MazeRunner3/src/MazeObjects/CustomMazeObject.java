package MazeObjects;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import Drawing.DrawingUtil;

import com.sun.opengl.util.texture.Texture;

public class CustomMazeObject extends MazeObject{

	private File file;
	private Texture texture;
	private boolean hasTexture = false;

	public CustomMazeObject()
	{
		super(true);	
	}

	public CustomMazeObject(ArrayList<Vector3f> vertices, ArrayList<Vector2f> texVertices, ArrayList<Face> faces, Texture texture)
	{
		super(vertices, texVertices, faces);
		this.texture = texture;
	}

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
			res.removeRedundantVertices();
			System.out.println("Read in " + res.vertices.size() + " vertices.");
			System.out.println("Read in " + res.faces.size() + " faces.");

			res.file = file;
			
			res.rotateVerticesX(-90, 0, 0);

			sc.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}

		return res;
	}

	public void addTexture(Texture t)
	{
		texture = t;
	}

	public Texture getTexture()
	{
		return texture;
	}

	public void setTexture(GL gl)
	{
		if(hasTexture)
		{
			String fileName = file.getName();
			String[] name = fileName.split("[.]");
			addTexture(DrawingUtil.initTexture(gl, name[0]));
		}
	}

	public File getFile()
	{
		return file;
	}

	public CustomMazeObject translate(float x, float y, float z)
	{

		CustomMazeObject res = this.clone();
		for(Vector3f vertex : res.vertices)
			vertex.add(new Vector3f(x, y, z));
		return res;
	}

	public CustomMazeObject clone()
	{
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		for(Vector3f vertex : this.vertices)
		{
			vertices.add((Vector3f)vertex.clone());
		}
		return new CustomMazeObject(vertices, this.texVertices, this.faces, this.texture);
	}

	public boolean equals(Object other)
	{
		if(other instanceof CustomMazeObject)
		{
			CustomMazeObject that = (CustomMazeObject) other;
			return this.faces == that.faces;
		}
		return false;
	}


}
