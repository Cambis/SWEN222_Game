package renderer.math;

public class Vec2 {
	private final float x;
	private final float y;

	public Vec2(){
		super();
		this.x = 0;
		this.y = 0;
	}

	public Vec2(float x, float y){
		super();
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}



	public Vec2 add(Vec2 v){
		return new Vec2(x + v.x, y + v.y);
	}

	public Vec2 sub(Vec2 v){
		return new Vec2(x - v.x, y - v.y);
	}

	public Vec2 mul(Vec2 v){
		return new Vec2(x * v.x, y * v.y);
	}

	public Vec2 deiv(Vec2 v){
		return new Vec2(x / v.x, y / v.y);
	}
}
