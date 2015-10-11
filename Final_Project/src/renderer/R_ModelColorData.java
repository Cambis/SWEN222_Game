package renderer;

import game.logic.StealthGame;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import renderer.geometry.Polygon;
import renderer.geometry.Vertex;
import renderer.math.Mat4;
import resource.ResourceLoader;

/**
 *	This class holds the data required to draw a colored model
 *
 * @author Stephen Thompson
 *
 */
public class R_ModelColorData extends R_AbstractModelData {
	// The color used when drawing the model
	private Color col;

	/**
	 * The R_ModelColorData constructor
	 *
	 * @param name	the model's name
	 * @param pathname the path to the model
	 * @param col	the model's color
	 */
	public R_ModelColorData(String name, String pathname, Color col) {
		super(name);
		load(pathname);
		this.col = col;
	}

	/**
	 * Loads a new model from an .obj file
	 *
	 * @param pathname - the filepath to the .obj file
	 */
	private void load(String pathname) {
		verts = new ArrayList<Vertex>();
		polys = new ArrayList<Polygon>();

		try {
			// Opens up a file for reading
			Scanner sc;
			if (StealthGame.EXPORT)
				sc = new Scanner(ResourceLoader.load(pathname));
			else
				sc = new Scanner(new File(pathname));

			verts.add(new Vertex(0, 0, 0));
			while (sc.hasNextLine()) {
				// If the line starts with 'v' then read a vertex
				if (sc.hasNext("v")) {
					sc.next();
					verts.add(new Vertex(sc.nextFloat(), sc.nextFloat(), sc.nextFloat()));
				}

				// If the line starts with 'f' then read a vertex
				if (sc.hasNext("f")) {
					sc.next();
					polys.add(new Polygon(sc.nextInt(), sc.nextInt(), sc.nextInt()));
				}
				sc.nextLine();
			}
			sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draws the model to 2 buffers - the viewport and the zBuffer
	 *tileSize
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
	@Override
	protected void draw(int[] viewport, float[][] zBuffer, int width, int height, Mat4 viewProjMatrix, Mat4 modelMatrix,
			List<Light> lights, R_Player.Team side, R_Player.Team visible, int[][] shadowMap, int tileSize) {
		// Generates matrix transformation positions for each vertex
		for (Vertex v : verts) {
			v.generateWP(modelMatrix, viewProjMatrix);
		}

		// Draws the polygons
		for (Polygon p : polys) {
			p.draw(viewport, zBuffer, width, height, col, verts, lights, side, visible, shadowMap, tileSize);
		}
	}
}
