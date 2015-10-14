package renderer;

import java.util.List;

import renderer.math.Mat4;
import renderer.math.Vec3;

/**
 *	This class holds a R_AbstractModelData object and draws it in the correct position, orientation and scale
 *
 * @author Stephen Thompson
 * ID: 300332215
 */
public abstract class R_AbstractModel {
	// the model's name
	private final String name;

	// the model's data object
	private final R_AbstractModelData model;

	// the model's position
	private Vec3 position;

	// the model's orientation
	private Vec3 orientation;

	// the model's scale
	private Vec3 scale;


	/**
	 * @param name -the model's name
	 * @param model - the model data to use, this model does not hold the actual data, but rather draws the modeldata at the correct position
	 * 				  *Note that only one modeldata object is required for the enitre program
	 * @param position - the model's position
	 * @param orientation - the model's orientation
	 * @param scale - the model's scale
	 */
	public R_AbstractModel(String name, R_AbstractModelData model, Vec3 position, Vec3 orientation, Vec3 scale) {
		super();
		this.position = position;
		this.orientation = orientation;
		this.scale = scale;
		this.name = name;
		this.model = model;
	}

	/**
	 * @return - returns the model's position
	 */
	public Vec3 getPosition() {
		return position;
	}

	/**
	 * @param position - sets the model's position
	 */
	public void setPosition(Vec3 position) {
		this.position = position;
	}

	/**
	 * @return - returns the model's orientation
	 */
	public Vec3 getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation - sets the model's orientation
	 */
	public void setOrientation(Vec3 orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return - returns the model's scale
	 */
	public Vec3 getScale() {
		return scale;
	}

	/**
	 * @param scale - sets the model's scale
	 */
	public void setScale(Vec3 scale) {
		this.scale = scale;
	}

	/**
	 * @return - returns the model's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return - returns the model's modeldata object
	 */
	protected R_AbstractModelData getModel(){
		return model;
	}


	/**
	 * Draws the model to 2 buffers - the viewport and the zBuffer
	 *tileSize
	 * @param viewport	the buffer to draw the pixels to
	 * @param zBuffer	the zbuffer used to determine whether to draw the pixel
	 * @param width		the width of the screen
	 * @param height	the height of the screen
	 * @param viewProjMatrix	the matrix used to convert the vertices into screen space
	 * @param lights	a list of lights to use during the render
	 * @param side		the team's side to view
	 * @param visible	the object's team
	 * @param shadowMap	the 2d array of the map's layout
	 * @param tileSize	the tile's size used to map the positions to the shadowmap
	 */
	protected void draw(int[] viewport, float[][] zBuffer, int width, int height, Mat4 viewProjMatrix, List<Light> lights,
										R_Player.Team side, R_Player.Team visible, int[][] shadowMap, int tileSize) {
		// Translate to position
		Mat4 modelMatrix = Mat4.createTranslate(position);

		// Rotate around y
		modelMatrix = modelMatrix.mul(Mat4.createRotationYAxis(orientation.getY()));

		// Rotate around z
		modelMatrix = modelMatrix.mul(Mat4.createRotationZAxis(orientation.getZ()));

		// Rotate around x
		modelMatrix = modelMatrix.mul(Mat4.createRotationXAxis(orientation.getX()));

		// Scale
		modelMatrix = modelMatrix.mul(Mat4.createScale(scale));

		// Finally draw object
		model.draw(viewport, zBuffer, width, height, viewProjMatrix.mul(modelMatrix), modelMatrix, lights, side, visible, shadowMap, tileSize);
	}
}
