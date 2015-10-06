package renderer;

import renderer.math.Mat4;
import renderer.math.Vec3;

public abstract class R_AbstractCamera {
	private final String name;
	private Vec3 position;
	private Vec3 target;
	private Vec3 up;
	protected float near;
	protected float far;

	// The camera's aspect ratio
	protected float aspect;

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

	/**
	 * @return - returns the camera's aspect ratio
	 */
	public float getAspect() {
		return aspect;
	}

	/**
	 * @param aspect - sets the camera's aspect ratio
	 */
	public void setAspect(float aspect) {
		this.aspect = aspect;
	}


	protected Mat4 getLookAt(){
		return Mat4.createLookAt(position, target, up).mul(Mat4.createTranslate(Vec3.Zero().sub(target)));
	}

	protected abstract Mat4 getProjection();
}
