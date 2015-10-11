package renderer;

import renderer.math.Mat4;
import renderer.math.Vec3;
/**
 * This class represents a camera in the renderer
 *
 * @author Stephen Thompson
 *
 */
public abstract class R_AbstractCamera {
	// the camera's name
	private final String name;

	// the camera's position
	private Vec3 position;

	// the camera's target to point towards
	private Vec3 target;

	// the camera's up vector
	private Vec3 up;

	// the camera's near plane
	protected float near;

	// the camera's far plane
	protected float far;

	// The camera's aspect ratio
	protected float aspect;

	/**
	 * The R_AbstractCamera constructor
	 * @param name - the camera's name
	 * @param position - the camera's position
	 * @param target - the camera's target to point towards
	 * @param up - the camera's up vector
	 * @param near - the camera's near plane
	 * @param far - the camera's far plane
	 */
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

	/**
	 * @return - returns the camera's position
	 */
	public Vec3 getPosition() {
		return position;
	}

	/**
	 * @param position - sets the camera's position
	 */
	public void setPosition(Vec3 position) {
		this.position = position;
	}

	/**
	 * @return - returns the camera's target
	 */
	public Vec3 getTarget() {
		return target;
	}

	/**
	 * @param target - sets the camera's target
	 */
	public void setTarget(Vec3 target) {
		this.target = target;
	}

	/**
	 * @return - returns the camera's up vector
	 */
	public Vec3 getUp() {
		return up;
	}

	/**
	 * @param up - sets the camera's up vector
	 */
	public void setUp(Vec3 up) {
		this.up = up;
	}

	/**
	 * @return - returns the camera's near plane
	 */
	public float getNear() {
		return near;
	}

	/**
	 *
	 * @param near - sets the camera's near plane
	 */
	public void setNear(float near) {
		this.near = near;
	}

	/**
	 * @return - returns the camera's far plane
	 */
	public float getFar() {
		return far;
	}

	/**
	 * @param far - sets the camera's far plane
	 */
	public void setFar(float far) {
		this.far = far;
	}

	/**
	 * @return - returns the camera's name
	 */
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

	/**
	 * @return - returns the camera's lookat matrix
	 */
	protected Mat4 getLookAt(){
		return Mat4.createLookAt(position, target, up).mul(Mat4.createTranslate(Vec3.Zero().sub(target)));
	}

	/**
	 * @return - returns the camera's projection matrix
	 */
	protected abstract Mat4 getProjection();
}
