package renderer;

import java.awt.image.BufferedImage;

import renderer.math.Mat4;
import renderer.math.Vec3;

public abstract class R_AbstractModel {
	private final String name;
	private final R_AbstractModelData model;

	private Vec3 position;
	private Vec3 orientation;
	private Vec3 scale;

	public R_AbstractModel(String name, R_AbstractModelData model, Vec3 position, Vec3 orientation, Vec3 scale) {
		super();
		this.position = position;
		this.orientation = orientation;
		this.scale = scale;
		this.name = name;
		this.model = model;
	}

	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public Vec3 getOrientation() {
		return orientation;
	}

	public void setOrientation(Vec3 orientation) {
		this.orientation = orientation;
	}

	public Vec3 getScale() {
		return scale;
	}

	public void setScale(Vec3 scale) {
		this.scale = scale;
	}

	public String getName() {
		return name;
	}

	protected R_AbstractModelData getModel(){
		return model;
	}

	protected void draw(int[] viewport, float[][] zBuffer, int width, int height, Mat4 viewProjMatrix) {
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
		model.draw(viewport, zBuffer, width, height, viewProjMatrix, modelMatrix);
	}
}
