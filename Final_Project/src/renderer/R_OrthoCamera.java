package renderer;

import java.awt.geom.Rectangle2D;

import renderer.math.Vec3;

public class R_OrthoCamera extends R_AbstractCamera{
	private Rectangle2D dimension;

	public R_OrthoCamera(String name, Vec3 position, Vec3 target, Vec3 up,
			float near, float far, Rectangle2D dimension) {
		super(name, position, target, up, near, far);
		this.dimension = dimension;
	}

	public Rectangle2D getDimension() {
		return dimension;
	}

	public void setDimension(Rectangle2D dimension) {
		this.dimension = dimension;
	}
}
