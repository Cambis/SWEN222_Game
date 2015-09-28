package renderer.geometry;

import java.awt.Color;
import java.awt.image.BufferedImage;

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

	public void rotate(float amount){
		v1.rotate(amount);
		v2.rotate(amount);
		v3.rotate(amount);
	}


	public void draw(BufferedImage viewport, float[][] zBuffer) {

		Vec3 normal = (v3.getPosition().sub(v2.getPosition())).cross(v2.getPosition().sub(v1.getPosition()));

		if (normal.getZ() < 0){
			return;
		}

		Vertex v[] = new Vertex[]{v1, v2, v3, v3};

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

		Vertex mid = new Vertex(xMid, yMid, zMid, new int[]{0,0,0});
		v[3] = v[2];
		v[2] = mid;
		if (v[1].getPosition().getX() > v[2].getPosition().getX()) {swap(v, 1, 2);}

		float spot = 0;

		Vec3 vdir = v1.getPosition().sub(new Vec3(5, -3, 5));
		Vec3 ldir = new Vec3(0, 1f, 0);
		if (vdir.dot(ldir) > 0.7){
			spot = Math.max(0, Math.min(1, normal.dot(ldir)));
		}
		float light = Math.max(0, Math.min(1, normal.dot(new Vec3(-0.5f, 0.75f, 1))));

		int c = new Color((int)(50 + 105 * light + 100*spot), (int)(255*spot), (int)(255*spot)).getRGB();
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
