package renderer;

import java.awt.Color;

import renderer.math.Vec3;

public class R_Model extends R_AbstractModel {

	public R_Model(String name, String modelFilePath, Vec3 position, Vec3 orientation, Vec3 scale, Color col) {
		super(name, modelFilePath, position, orientation, scale, col);
	}

}
