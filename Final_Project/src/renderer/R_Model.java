package renderer;

import renderer.math.Vec3;

/**
 * This class represents a scenery model
 *
 * @author Stephen Thompson
 */
public class R_Model extends R_AbstractModel {

	public R_Model(String name, R_AbstractModelData model, Vec3 position, Vec3 orientation, Vec3 scale) {
		super(name, model, position, orientation, scale);
	}
}
