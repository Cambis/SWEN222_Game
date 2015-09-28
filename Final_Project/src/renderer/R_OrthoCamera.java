package renderer;

import java.awt.geom.Rectangle2D;

import renderer.math.Mat4;
import renderer.math.Vec3;

public class R_OrthoCamera extends R_AbstractCamera{
	private Rectangle2D d;

	public R_OrthoCamera(String name, Vec3 position, Vec3 target, Vec3 up,
			float near, float far, Rectangle2D dimension) {
		super(name, position, target, up, near, far);
		this.d = dimension;
	}

	public Rectangle2D getDimension() {
		return d;
	}

	public void setDimension(Rectangle2D dimension) {
		this.d = dimension;
	}

	@Override
	protected Mat4 getProjection() {
		return Mat4.createOrtho((float)d.getMinX(), (float)d.getMaxX(), (float)d.getMaxY(), (float)d.getMinY(), near, far);

	}
}
