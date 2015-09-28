package renderer;

import renderer.math.Vec3;

public class R_PerspectiveCamera extends R_AbstractCamera{
	private float angle;
	private float aspect;

	public R_PerspectiveCamera(String name, Vec3 position, Vec3 target,
			Vec3 up, float near, float far, float angle, float aspect) {
		super(name, position, target, up, near, far);
		this.angle = angle;
		this.aspect = aspect;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAspect() {
		return aspect;
	}

	public void setAspect(float aspect) {
		this.aspect = aspect;
	}
}
