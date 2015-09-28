package renderer.math;

public class Vec3 {
	private final float x;
	private final float y;
	private final float z;

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
		return new Vec3(0,-1,0);
	}

	public static Vec3 UnitZ(){
		return new Vec3(0,0,1);
	}

	public static Vec3 Zero(){
		return new Vec3(0,0,0);
	}
}
