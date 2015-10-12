package renderer.geometry;

import java.awt.Color;
import java.util.List;

import renderer.Light;
import renderer.R_Player;
import renderer.math.Vec3;

/**
 * Represents a polygon. Polygon objects hold 3 vertices and can draw a polygon to an array.
 *
 * @author Stephen Thompson
 *
 */
public class Polygon {
	// The index to the first vertex
	private final int v1;

	// The index to the second vertex
	private final int v2;

	// The index to the third vertex
	private final int v3;

	/**
	 * The polygon constructor
	 *
	 * @param v1 - index to the first vertex
	 * @param v2 - index to the second vertex
	 * @param v3 - index to the third vertex
	 */
	public Polygon(int v1, int v2, int v3) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	/**
	 * Swaps two vertices inside an array
	 * @param v - the array of vertices
	 * @param i1 - the index of the first vertex
	 * @param i2 - the index of the second vertex
	 */
	private void swap(Vertex v[], int i1, int i2){
		Vertex temp = v[i1];
		v[i1] = v[i2];
		v[i2] = temp;
	}

	/**
	 * Renders a triangl to a buffer
	 *
	 * @param viewport - the buffer to render to
	 * @param zBuffer - the zbuffers used to check whether to render or not
	 * @param width - the width of the viewport
	 * @param height - the height of the viewport
	 * @param col - the color of the polygon
	 * @param vertices - the list of vertices in the model
	 * @param lights - the list of lights in the scene
	 * @param side - the team side to render from
	 * @param visible - the model's team side. Models on the opposite team to side only render when in a spotlight
	 * @param shadowMap - the map of the map's walls
	 * @param tileSize - the size of the tiles on the map
	 */
	public void draw(int[] viewport, float[][] zBuffer, int width, int height, Color col, List<Vertex> vertices, List<Light> lights, R_Player.Team side, R_Player.Team visible, int[][] shadowMap, int tileSize) {
		Vertex tempV1 = vertices.get(v1);
		Vertex tempV2 = vertices.get(v2);
		Vertex tempV3 = vertices.get(v3);

		// Backface culling
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

		// Finds the 4th coordinate
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

		// Lighting
		// Uses the center of the face for light calculations
		float light = 0;
		boolean inlight = false;
		Vec3 normal = (tempV3.getWorld().sub(tempV2.getWorld())).cross(tempV2.getWorld().sub(tempV1.getWorld()));
		Vec3 positionAvg = tempV1.getWorld().add(
							tempV2.getWorld().add(
									tempV3.getWorld())).div(new Vec3(3, 3, 3));

		LightLoop : for (Light l : lights){
			// Only renders spotlights if the spotlight belongs to the same team as the renderer is viewing
			if (l.getSide() != side && visible == R_Player.Team.SCENE){
				continue;
			}
			// gets direction of spotlight to the center of the face
			Vec3 vdir = positionAvg.sub(l.getPosition());

			// Checks whether the
			if (vdir.dot(l.getDirection()) > l.getCutoff()){
				// Checks whether the face is in the shadow
				for (int i = 0; i < tileSize*2-1; ++i){
					Vec3 pos = Vec3.Lerp(l.getPosition(), positionAvg, (float)i/(tileSize*2)).div(new Vec3(0.02, 0.02, 0.02)).div(new Vec3(tileSize, tileSize, tileSize));
					pos = pos.add(new Vec3(0.5, 0.5, 0.5));
					if (pos.getX() >= 0 && pos.getZ() >= 0 && pos.getX() < shadowMap.length && pos.getZ() < shadowMap[0].length){
						if (shadowMap[(int)(pos.getX())][(int)(pos.getZ())] == 1){
							continue LightLoop;
						}
					}
				}

				// Display light
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
		// Add slight ambient and directional light
		light += Math.max(0, normal.dot(new Vec3(-0.3, -0.6, -0.9))) * 0.1f;
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

	/**
	 * Fills the top half of the triangle
	 *
	 * @param viewport - the buffer to render to
	 * @param zBuffer - the zbuffers used to check whether to render or not
	 * @param width - the width of the viewport
	 * @param height - the height of the viewport
	 * @param v - the array of vertices
	 * @param c - the color rgb interger value used to render the polygon
	 */
	private void fillTopTri(int[] viewport, float[][] zBuffer, int width, int height, Vertex[] v,  int c){
		int min = (int)Math.floor(v[0].getProjected().getY());
		int max = (int)Math.floor(v[2].getProjected().getY());

		// gets the slopes of the sides
		float angXLeft = (v[1].getProjected().getX() - v[0].getProjected().getX()) / (v[1].getProjected().getY() - v[0].getProjected().getY());
		float angXRight = (v[2].getProjected().getX() - v[0].getProjected().getX()) / (v[2].getProjected().getY() - v[0].getProjected().getY());

		float xL = v[0].getProjected().getX();
		float xR = v[0].getProjected().getX();

		// iterates through each y value in the polygon
		for (int y = min; y < max; ++y){
			if (y >= 0 && y < height){
				int xMin = (int) Math.max(0, xL);
				int xMax = (int) Math.min(width-1, xR);

				// Z Buffer
				float yfrac = ((float)(y - min)) / (max - min);
				float zLeft = v[0].getProjected().getZ() * (1 - yfrac) + v[1].getProjected().getZ() * yfrac;
				float zRight = v[0].getProjected().getZ() * (1 - yfrac) + v[2].getProjected().getZ() * yfrac;

				// iterates over row
				for (int x = xMin; x <= xMax; ++x){
					float xfrac = ((float)(x - xMin)) / (xMax - xMin);
					float zVal = zLeft*(1-xfrac) + zRight*xfrac;

					// if value is closer to the camera than what is in the zbuffer, add it
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

	/**
	 * Fills the bottom half of the triangle
	 *
	 * @param viewport - the buffer to render to
	 * @param zBuffer - the zbuffers used to check whether to render or not
	 * @param width - the width of the viewport
	 * @param height - the height of the viewport
	 * @param v - the array of vertices
	 * @param c - the color rgb interger value used to render the polygon
	 */
	private void fillBottomTri(int[] viewport, float[][] zBuffer, int width, int height, Vertex[] v, int c){
		int min = (int)Math.floor(v[1].getProjected().getY());
		int max = (int)Math.ceil(v[3].getProjected().getY());

		// gets the slopes of the sides
		float angXLeft = (v[3].getProjected().getX() - v[1].getProjected().getX()) / (v[3].getProjected().getY() - v[1].getProjected().getY());
		float angXRight = (v[3].getProjected().getX() - v[2].getProjected().getX()) / (v[3].getProjected().getY() - v[2].getProjected().getY());

		float xL = v[1].getProjected().getX();
		float xR = v[2].getProjected().getX();


		// iterates through each y value in the polygon
		for (int y = min; y < max; ++y){
			if (y >= 0 && y < height){
				int xMin = (int) Math.max(0, xL);
				int xMax = (int) Math.min(width-1, xR);

				// Z Buffer
				float yfrac = ((float)(y - min)) / (max - min);
				float zLeft = v[1].getProjected().getZ() * (1 - yfrac) + v[3].getProjected().getZ() * yfrac;
				float zRight = v[2].getProjected().getZ() * (1 - yfrac) + v[3].getProjected().getZ() * yfrac;

				// iterates over row
				for (int x = xMin; x <= xMax; ++x){
					float xfrac = ((float)(x - xMin)) / (xMax - xMin);
					float zVal = zLeft*(1-xfrac) + zRight*xfrac;

					// if value is closer to the camera than what is in the zbuffer, add it
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
