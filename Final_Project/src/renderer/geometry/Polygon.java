package renderer.geometry;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import renderer.Light;
import renderer.R_Player;
import renderer.math.Mat4;
import renderer.math.Vec3;

public class Polygon {
	private final int v1;
	private final int v2;
	private final int v3;

	public Polygon(int v1, int v2, int v3) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	private void swap(Vertex v[], int i1, int i2){
		Vertex temp = v[i1];
		v[i1] = v[i2];
		v[i2] = temp;
	}

	public void draw(int[] viewport, float[][] zBuffer, int width, int height, Color col, List<Vertex> vertices, List<Light> lights, R_Player.Team side, R_Player.Team visible) {
		Vertex tempV1 = vertices.get(v1);
		Vertex tempV2 = vertices.get(v2);
		Vertex tempV3 = vertices.get(v3);

		Vec3 direction = (tempV3.getProjected().sub(tempV2.getProjected())).cross(tempV2.getProjected().sub(tempV1.getProjected()));

		if (direction.getZ() < 0){
			return;
		}

		Vertex v[] = new Vertex[]{tempV1, tempV2, tempV3, tempV3};

		// Sort vertices in order of -y to +y
		if (v[0].getProjected().getY() > v[1].getProjected().getY()) {swap(v, 0, 1);}
		if (v[1].getProjected().getY() > v[2].getProjected().getY()) {swap(v, 1, 2);}
		if (v[0].getProjected().getY() > v[1].getProjected().getY()) {swap(v, 0, 1);}
		v[3] = v[2];

		// Find 4th coord
		float yMid = v[1].getProjected().getY();
		float xMid = v[0].getProjected().getX() + ((v[1].getProjected().getY() - v[0].getProjected().getY()) /
												  (v[2].getProjected().getY() - v[0].getProjected().getY())) *
												  (v[2].getProjected().getX() - v[0].getProjected().getX());

		float zMid = v[0].getProjected().getZ() + ((v[1].getProjected().getY() - v[0].getProjected().getY()) /
				  (v[2].getProjected().getY() - v[0].getProjected().getY())) *
				  (v[2].getProjected().getZ() - v[0].getProjected().getZ());

		Vertex mid = new Vertex(xMid, yMid, zMid);
		v[3] = v[2];
		v[2] = mid;
		if (v[1].getProjected().getX() > v[2].getProjected().getX()) {swap(v, 1, 2);}

		// Light
		float spot = 0;
		Vec3 normal = (tempV3.getWorld().sub(tempV2.getWorld())).cross(tempV2.getWorld().sub(tempV1.getWorld()));

		Vec3 positionAvg = tempV1.getWorld().add(
							tempV2.getWorld().add(
									tempV3.getWorld())).div(new Vec3(3, 3, 3));
		float light = 0;
		boolean inlight = false;
		for (Light l : lights){
			if (l.getSide() != side && visible == R_Player.Team.SCENE){
				continue;
			}
			Vec3 vdir = positionAvg.sub(l.getPosition());
			if (vdir.dot(l.getDirection()) > 0.8){
				inlight = true;
				light += Math.max(0, Math.min(1, normal.dot(vdir)));
			}
		}
		// Side == team side to view (G or S)
		// visible == team current model is on (any)
		if (side != visible && visible != R_Player.Team.SCENE){
			if (!inlight){
				return;
			}
		}
		Vec3 amb = new Vec3(0.25f, 0.25f, 0.25f);
		int c = new Color((int)(Math.max(0, Math.min(255, col.getRed() * (amb.getX() + light)))),
					      (int)(Math.max(0, Math.min(255, col.getGreen() * (amb.getY() + light)))),
						  (int)(Math.max(0, Math.min(255, col.getBlue() * (amb.getZ() + light))))).getRGB();
		// Fill top half
		if (v[0].getProjected().getY() < v[1].getProjected().getY() && v[0].getProjected().getY() < v[2].getProjected().getY() ){
			fillTopTri(viewport, zBuffer, width, height, v, c);
		}

		// fill bottom half
		if (v[1].getProjected().getY() < v[3].getProjected().getY() && v[2].getProjected().getY() < v[3].getProjected().getY() ){
			fillBottomTri(viewport, zBuffer, width, height, v, c);
		}
	}

	private void fillTopTri(int[] viewport, float[][] zBuffer, int width, int height, Vertex[] v, int c){
		int min = (int)Math.floor(v[0].getProjected().getY());
		int max = (int)Math.floor(v[2].getProjected().getY());

		// X
		float angXLeft = (v[1].getProjected().getX() - v[0].getProjected().getX()) / (v[1].getProjected().getY() - v[0].getProjected().getY());
		float angXRight = (v[2].getProjected().getX() - v[0].getProjected().getX()) / (v[2].getProjected().getY() - v[0].getProjected().getY());

		float xL = v[0].getProjected().getX();
		float xR = v[0].getProjected().getX();

		for (int y = min; y < max; ++y){

			if (y >= 0 && y < height){
				int xMin = (int) Math.max(0, xL);
				int xMax = (int) Math.min(width-1, xR);

				// Z Buffer
				float yfrac = ((float)(y - min)) / (max - min);
				float zLeft = v[0].getProjected().getZ() * (1 - yfrac) + v[1].getProjected().getZ() * yfrac;
				float zRight = v[0].getProjected().getZ() * (1 - yfrac) + v[2].getProjected().getZ() * yfrac;

				for (int x = xMin; x <= xMax; ++x){
					float xfrac = ((float)(x - xMin)) / (xMax - xMin);
					float zVal = zLeft*(1-xfrac) + zRight*xfrac;

					if (zVal < zBuffer[x][y]){
						viewport[width*y + x] = c;
						zBuffer[x][y] = zVal;
					}
				}
			}
			xL += angXLeft;
			xR += angXRight;
		}
	}

	private void fillBottomTri(int[] viewport, float[][] zBuffer, int width, int height, Vertex[] v, int c){
		int min = (int)Math.floor(v[1].getProjected().getY());
		int max = (int)Math.ceil(v[3].getProjected().getY());

		float angXLeft = (v[3].getProjected().getX() - v[1].getProjected().getX()) / (v[3].getProjected().getY() - v[1].getProjected().getY());
		float angXRight = (v[3].getProjected().getX() - v[2].getProjected().getX()) / (v[3].getProjected().getY() - v[2].getProjected().getY());

		float xL = v[1].getProjected().getX();
		float xR = v[2].getProjected().getX();


		for (int y = min; y < max; ++y){
			if (y >= 0 && y < height){
				int xMin = (int) Math.max(0, xL);
				int xMax = (int) Math.min(width-1, xR);

				// Z Buffer
				float yfrac = ((float)(y - min)) / (max - min);
				float zLeft = v[1].getProjected().getZ() * (1 - yfrac) + v[3].getProjected().getZ() * yfrac;
				float zRight = v[2].getProjected().getZ() * (1 - yfrac) + v[3].getProjected().getZ() * yfrac;

				for (int x = xMin; x <= xMax; ++x){
					float xfrac = ((float)(x - xMin)) / (xMax - xMin);
					float zVal = zLeft*(1-xfrac) + zRight*xfrac;

					if (zVal < zBuffer[x][y]){
						viewport[y*width + x] = c;
						zBuffer[x][y] = zVal;
					}
				}
			}

			xL += angXLeft;
			xR += angXRight;
		}
	}
}
