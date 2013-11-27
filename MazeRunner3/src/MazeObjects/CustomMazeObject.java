package MazeObjects;

import java.io.File;
import java.util.Scanner;

import javax.vecmath.Vector3f;

public class CustomMazeObject extends MazeObject{
	
	public CustomMazeObject()
	{
		super();
	}
	
	public static MazeObject readFromOBJ(String fileName)
	{
		MazeObject res = new CustomMazeObject();
		
		try{
			Scanner sc = new Scanner(new File(fileName));
			
			while(sc.hasNextLine())
			{
				String line = sc.nextLine();
				if(line.startsWith("v"))
				{
					String coordinates[] = line.split("[ ]");
					float x = Float.parseFloat(coordinates[1]) * 2000;
					float y = Float.parseFloat(coordinates[2]) * 2000;
					float z = Float.parseFloat(coordinates[3]) * 2000;
					res.vertices.add(new Vector3f(x, y, z));
					
				}
				if(line.startsWith("f"))
				{
					String points[] = line.split("[ ]");
					int [] face = new int[points.length - 1];
					for(int i  = 1; i < points.length; i++)
					{
						String[] point = points[i].split("[//]"); 
						face[i - 1] = Integer.parseInt(point[0]);
					}
					res.addFace(face);
				}
			}
			System.out.println("Read in " + res.vertices.size() + " vertices.");
			System.out.println("Read in " + res.faces.size() + " faces.");
			sc.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return res;
	}

}
