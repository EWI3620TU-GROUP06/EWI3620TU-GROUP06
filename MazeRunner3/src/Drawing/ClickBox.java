package Drawing;

import javax.vecmath.Vector2f;

public class ClickBox {
	
	private Vector2f location;
	private int leftBound;
	private int rightBound;
	private int upperBound;
	private int lowerBound;

	public ClickBox(int x, int y, int l, int r, int up, int low){
		this.location = new Vector2f(x,y);
		this.leftBound = l;
		this.rightBound = r;
		this.upperBound = up;
		this.lowerBound = low;
	}
}
