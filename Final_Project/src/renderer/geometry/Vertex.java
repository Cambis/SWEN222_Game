package renderer.geometry;

import renderer.math.Vec3;

public class Vertex {
	private Vec3 position;

	public Vertex(float x, float y, float z) {
		super();
		this.position = new Vec3(x, y, z);
	}

	public Vertex(Vertex v) {
		super();
		this.position = new Vec3(v.position);
	}

	public Vertex(Vec3 v) {
		super();
		this.position = new Vec3(v);
	}


	public Vec3 getPosition() {
		return position;
	}
}
