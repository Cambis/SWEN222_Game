package renderer.math;

public class Vec3 {
	private float x;
	private float y;
	private float z;

	public Vec3(){
		super();
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vec3(Vec3 v){
		super();
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}


	public Vec3(float x, float y, float z){
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3(double x, double y, double z) {
		super();
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Vec3 add(Vec3 v){
		return new Vec3(x + v.x, y + v.y, z + v.z);
	}

	public Vec3 sub(Vec3 v){
		return new Vec3(x - v.x, y - v.y, z - v.z);
	}

	public Vec3 mul(Vec3 v){
		return new Vec3(x * v.x, y * v.y, z * v.z);
	}

	public Vec3 div(Vec3 v){
		return new Vec3(x / v.x, y / v.y, z / v.z);
	}

	public Vec3 cross(Vec3 v){
		return new Vec3(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x);
	}

	public Vec3 normalize(){
		float len = mag();
		return new Vec3(x / len, y / len, z / len);
	}

	public float mag(){
		return (float) Math.sqrt(x*x + y*y+ z*z);
	}

	public float dot(Vec3 v){
		return (x * v.x + y * v.y + z * v.z) / (mag() * v.mag()) ;
	}

	public static Vec3 UnitX(){
		return new Vec3(1,0,0);
	}

	public static Vec3 UnitY(){
		return new Vec3(0,1,0);
	}

	public static Vec3 UnitZ(){
		return new Vec3(0,0,1);
	}

	public static Vec3 Zero(){
		return new Vec3(0,0,0);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public static Vec3 One() {
		return new Vec3(1,1,1);
	}
}
