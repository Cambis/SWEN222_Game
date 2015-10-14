package renderer.math;

import java.util.Arrays;

/**
 *	This class provides a number of methods for performing matrix math
 *
 * @author Stephen Thompson
 *
 */
public class Mat4 {
	// the matrix data array
	final float[] matrix;

	/**
	 * constructor for creating a new hardcoded matrix
	 * @param matrix - float array of the rows and columns
	 */
	private Mat4(float[] matrix){
		this.matrix = matrix;
	}

	/**
	 * @return - returns an identity matrix
	 */
	public static Mat4 createIdentity() {
		return new Mat4(new float[]{1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1});
	}

	/**
	 * Returns a translation matrix according to the passed in values
	 *
	 * @param x - the amount to translate the x axis by
	 * @param y - the amount to translate the y axis by
	 * @param z - the amount to translate the z axis by
	 * @return - a translation matrix
	 */
	public static Mat4 createTranslate(float x, float y, float z){
		return new Mat4(new float[]{1, 0, 0, x,
									0, 1, 0, y,
									0, 0, 1, z,
									0, 0, 0, 1});
	}

	/**
	 * Returns a translation matrix according to the passed in values
	 *
	 * @param position - the amount to translate by
	 * @return - returns a translation matrix
	 */
	public static Mat4 createTranslate(Vec3 position) {
		return createTranslate(position.getX(), position.getY(), position.getZ());
	}

	/**
	 * Returns a rotation matrix that rotates around the X axis by the passed in angle
	 *
	 * @param angle - the angle in radians to rotate by
	 * @return - a rotation matrix
	 */
	public static Mat4 createRotationXAxis(float angle){
		return new Mat4(new float[]{ 1, 0, 0, 0,
									 0, (float)Math.cos(angle), (float) -Math.sin(angle), 0,
									 0, (float) Math.sin(angle), (float) Math.cos(angle), 0,
									0, 0, 0, 1});
	}


	/**
	 * Returns a rotation matrix that rotates around the Y axis by the passed in angle
	 *
	 * @param angle - the angle in radians to rotate by
	 * @return - a rotation matrix
	 */
	public static Mat4 createRotationYAxis(float angle){
		return new Mat4(new float[]{(float) Math.cos(angle), 0, (float) Math.sin(angle), 0,
									0, 1, 0, 0,
									(float) -Math.sin(angle), 0, (float) Math.cos(angle), 0,
									0, 0, 0, 1});
	}


	/**
	 * Returns a rotation matrix that rotates around the Z axis by the passed in angle
	 *
	 * @param angle - the angle in radian to rotate by
	 * @return - a rotation matrix
	 */
	public static Mat4 createRotationZAxis(float angle){
		return new Mat4	(new float[]{(float) Math.cos(angle), (float) -Math.sin(angle), 0, 0,
									(float) Math.sin(angle), (float) Math.cos(angle), 0, 0,
									0, 0, 1, 0,
									0, 0, 0, 1});
	}

	/**
	 * Returns an orthographic projection matrix. The projection matrix transforms the camera coordinates into
	 * projection coordinates, values inside the view ranging from -1 to 1.
	 *
	 * @param l - the left clipping plane
	 * @param r - the right clipping plane
	 * @param b - the bottom clipping plane
	 * @param t - the top clipping plane
	 * @param n - the near clipping plane
	 * @param f - the far clipping plane
	 * @return - an orthographic projection matrix
	 */
	public static Mat4 createOrtho(float l, float r, float b, float t, float n, float f){
		return new Mat4(new float[]{2/(r-l),       0,          0, -((r+l)/(r-l)),
										  0, 2/(t-b),          0, -((t+b)/(t-b)),
										  0,       0, (-2)/(f-n),    (f+n)/(f-n),
										  0,       0,          0,              1 });
	}

	/**
	 * Returns a new lookat matrix. The lookat matrix transforms the world into camera coordinates
	 *
	 * @param eye - the position of the camera
	 * @param target - the target the camera is pointing towards
	 * @param up - the up vector
	 * @return a new lookat matrix
	 */
	public static Mat4 createLookAt(Vec3 eye, Vec3 target, Vec3 up){
		Vec3 b3 = eye.sub(target).normalize();
		Vec3 b1 = up.cross(b3).normalize();
		Vec3 b2 = b3.cross(b1).normalize();

		return new Mat4(new float[]{b1.getX(), b1.getY(), b1.getZ(), -b1.dot(eye),
									b2.getX(), b2.getY(), b2.getZ(), -b2.dot(eye),
									b3.getX(), b3.getY(), b3.getZ(), -b3.dot(eye),
									        0,         0,        0,          1 });
	}


	/**
	 * Returns a scale matrix according to the passed in values
	 *
	 * @param x - the amount to scale the x axis by
	 * @param y - the amount to scale the y axis by
	 * @param z - the amount to scale the z axis by
	 * @return - a scale matrix
	 */
	public static Mat4 createScale(float x, float y, float z){
		return new Mat4(new float[]{x, 0, 0, 0,
									0, y, 0, 0,
									0, 0, z, 0,
									0, 0, 0, 1});
	}

	/**
	 * Returns a scale matrix according to the values in the passed in vec3
	 *
	 * @param scale - a vec3 containing the scale values
	 * @return - a scale matrix
	 */
	public static Mat4 createScale(Vec3 scale) {
		return createScale(scale.getX(), scale.getY(), scale.getZ());
	}

	/**
	 * Multiplies a vec3 by this mat4
	 *
	 * @param v - the vec3 to multiply this matrix by
	 * @return - the product of this object and v
	 */
	public Vec3 mul(Vec3 v){
		return new Vec3(v.getX() * matrix[0] + v.getY() * matrix[1] + v.getZ() * matrix[2] + matrix[3],
						v.getX() * matrix[4] + v.getY() * matrix[5] + v.getZ() * matrix[6] + matrix[7],
						v.getX() * matrix[8] + v.getY() * matrix[9] + v.getZ() * matrix[10] + matrix[11]);
	}

	/**
	 * Multiplies a this mat4 by another mat4 object
	 *
	 * @param v - the matrix to multiply this object by
	 * @return - the product of this object and v
	 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(matrix);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mat4 other = (Mat4) obj;
		if (!Arrays.equals(matrix, other.matrix))
			return false;
		return true;
	}
}
