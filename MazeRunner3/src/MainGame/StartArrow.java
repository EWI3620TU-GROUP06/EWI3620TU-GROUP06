package MainGame;

public class StartArrow extends GraphicsObject {

	private float orientation;

	public StartArrow(float width, float angle)
	{
		super();
		addVertex(new Vector3f(width/2, (float)0.01, width/2));
		addVertex(new Vector3f(width / 3, (float)0.01, width / 3));
		addVertex(new Vector3f(width / 2, (float)0.01, width));
		addVertex(new Vector3f(width * 2 / 3, (float)0.01, width / 3));

		int face0[] = {0,1,2,3};
		addFace(face0);

		rotateVerticesY(angle, 2.5, 2.5);
		orientation = angle;
	}

	public void setAngle(float angle)
	{
		rotateVerticesY(angle - orientation, 2.5, 2.5);
		orientation = angle;
	}

}
