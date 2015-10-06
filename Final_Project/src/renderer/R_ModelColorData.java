package renderer;

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

public class R_ModelColorData extends R_AbstractModelData {
	private Color col;


	public R_ModelColorData(String name, String pathname, Color col){
		super(name);
		load(pathname);
		this.col = col;
	}

	private void load(String pathname){
		verts = new ArrayList<Vertex>();
		polys = new ArrayList<Polygon>();

		try {
			Scanner sc = new Scanner(new File(pathname));
			verts.add(new Vertex(0, 0, 0));
			while (sc.hasNextLine()){
				if (sc.hasNext("v")){
					sc.next();
					verts.add(new Vertex(sc.nextFloat(), sc.nextFloat(), sc.nextFloat()));
				}

				if (sc.hasNext("f")){
					sc.next();
					polys.add(new Polygon(sc.nextInt(), sc.nextInt(), sc.nextInt()));
				}
				sc.nextLine();
			}
			sc.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void draw(int[] viewport, float[][] zBuffer, int width, int height, Mat4 viewProjMatrix, Mat4 modelMatrix, List<Light> lights) {
		for (Vertex v : verts){
			v.generateWP(modelMatrix, viewProjMatrix);
		}
		for (Polygon p : polys){
			p.draw(viewport, zBuffer, width, height, col, verts, lights);
		}
	}
}
