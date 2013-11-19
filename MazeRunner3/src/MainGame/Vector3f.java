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
	
	public Vector3f add(Vector3f that)
	{
		float x = this.x + that.getX();
		float y = this.y + that.getY();
		float z = this.z + that.getZ();
		return new Vector3f(x, y, z);
	}
	
	public Vector3f sub(Vector3f that)
	{
		float x = this.x - that.getX();
		float y = this.y - that.getY();
		float z = this.z - that.getZ();
		return new Vector3f(x, y, z);
	}
	
	public Vector3f mul(float factor)
	{
		float x = this.x * factor;
		float y = this.y * factor;
		float z = this.z * factor;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f out(Vector3f that)
	{
		float x = this.z * that.y - that.z * this.y;
		float y = this.x * that.z - that.x * this.z;
		float z = this.y * that.x - that.y * this.x;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f normalize()
	{
		float length = (float)Math.sqrt(x*x + y*y + z*z);
		float x = this.x / length;
		float y = this.y / length;
		float z = this.z / length;
		return  new Vector3f(x, y, z);
	}

	
}
