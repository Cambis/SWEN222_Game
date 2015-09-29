package renderer.geometry;

import java.awt.Color;
import java.awt.image.BufferedImage;

import renderer.math.Mat4;
import renderer.math.Vec3;

public class Polygon {
	private final Vertex v1;
	private final Vertex v2;
	private final Vertex v3;

	public Polygon(Vertex v1, Vertex v2, Vertex v3) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}



	public Vertex getV1() {
		return v1;
	}



	public Vertex getV2() {
		return v2;
	}



	public Vertex getV3() {
		return v3;
	}


	private void swap(Vertex v[], int i1, int i2){
		Vertex temp = v[i1];
		v[i1] = v[i2];
		v[i2] = temp;
	}

	public void draw(BufferedImage viewport, float[][] zBuffer, Mat4 viewProjMatrix, Mat4 modelMatrix, Color col) {
		Vertex modV1 = new Vertex(modelMatrix.mul(v1.getPosition()));
		Vertex modV2 = new Vertex(modelMatrix.mul(v2.getPosition()));
		Vertex modV3 = new Vertex(modelMatrix.mul(v3.getPosition()));

		Vertex tempV1 = new Vertex(viewProjMatrix.mul(modV1.getPosition()));
		Vertex tempV2 = new Vertex(viewProjMatrix.mul(modV2.getPosition()));
		Vertex tempV3 = new Vertex(viewProjMatrix.mul(modV3.getPosition()));

		Vec3 direction = (tempV3.getPosition().sub(tempV2.getPosition())).cross(tempV2.getPosition().sub(tempV1.getPosition()));

		if (direction.getZ() < 0){
			return;
		}

		Vertex v[] = new Vertex[]{tempV1, tempV2, tempV3, tempV3};

		// Sort vertices in order of -y to +y
		if (v[0].getPosition().getY() > v[1].getPosition().getY()) {swap(v, 0, 1);}
		if (v[1].getPosition().getY() > v[2].getPosition().getY()) {swap(v, 1, 2);}
		if (v[0].getPosition().getY() > v[1].getPosition().getY()) {swap(v, 0, 1);}
		v[3] = v[2];

		// Find 4th coord
		float yMid = v[1].getPosition().getY();
		float xMid = v[0].getPosition().getX() + ((v[1].getPosition().getY() - v[0].getPosition().getY()) /
												  (v[2].getPosition().getY() - v[0].getPosition().getY())) *
												  (v[2].getPosition().getX() - v[0].getPosition().getX());

		float zMid = v[0].getPosition().getZ() + ((v[1].getPosition().getY() - v[0].getPosition().getY()) /
				  (v[2].getPosition().getY() - v[0].getPosition().getY())) *
				  (v[2].getPosition().getZ() - v[0].getPosition().getZ());

		Vertex mid = new Vertex(xMid, yMid, zMid);
		v[3] = v[2];
		v[2] = mid;
		if (v[1].getPosition().getX() > v[2].getPosition().getX()) {swap(v, 1, 2);}

		// Light
		float spot = 0;
		Vec3 normal = (modV3.getPosition().sub(modV2.getPosition())).cross(modV2.getPosition().sub(modV1.getPosition()));

		Vec3 vdir = tempV1.getPosition().sub(new Vec3(0, 1, 0));
		Vec3 ldir = new Vec3(-1, -1, 0);
		if (vdir.dot(ldir) > 0.8){
			spot = Math.max(0, Math.min(1, normal.dot(ldir)));
		}
		Vec3 amb = new Vec3(0.25f, 0.25f, 0.25f);
		float light = spot + Math.max(0, Math.min(1, normal.dot(new Vec3(-0.5f, -0.25f, -0.5f))));

		int c = new Color((int)(Math.max(0, Math.min(255, col.getRed() * (amb.getX() + light)))),
					      (int)(Math.max(0, Math.min(255, col.getGreen() * (amb.getY() + light)))),
						  (int)(Math.max(0, Math.min(255, col.getBlue() * (amb.getZ() + light))))).getRGB();
		// Fill top half
		if (v[0].getPosition().getY() < v[1].getPosition().getY() && v[0].getPosition().getY() < v[2].getPosition().getY() ){
			fillTopTri(viewport, zBuffer, v, c);
		}

		// fill bottom half
		if (v[1].getPosition().getY() < v[3].getPosition().getY() && v[2].getPosition().getY() < v[3].getPosition().getY() ){
			fillBottomTri(viewport, zBuffer, v, c);
		}
	}

	private void fillTopTri(BufferedImage viewport, float[][] zBuffer, Vertex[] v, int c){
		int min = (int)v[0].getPosition().getY();
		int max = (int)v[1].getPosition().getY();

		// X
		float angXLeft = (v[1].getPosition().getX() - v[0].getPosition().getX()) / (v[1].getPosition().getY() - v[0].getPosition().getY());
		float angXRight = (v[2].getPosition().getX() - v[0].getPosition().getX()) / (v[2].getPosition().getY() - v[0].getPosition().getY());

		float xL = v[0].getPosition().getX();
		float xR = v[0].getPosition().getX();

		for (int y = min; y < max; ++y){

			xL += angXLeft;
			xR += angXRight;

			if (y < 0 || y >= viewport.getHeight()){
				continue;
			}

			int xMin = (int) Math.max(0, Math.floor(xL));
			int xMax = (int) Math.min(viewport.getWidth()-1, Math.ceil(xR));


			// Dirty Fix
			if (y == max-1){
				xMin = (int) v[1].getPosition().getX();
				xMax = (int) v[2].getPosition().getX();
			}

			// Z Buffer
			float yfrac = ((float)(y - min)) / (max - min);
			float zLeft = v[0].getPosition().getZ() * (1 - yfrac) + v[1].getPosition().getZ() * yfrac;
			float zRight = v[0].getPosition().getZ() * (1 - yfrac) + v[2].getPosition().getZ() * yfrac;

			for (int x = xMin; x < xMax; ++x){
				if (xMin < 0 || xMax >= viewport.getWidth()-1) continue;
				float xfrac = ((float)(x - xMin)) / (xMax - xMin);
				float zVal = zLeft*(1-xfrac) + zRight*xfrac;
				if (zVal < zBuffer[x][y]){
					viewport.setRGB(x, y, c);
					zBuffer[x][y] = zVal;
				}
			}
		}
	}

	private void fillBottomTri(BufferedImage viewport, float[][] zBuffer, Vertex[] v, int c){
		int min = (int)v[1].getPosition().getY();
		int max = (int)v[3].getPosition().getY();

		float angXLeft = (v[3].getPosition().getX() - v[1].getPosition().getX()) / (v[3].getPosition().getY() - v[1].getPosition().getY());
		float angXRight = (v[3].getPosition().getX() - v[2].getPosition().getX()) / (v[3].getPosition().getY() - v[2].getPosition().getY());

		float xL = v[1].getPosition().getX();
		float xR = v[2].getPosition().getX();


		for (int y = min; y < max; ++y){
			xL += angXLeft;
			xR += angXRight;

			int xMin = (int) Math.max(0, Math.floor(xL));
			int xMax = (int) Math.min(viewport.getWidth()-1, Math.ceil(xR));

			if (y < 0 || y >= viewport.getHeight()){
				continue;
			}

			// Z Buffer
			float yfrac = ((float)(y - min)) / (max - min);
			float zLeft = v[1].getPosition().getZ() * (1 - yfrac) + v[3].getPosition().getZ() * yfrac;
			float zRight = v[2].getPosition().getZ() * (1 - yfrac) + v[3].getPosition().getZ() * yfrac;

			for (int x = xMin; x < xMax; ++x){
				float xfrac = ((float)(x - xMin)) / (xMax - xMin);
				float zVal = zLeft*(1-xfrac) + zRight*xfrac;
				if (zVal < zBuffer[x][y]){
					viewport.setRGB(x, y, c);
					zBuffer[x][y] = zVal;
				}
			}
		}
	}
}
