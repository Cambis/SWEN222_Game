package renderer.geometry;

import renderer.math.Mat4;
import renderer.math.Vec3;

/**
 *
 * @author Stephen Thompson
 *
 */
public class Vertex {
	// the local coordinates
	private Vec3 local;

	// the world coordinates
	private Vec3 world;

	// the projected coordinates
	private Vec3 projected;

	/***
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vertex(float x, float y, float z) {
		super();
		this.local = new Vec3(x, y, z);
		this.world = this.local;
		this.projected = this.local;
	}

	/**
	 *
	 * @param v
	 */
	public Vertex(Vertex v) {
		super();
		this.local = new Vec3(v.local);
		this.world = new Vec3(v.world);
		this.projected = new Vec3(v.projected);
	}

	/**
	 *
	 * @param v
	 */
	public Vertex(Vec3 v) {
		super();
		this.local = new Vec3(v);
	}

	/**
	 *
	 * @return
	 */
	public Vec3 getLocal() {
		return local;
	}

	/**
	 *
	 * @return
	 */
	public Vec3 getWorld() {
		return world;
	}

	/**
	 *
	 * @return
	 */
	public Vec3 getProjected() {
		return projected;
	}

	/**
	 *
	 * @param worldMat
	 * @param projMat
	 */
	public void generateWP(Mat4 worldMat, Mat4 projMat){
		world = worldMat.mul(local);
		projected = projMat.mul(local);
	}
}
