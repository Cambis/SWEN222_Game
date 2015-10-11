package renderer.geometry;

import renderer.math.Mat4;
import renderer.math.Vec3;

/**
 * This class represents a vertex, and stores the position in local, world and projected space
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
	 * @param v - creates a new vertex at position (x, y, z)
	 * @param x - the x value
	 * @param y - the y value
	 * @param z - the z value
	 */
	public Vertex(float x, float y, float z) {
		super();
		this.local = new Vec3(x, y, z);
		this.world = this.local;
		this.projected = this.local;
	}

	/**
	 * @param v - creates a clone of vertex v
	 */
	public Vertex(Vertex v) {
		super();
		this.local = new Vec3(v.local);
		this.world = new Vec3(v.world);
		this.projected = new Vec3(v.projected);
	}

	/**
	 * @param v - creates a new vertex at position v
	 */
	public Vertex(Vec3 v) {
		super();
		this.local = new Vec3(v);
	}

	/**
	 *
	 * @return - returns the local position
	 */
	public Vec3 getLocal() {
		return local;
	}

	/**
	 *
	 * @return - returns the world position
	 */
	public Vec3 getWorld() {
		return world;
	}

	/**
	 *
	 * @return - returns the projected position
	 */
	public Vec3 getProjected() {
		return projected;
	}

	/**
	 * Generates the positions of the vector in model space and screen space
	 * @param worldMat - the world matrix
	 * @param projMat - the mvp matrix
	 */
	public void generateWP(Mat4 worldMat, Mat4 projMat){
		world = worldMat.mul(local);
		projected = projMat.mul(local);
	}
}
