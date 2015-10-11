package renderer.math;

/**
 * This class represents a 3d vector
 *
 * @author Stephen Thompson
 *
 */
public class Vec3 {
	// the x position
	private float x;

	// the y position
	private float y;

	// the z position
	private float z;

	/**
	 * Creates a new vector from another vector
	 * @param v
	 */
	public Vec3(Vec3 v){
		super();
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec3(float x, float y, float z){
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec3(double x, double y, double z) {
		super();
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	/**
	 * @return - the x value of this vector
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return - the y value of this vector
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return - the z value of this vector
	 */
	public float getZ() {
		return z;
	}

	/**
	 * This method sums two vectors
	 * @param v - a different vector
	 * @return - a vector which is the sum of this vector and vector v
	 */
	public Vec3 add(Vec3 v){
		return new Vec3(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * This method subtracts two vectors
	 * @param v - a different vector
	 * @return - a vector which is the difference of this vector and vector v
	 */
	public Vec3 sub(Vec3 v){
		return new Vec3(x - v.x, y - v.y, z - v.z);
	}

	/**
	 * This method multiplies two vectors
	 * @param v - a different vector
	 * @return - a vector which is the product of this vector and vector v
	 */
	public Vec3 mul(Vec3 v){
		return new Vec3(x * v.x, y * v.y, z * v.z);
	}

	/**
	 * This method divides two vectors
	 * @param v - a different vector
	 * @return - a vector which is this vector divided vector v
	 */
	public Vec3 div(Vec3 v){
		return new Vec3(x / v.x, y / v.y, z / v.z);
	}

	/**
	 * calculates the cross product of this vector and another vector
	 * @param v
	 * @return
	 */
	public Vec3 cross(Vec3 v){
		return new Vec3(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x);
	}

	/**
	 * Normalizes a vector
	 * @return the normalized vector of this vector
	 */
	public Vec3 normalize(){
		float len = mag();
		return new Vec3(x / len, y / len, z / len);
	}

	/**
	 * returns the length of this vector
	 * @return the length of this vector
	 */
	public float mag(){
		return (float) Math.sqrt(x*x + y*y+ z*z);
	}

	/**
	 *
	 * @param v
	 * @return
	 */
	public float dot(Vec3 v){
		return (x * v.x + y * v.y + z * v.z) / (mag() * v.mag()) ;
	}

	/**
	 *
	 * @return
	 */
	public static Vec3 UnitX(){
		return new Vec3(1,0,0);
	}

	/**
	 *
	 * @return
	 */
	public static Vec3 UnitY(){
		return new Vec3(0,1,0);
	}

	/**
	 *
	 * @return
	 */
	public static Vec3 UnitZ(){
		return new Vec3(0,0,1);
	}

	/**
	 *
	 * @return
	 */
	public static Vec3 Zero(){
		return new Vec3(0,0,0);
	}

	/**
	 *
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 *
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 *
	 * @param z
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 *
	 * @return
	 */
	public static Vec3 One() {
		return new Vec3(1,1,1);
	}

	/**
	 *
	 * @param a
	 * @param b
	 * @param val
	 * @return
	 */
	public static Vec3 Lerp(Vec3 a, Vec3 b, float val) {
		if (val <= 0){
			return new Vec3(a);
		} else if (val >= 1){
			return new Vec3(b);
		}
		else {
			float xLerp = a.getX() * (1-val) + b.getX() * val;
			float yLerp = a.getY() * (1-val) + b.getY() * val;
			float zLerp = a.getZ() * (1-val) + b.getZ() * val;
			return new Vec3(xLerp, yLerp, zLerp);
		}
	}
}
