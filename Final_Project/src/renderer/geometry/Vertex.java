package renderer.geometry;

import renderer.math.Mat4;
import renderer.math.Vec3;

public class Vertex {
	private Vec3 local;
	private Vec3 world;
	private Vec3 projected;

	public Vertex(float x, float y, float z) {
		super();
		this.local = new Vec3(x, y, z);
		this.world = this.local;
		this.projected = this.local;
	}

	public Vertex(Vertex v) {
		super();
		this.local = new Vec3(v.local);
		this.world = new Vec3(v.world);
		this.projected = new Vec3(v.projected);
	}

	public Vertex(Vec3 v) {
		super();
		this.local = new Vec3(v);
	}

	public Vec3 getLocal() {
		return local;
	}

	public Vec3 getWorld() {
		return world;
	}

	public Vec3 getProjected() {
		return projected;
	}

	public void generateWP(Mat4 worldMat, Mat4 projMat){
		world = worldMat.mul(local);
		projected = projMat.mul(worldMat.mul(local));
	}
}
