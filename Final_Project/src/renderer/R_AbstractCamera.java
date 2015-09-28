package renderer;

import renderer.math.Vec3;

public class R_AbstractCamera {
	private final String name;
	private Vec3 position;
	private Vec3 target;
	private Vec3 up;
	private float near;
	private float far;

	public R_AbstractCamera(String name, Vec3 position, Vec3 target, Vec3 up,
			float near, float far) {
		super();
		this.name = name;
		this.position = position;
		this.target = target;
		this.up = up;
		this.near = near;
		this.far = far;
	}

	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public Vec3 getTarget() {
		return target;
	}

	public void setTarget(Vec3 target) {
		this.target = target;
	}

	public Vec3 getUp() {
		return up;
	}

	public void setUp(Vec3 up) {
		this.up = up;
	}

	public float getNear() {
		return near;
	}

	public void setNear(float near) {
		this.near = near;
	}

	public float getFar() {
		return far;
	}

	public void setFar(float far) {
		this.far = far;
	}

	public String getName() {
		return name;
	}
}
