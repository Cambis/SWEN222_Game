package renderer.math;

public class Mat2 {
	final float[] matrix;

	private Mat2(float[] matrix){
		this.matrix = matrix;
	}

	public static Mat2 createRotation(float angle){
		return new Mat2(new float[]{(float) Math.cos(angle), (float) -Math.sin(angle), (float) Math.sin(angle), (float) Math.cos(angle)});
	}

	public Vec2 mul(Vec2 v){
		return new Vec2(v.getX() * matrix[0] + v.getY() * matrix[1], v.getX() * matrix[2] + v.getY() * matrix[3]);
	}
}
