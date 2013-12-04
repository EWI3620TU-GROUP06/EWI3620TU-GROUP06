package MazeObjects;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class CustomMazeObject extends MazeObject{
	
	private File file;
	
	public CustomMazeObject()
	{
		super();	
	}
	
	public CustomMazeObject(ArrayList<Vector3f> vertices, ArrayList<Vector3f> normals, ArrayList<int[]> faces)
	{
		super(vertices, normals, faces);
	}
	
	public static CustomMazeObject readFromOBJ(File file)
	{
		CustomMazeObject res = new CustomMazeObject();
		
		try{
			Scanner sc = new Scanner(file);
			
			while(sc.hasNextLine())
			{
				String line = sc.nextLine();
				if(line.startsWith("v"))
				{
					String coordinates[] = line.split("[ ]");
					float x = Float.parseFloat(coordinates[1]) * 0.001f;
					float y = Float.parseFloat(coordinates[2]) * 0.001f;
					float z = Float.parseFloat(coordinates[3]) * 0.001f;
					res.vertices.add(new Vector3f(x, y, z));
					
				}
				if(line.startsWith("f"))
				{
					String points[] = line.split("[ ]");
					int [] face = new int[points.length - 1];
					for(int i  = 1; i < points.length; i++)
					{
						String[] point = points[i].split("[//]"); 
						face[i - 1] = Integer.parseInt(point[0]) - 1;
					}
					res.addFace(face);
				}
			}
			res.removeRedundantVertices();
			System.out.println("Read in " + res.vertices.size() + " vertices.");
			System.out.println("Read in " + res.faces.size() + " faces.");
			
			res.file = file;
			
			sc.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static void addTexture(Texture t)
	{
		//Custom object cannot yet contain textures.
	}
	
	public Texture getTexture()
	{
		return null;
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
		return new CustomMazeObject(vertices, this.normals, this.faces);
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
