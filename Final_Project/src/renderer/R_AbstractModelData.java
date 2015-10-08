package renderer;

import java.util.List;

import renderer.geometry.Polygon;
import renderer.geometry.Vertex;
import renderer.math.Mat4;

public abstract class R_AbstractModelData {
	private final String name;
	protected List<Polygon> polys;
	protected List<Vertex> verts;

	public R_AbstractModelData(String name) {
		super();
		this.name = name;
	}

	public String getName(){
		return name;
	}

	protected abstract void draw(int[] viewport, float[][] zBuffer, int width, int height, Mat4 viewProjMatrix, Mat4 modelMatrix, List<Light> lights, R_Player.Team side, R_Player.Team visible);
}