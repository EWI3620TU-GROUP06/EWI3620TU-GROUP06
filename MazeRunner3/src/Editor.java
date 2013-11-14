
public class Editor extends GameObject{
	
	private float FOV;
	
	private Control control; 

	private double horAngle, verAngle;
	
	private int selectedX, selectedZ;
	
	private double squareSize;
	
	private float buttonSize;
	
	public Editor(double x, double y, double z, double h, double v)
	{
		super(x, y, z);
		horAngle = h;
		verAngle = v;
	}
	
	public void setFOV(float FOV)
	{
		this.FOV = FOV;
	}
	
	public void setButtonSize(float buttonSize)
	{
		this.buttonSize = buttonSize;
	}
	
	public int getSelectedX() {
		return selectedX;
	}

	public int getSelectedZ() {
		return selectedZ;
	}
	
	public void setSquareSize(double squareSize) {
		this.squareSize = squareSize;
	}
	
	/**
	 * Sets the Control object that will control the player's motion
	 * <p>
	 * The control must be set if the object should be moved.
	 * @param input
	 */
	public void setControl(Control control)
	{
		this.control = control;
	}
	
	/**
	 * Returns the horizontal angle of the orientation.
	 * @return the horAngle
	 */
	public double getHorAngle() {
		return horAngle;
	}

	/**
	 * Sets the horizontal angle of the orientation.
	 * @param horAngle the horAngle to set
	 */
	public void setHorAngle(double horAngle) {
		this.horAngle = horAngle;
	}

	/**
	 * Returns the vertical angle of the orientation.
	 * @return the verAngle
	 */
	public double getVerAngle() {
		return verAngle;
	}

	/**
	 * Sets the vertical angle of the orientation.
	 * @param verAngle the verAngle to set
	 */
	public void setVerAngle(double verAngle) {
		this.verAngle = verAngle;
	}
	
	public void update(int screenWidth, int screenHeight )
	{
		if(control != null)
		{
			control.update();
			int notches = control.getNotches();
			if(locationY + notches > 5){
				locationY += notches;
				double halfTan = Math.tan(Math.toRadians(FOV/2));
				double factor = (screenHeight/2) / (halfTan * locationY);
				selectedX = (int) Math.floor(((control.getMouseX() - screenWidth/2)/factor + locationX)/squareSize);
				selectedZ = (int) Math.floor(((control.getMouseY() - screenHeight/2)/factor + locationZ)/squareSize);
			}
			int button = control.getButtons(3, buttonSize);
			if(button == 0)
			{
				System.out.println("Button 0 pressed!");
			}
			if(button == 1)
			{
				System.out.println("Button 1 pressed!");
			}
			if(button == 2)
			{
				System.out.println("Button 2 pressed!");
			}
		}

	}

}
