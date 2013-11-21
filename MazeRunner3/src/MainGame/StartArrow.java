package MainGame;

public class StartArrow extends GraphicsObject {

	private int orientation;

	public StartArrow(float width, int angle)
	{
		super();
		addVertex(new Vector3f(width/2, (float)0.01, width/2));
		addVertex(new Vector3f(width / 3, (float)0.01, width / 3));
		addVertex(new Vector3f(width / 2, (float)0.01, width));
		addVertex(new Vector3f(width * 2 / 3, (float)0.01, width / 3));

		int face0[] = {0,1,2,3};
		addFace(face0);

		rotateVerticesY(angle);
		orientation = angle;
	}

	public void setAngle(int angle)
	{
		rotateVerticesY(angle - orientation);
		orientation = angle;
	}

}
