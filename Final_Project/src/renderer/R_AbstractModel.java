package renderer;

import renderer.geometry.Model;
import renderer.math.Vec3;

public abstract class R_AbstractModel {
	private final String name;
	private final Model model;
	private Vec3 position;
	private Vec3 orientation;


	public R_AbstractModel(String name, String modelfilepath, Vec3 position, Vec3 orientation) {
		super();
		this.position = position;
		this.orientation = orientation;
		this.name = name;
		this.model = new Model(modelfilepath);
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

	public String getName() {
		return name;
	}

	protected Model getModel(){
		return model;
	}

}
