package renderer;

import renderer.math.Mat4;
import renderer.math.Vec3;

/**
 * This class provides a camera for rendering a scene in an orthographic(2D) view
 *
 * @author Stephen Thompson
 * ID: 300332215
 */
public class R_OrthoCamera extends R_AbstractCamera{
	// The camera's scale factor, a higher scale shows more of the scene
	private float scale;

	/**
	 * Constructor for creating a new orthographic camera
	 *
	 * @param name		The name of the camera
	 * @param position	The position of the camera
	 * @param target	The position where the camera is pointing
	 * @param up		The up vector, use Vec3.UnitY if unsure of what to use
	 * @param near		The closest distance object can be to the camera to get drawn
	 * @param far		The farthest distance object can be to the camera to get drawn
	 * @param scale		The zoom factor
	 * @param aspect	The aspect ratio (renderer's width/render's height)
	 */
	public R_OrthoCamera(String name, Vec3 position, Vec3 target, Vec3 up,
						 float near, float far, float scale) {
		super(name, position, target, up, near, far);
		this.scale = scale;
	}

	/**
	 * @param scale	- sets the camera's scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * @return		the camera's scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * @return 		returns an orthographic projection matrix
	 */
	@Override
	protected Mat4 getProjection() {
		return Mat4.createOrtho(-aspect*scale, aspect*scale, scale, -scale, near, far);
	}
}
