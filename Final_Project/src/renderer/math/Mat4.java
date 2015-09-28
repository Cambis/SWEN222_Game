package renderer.math;

public class Mat4 {
	final float[] matrix;

	private Mat4(float[] matrix){
		this.matrix = matrix;
	}

	public static Mat4 createTranslate(float x, float y, float z){
		return new Mat4(new float[]{1, 0, 0, x,
									0, 1, 0, y,
									0, 0, 1, z,
									0, 0, 0, 1});
	}

	public static Mat4 createRotationXAxis(float angle){
		return new Mat4(new float[]{ 1, 0, 0, 0,
									 0, (float)Math.cos(angle), (float) -Math.sin(angle), 0,
									 0, (float) Math.sin(angle), (float) Math.cos(angle), 0,
									0, 0, 0, 1});
	}

	public static Mat4 createRotationYAxis(float angle){
		return new Mat4(new float[]{(float) Math.cos(angle), 0, (float) Math.sin(angle), 0,
									0, 1, 0, 0,
									(float) -Math.sin(angle), 0, (float) Math.cos(angle), 0,
									0, 0, 0, 1});
	}

	public static Mat4 createRotationZAxis(float angle){
		return new Mat4(new float[]{(float) Math.cos(angle), (float) -Math.sin(angle), 0, 0,
									(float) Math.sin(angle), (float) Math.cos(angle), 0, 0,
									0, 0, 1, 0,
									0, 0, 0, 1});
	}

	public static Mat4 createOrtho(float l, float r, float b, float t, float n, float f){
		return new Mat4(new float[]{2/(r-l),       0,          0, -((r+l)/(r-l)),
										  0, 2/(t-b),          0, -((t+b)/(t-b)),
										  0,       0, (-2)/(f-n),    (f+n)/(f-n),
										  0,       0,          0,              1 });
	}

	public static Mat4 createFrustum(float l, float r, float b, float t, float n, float f){
		return new Mat4(new float[]{(2*n)/(r-l),           0,    (r+l)/(r-l),          0,
									          0, (2*n)/(t-b),    (t+b)/(t-b),          0,
									          0,           0, -((f+n)/(f-n)), (-2*f*n)/(f-n),
									          0,           0,          -1,              0 });
	}


	public static Mat4 createPerspective(float angle, float aspect, float n, float f){
		float tanAng = (float) Math.tan(Math.toRadians(angle/2));
		return new Mat4(new float[]{1/(aspect*tanAng),  0, 0, 0,
									          0, 1/tanAng, 0, 0,
									          0,        0, (-n-f)/(n-f), (-2*f*n)/(n-f),
									          0,        0,-1, 0 });
	}

	public static Mat4 createLookAt(Vec3 eye, Vec3 target, Vec3 up){
		Vec3 b3 = eye.sub(target).normalize();
		Vec3 b1 = up.cross(b3).normalize();
		Vec3 b2 = b3.cross(b1).normalize();

		return new Mat4(new float[]{b1.getX(), b1.getY(), b1.getZ(), -b1.dot(eye),
									b2.getX(), b2.getY(), b2.getZ(), -b2.dot(eye),
									b3.getX(), b3.getY(), b3.getZ(), -b3.dot(eye),
									        0,         0,        0,          1 });
	}

	public static Mat4 createScale(float x, float y, float z){
		return new Mat4(new float[]{x, 0, 0, 0,
									0, y, 0, 0,
									0, 0, z, 0,
									0, 0, 0, 1});
	}

	public Vec3 mul(Vec3 v){
		return new Vec3(v.getX() * matrix[0] + v.getY() * matrix[1] + v.getZ() * matrix[2] + matrix[3],
						v.getX() * matrix[4] + v.getY() * matrix[5] + v.getZ() * matrix[6] + matrix[7],
						v.getX() * matrix[8] + v.getY() * matrix[9] + v.getZ() * matrix[10] + matrix[11]);
	}

	public Mat4 mul(Mat4 v){
		return new Mat4(new float[]{
				matrix[0] * v.matrix[0] + matrix[1] * v.matrix[4] + matrix[2] * v.matrix[8] + matrix[3] * v.matrix[12],
				matrix[0] * v.matrix[1] + matrix[1] * v.matrix[5] + matrix[2] * v.matrix[9] + matrix[3] * v.matrix[13],
				matrix[0] * v.matrix[2] + matrix[1] * v.matrix[6] + matrix[2] * v.matrix[10] + matrix[3] * v.matrix[14],
				matrix[0] * v.matrix[3] + matrix[1] * v.matrix[7] + matrix[2] * v.matrix[11] + matrix[3] * v.matrix[15],

				matrix[4] * v.matrix[0] + matrix[5] * v.matrix[4] + matrix[6] * v.matrix[8] + matrix[7] * v.matrix[12],
				matrix[4] * v.matrix[1] + matrix[5] * v.matrix[5] + matrix[6] * v.matrix[9] + matrix[7] * v.matrix[13],
				matrix[4] * v.matrix[2] + matrix[5] * v.matrix[6] + matrix[6] * v.matrix[10] + matrix[7] * v.matrix[14],
				matrix[4] * v.matrix[3] + matrix[5] * v.matrix[7] + matrix[6] * v.matrix[11] + matrix[7] * v.matrix[15],

				matrix[8] * v.matrix[0] + matrix[9] * v.matrix[4] + matrix[10] * v.matrix[8] + matrix[11] * v.matrix[12],
				matrix[8] * v.matrix[1] + matrix[9] * v.matrix[5] + matrix[10] * v.matrix[9] + matrix[11] * v.matrix[13],
				matrix[8] * v.matrix[2] + matrix[9] * v.matrix[6] + matrix[10] * v.matrix[10] + matrix[11] * v.matrix[14],
				matrix[8] * v.matrix[3] + matrix[9] * v.matrix[7] + matrix[10] * v.matrix[11] + matrix[11] * v.matrix[15],

				matrix[12] * v.matrix[0] + matrix[13] * v.matrix[4] + matrix[14] * v.matrix[8] + matrix[15] * v.matrix[12],
				matrix[12] * v.matrix[1] + matrix[13] * v.matrix[5] + matrix[14] * v.matrix[9] + matrix[15] * v.matrix[13],
				matrix[12] * v.matrix[2] + matrix[13] * v.matrix[6] + matrix[14] * v.matrix[10] + matrix[15] * v.matrix[14],
				matrix[12] * v.matrix[3] + matrix[13] * v.matrix[7] + matrix[14] * v.matrix[11] + matrix[15] * v.matrix[15]});
	}

	public static Mat4 createTranslate(Vec3 position) {
		return createTranslate(position.getX(), position.getY(), position.getZ());
	}

	public static Mat4 createIdentity() {
		return new Mat4(new float[]{1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1});
	}

	public static Mat4 createScale(Vec3 scale) {
		return createScale(scale.getX(), scale.getY(), scale.getZ());
	}
}
