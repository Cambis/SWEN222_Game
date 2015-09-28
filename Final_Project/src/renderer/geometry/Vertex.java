package renderer.geometry;

import renderer.math.Mat4;
import renderer.math.Vec3;

public class Vertex {
	private Vec3 orig;
	private Vec3 position;
	private final int[] c;

	public Vertex(float x, float y, float z, int[] c) {
		super();
		this.orig = new Vec3(x, y, z);
		this.position = orig;
		this.c = c;
	}

	public Vertex(Vertex v) {
		super();
		this.orig = new Vec3(v.orig);
		this.position = orig;
		this.c = v.c;
	}

	public Vec3 getOrig() {
		return orig;
	}
	public Vec3 getPosition() {
		return position;
	}

	public void rotate(float amount){
		position = //Mat4.createTranslate(256, 256, 0).mul(//
				Mat4.createScale(600, 600, 600).mul(
				Mat4.createTranslate(0.8f, 0.65f, 0).mul(
								
				//Mat4.createOrtho(-2, 2, 2, -2, 1, 1000).mul(
				Mat4.createLookAt(new Vec3(3, -2, -3), Vec3.Zero(), Vec3.UnitY()).mul(
						Mat4.createScale(0.5f, 0.5f, 0.5f).mul(
								Mat4.createRotationYAxis(amount).mul(
									orig)
						)
						)
						///Mat4.createRotationXAxis(0.7f).mul(
						//		Mat4.createRotationYAxis(0.7f).mul(orig))

						/*Mat4.createRotationYAxis(amount).mul(
								Mat4.createRotationXAxis(amount).mul(orig))*/
						//)

				));

		/*
		position = //Mat4.createOrtho(0, 1, 1, 0, 1, 1000).mul(
						Mat4.createLookAt(new Vec3(5, 5, 0), Vec3.Zero(), Vec3.UnitY()).mul(
						//Mat4.createTranslate(256, 256, 0).mul(
							//Mat4.createScale(0.25f, 2f, 0.25f).mul(
							//Mat4.createTranslate(256, 256, 0).mul(
										Mat4.createRotationYAxis(amount).mul(
												Mat4.createRotationXAxis(amount).mul(orig)));
													//	Mat4.createTranslate(-256, -256, 0).mul(orig)))));*/
	}
	public int[] getColor() {
		return c;
	}


}
