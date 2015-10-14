package renderer;

import java.util.List;

import renderer.geometry.Polygon;
import renderer.geometry.Vertex;
import renderer.math.Mat4;

/**
 * This abstract class holds the data used to draw the models
 *
 * @author Stephen Thompson
 * ID: 300332215
 */
public abstract class R_AbstractModelData {
	// This object's name
	private final String name;

	// the list of polygons inside this object
	protected List<Polygon> polys;

	// the list of vertices inside this object
	protected List<Vertex> verts;

	/**
	 * R_AbstractModelData Constructor
	 * @param name - the name of this object
	 */
	public R_AbstractModelData(String name) {
		super();
		this.name = name;
	}

	/**
	 * Gets this object's name
	 * @return this object's name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Draws the model to 2 buffers - the viewport and the zBuffer
	 *
	 * @param viewport	the buffer to draw the pixels to
	 * @param zBuffer	the zbuffer used to determine whether to draw the pixel
	 * @param width		the width of the screen
	 * @param height	the height of the screen
	 * @param viewProjMatrix	the matrix used to convert the vertices into screen space
	 * @param modelMatrix		the matrix used to convert the vertices into world space
	 * @param lights	a list of lights to use during the render
	 * @param side		the team's side to view
	 * @param visible	the object's team
	 * @param shadowMap	the 2d array of the map's layout
	 * @param tileSize	the tile's size used to map the positions to the shadowmap
	 */
	protected abstract void draw(int[] viewport, float[][] zBuffer, int width, int height, Mat4 viewProjMatrix, Mat4 modelMatrix,
								 List<Light> lights, R_Player.Team side, R_Player.Team visible, int[][] shadowMap, int tileSize);
}