package MainGame;

public class Vector3f {
	
	private float x;
	private float y;
	private float z;
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	
	public void add(Vector3f that)
	{
		this.x += that.getX();
		this.y += that.getY();
		this.z += that.getZ();
	}
	
	public void mul(float factor)
	{
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
	}
	
	public Vector3f out(Vector3f that)
	{
		float x = this.y * that.z - that.y * this.z;
		float y = this.z * that.x - that.z * this.x;
		float z = this.x * that.y - that.x * this.y;
		return new Vector3f(x, y, z);
	}

	
}
