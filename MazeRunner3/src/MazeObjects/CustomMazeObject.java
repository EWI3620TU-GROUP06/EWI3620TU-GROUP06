package MazeObjects;

import java.io.File;
import java.util.Scanner;

import javax.vecmath.Vector3f;

import com.sun.opengl.util.texture.Texture;

public class CustomMazeObject extends MazeObject{
	
	private String fileName;
	
	public CustomMazeObject()
	{
		super();	
	}
	
	public static MazeObject readFromOBJ(File file)
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
					float x = Float.parseFloat(coordinates[1]) * 100;
					float y = Float.parseFloat(coordinates[2]) * 100;
					float z = Float.parseFloat(coordinates[3]) * 100;
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
			
			res.fileName = file.getName();
			
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
	
	public String getFileName()
	{
		return fileName;
	}

}
