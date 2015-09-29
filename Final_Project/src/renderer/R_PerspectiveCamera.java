package renderer;

import renderer.math.Mat4;
import renderer.math.Vec3;

public class R_PerspectiveCamera extends R_AbstractCamera{
	private float angle;

	public R_PerspectiveCamera(String name, Vec3 position, Vec3 target,
			Vec3 up, float near, float far, float angle) {
		super(name, position, target, up, near, far);
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}


	@Override
	protected Mat4 getProjection() {
		return Mat4.createPerspective(angle, aspect, near, far);
	}
}
