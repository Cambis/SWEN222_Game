package renderer;

import game.logic.StealthGame;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import renderer.geometry.Polygon;
import renderer.geometry.Vertex;
import renderer.math.Mat4;
import resource.ResourceLoader;

public class R_ModelColorData extends R_AbstractModelData {
	private Color col;

	public R_ModelColorData(String name, String pathname, Color col) {
		super(name);
		load(pathname);
		this.col = col;
	}

	private void load(String pathname) {
		verts = new ArrayList<Vertex>();
		polys = new ArrayList<Polygon>();

		try {

			Scanner sc;

			// Scanner sc = new Scanner(new File(pathname));
			if (StealthGame.EXPORT)
				sc = new Scanner(ResourceLoader.load(pathname));
			else
				sc = new Scanner(new File(pathname));

			verts.add(new Vertex(0, 0, 0));
			while (sc.hasNextLine()) {
				if (sc.hasNext("v")) {
					sc.next();
					verts.add(new Vertex(sc.nextFloat(), sc.nextFloat(), sc
							.nextFloat()));
				}

				if (sc.hasNext("f")) {
					sc.next();
					polys.add(new Polygon(sc.nextInt(), sc.nextInt(), sc
							.nextInt()));
				}
				sc.nextLine();
			}
			sc.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void draw(int[] viewport, float[][] zBuffer, int width,
			int height, Mat4 viewProjMatrix, Mat4 modelMatrix,
			List<Light> lights, R_Player.Team side, R_Player.Team visible,
			int[][] shadowMap, int tileSize) {
		for (Vertex v : verts) {
			v.generateWP(modelMatrix, viewProjMatrix);
		}
		for (Polygon p : polys) {
			p.draw(viewport, zBuffer, width, height, col, verts, lights, side,
					visible, shadowMap, tileSize);
		}
	}
}
