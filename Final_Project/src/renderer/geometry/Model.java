package renderer.geometry;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {
	private List<Polygon> polys;

	public Model(){
		polys = new ArrayList<Polygon>();
	}

	public Model(String pathname){
		load(pathname);
	}

	public void load(String pathname){
		List<Vertex> verts = new ArrayList<Vertex>();
		List<Polygon> po = new ArrayList<Polygon>();

		try {
			Scanner sc = new Scanner(new File(pathname));
			verts.add(new Vertex(0, 0, 0, new int[]{0}));
			while (sc.hasNextLine()){
				if (sc.hasNext("v")){
					sc.next();
					verts.add(new Vertex(sc.nextFloat(), sc.nextFloat(), sc.nextFloat(), new int[]{255, 0, 0, 0}));
				}

				if (sc.hasNext("f")){
					sc.next();
					po.add(new Polygon(new Vertex(verts.get(sc.nextInt())), new Vertex(verts.get(sc.nextInt())), new Vertex(verts.get(sc.nextInt()))));
				}
				sc.nextLine();
			}
			sc.close();

			polys = po;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rotate(float amount){
		for (Polygon p : polys){
			p.rotate(amount);
		}
	}

	public void draw(BufferedImage viewport, float[][] zBuffer){
		for (Polygon p : polys){
			p.draw(viewport, zBuffer);
		}
	}
}