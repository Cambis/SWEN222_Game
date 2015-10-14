package renderer.tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.Test;

import renderer.Light;
import renderer.R_Player.Team;
import renderer.geometry.Polygon;
import renderer.geometry.Vertex;
import renderer.math.Mat4;
import renderer.math.Vec3;

public class Geometry_Test {

	/***
	 * 	Polygon tests
	 */

	// Tests that back faces do not show
	@Test
	public void testPolygon_CullingBackFace(){
		// Create vertices
		Vertex v1 = new Vertex(new Vec3(0, 0, 0));
		Vertex v2 = new Vertex(new Vec3(3, 3, 0));
		Vertex v3 = new Vertex(new Vec3(0, 3, 0));
		v1.generateWP(Mat4.createIdentity(), Mat4.createIdentity());
		v2.generateWP(Mat4.createIdentity(), Mat4.createIdentity());
		v3.generateWP(Mat4.createIdentity(), Mat4.createIdentity());

		// Add to list
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);

		// Create Buffers
		int[] viewport = {0, 0, 0,
						  0, 0, 0,
						  0, 0, 0};
		float[][] zBuffer = {{Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE},
							 {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE},
							 {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE}};

		// Create lighting
		int[][] shadowMap = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
		ArrayList<Light> lights = new ArrayList<Light>();

		// Create and draw polygon
		Polygon p = new Polygon(0, 1, 2);
		p.draw(viewport, zBuffer, 3, 3, Color.RED, vertices, lights, Team.GUARD, Team.GUARD, shadowMap, 1);

		for (int i = 0; i < viewport.length; ++i){
			assertTrue(viewport[i] == 0);
		}
	}

	// Tests that front faces do show
	@Test
	public void testPolygon_CullingFrontFace(){
		float light = Math.max(0, Vec3.UnitY().dot(new Vec3(-0.3, -0.6, -0.9))) * 0.1f;
		Vec3 amb = new Vec3(0.25f, 0.25f, 0.25f);
		int expectCol = new Color((int)(Math.max(0, Math.min(255, 255 * (amb.getX() + light)))),0, 0).getRGB();
		float[] expected = {0, 0, 0, expectCol, expectCol, 0, expectCol, expectCol, expectCol};

		// Create vertices
		Vertex v1 = new Vertex(new Vec3(0, 0, 0));
		Vertex v2 = new Vertex(new Vec3(0, 3, 0));
		Vertex v3 = new Vertex(new Vec3(3, 3, 0));
		v1.generateWP(Mat4.createIdentity(), Mat4.createIdentity());
		v2.generateWP(Mat4.createIdentity(), Mat4.createIdentity());
		v3.generateWP(Mat4.createIdentity(), Mat4.createIdentity());

		// Add to list
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);

		// Create Buffers
		int[] viewport = {0, 0, 0,
						  0, 0, 0,
						  0, 0, 0};
		float[][] zBuffer = {{Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE},
							 {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE},
							 {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE}};

		// Create lighting
		int[][] shadowMap = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
		ArrayList<Light> lights = new ArrayList<Light>();

		// Create and draw polygon
		Polygon p = new Polygon(0, 1, 2);
		p.draw(viewport, zBuffer, 3, 3, Color.RED, vertices, lights, Team.GUARD, Team.GUARD, shadowMap, 1);

		for (int i = 0; i < viewport.length; ++i){
			assertTrue(viewport[i] == expected[i]);
		}
	}

	// Tests that spotlights don't light up polygons outside of the cutoff
	@Test
	public void testPolygon_NotInSpotlight(){
		float light = Math.max(0, Vec3.UnitY().dot(new Vec3(-0.3, -0.6, -0.9))) * 0.1f;
		Vec3 amb = new Vec3(0.25f, 0.25f, 0.25f);
		int expectCol = new Color((int)(Math.max(0, Math.min(255, 255 * (amb.getX() + light)))),0, 0).getRGB();
		float[] expected = {0, 0, 0, 0, expectCol, expectCol, 0, 0, 0};

		// Create vertices
		Vertex v1 = new Vertex(new Vec3(2, 1, 0));
		Vertex v2 = new Vertex(new Vec3(0, 0, 0));
		Vertex v3 = new Vertex(new Vec3(2, 2, 0));
		v1.generateWP(Mat4.createIdentity(), Mat4.createIdentity());
		v2.generateWP(Mat4.createIdentity(), Mat4.createIdentity());
		v3.generateWP(Mat4.createIdentity(), Mat4.createIdentity());

		// Add to list
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);

		// Create Buffers
		int[] viewport = {0, 0, 0,
						  0, 0, 0,
						  0, 0, 0};
		float[][] zBuffer = {{Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE},
							 {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE},
							 {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE}};

		// Create lighting
		int[][] shadowMap = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
		ArrayList<Light> lights = new ArrayList<Light>();
		lights.add(new Light(Vec3.UnitX(), Vec3.UnitX(), Team.GUARD));

		// Create and draw polygon
		Polygon p = new Polygon(0, 1, 2);
		p.draw(viewport, zBuffer, 3, 3, Color.RED, vertices, lights, Team.GUARD, Team.GUARD, shadowMap, 1);

		for (int i = 0; i < viewport.length; ++i){
			assertTrue(viewport[i] == expected[i]);
		}
	}

	// Tests that spotlights don't light up polygons inside shadows
	@Test
	public void testPolygon_InShadow(){
		Vec3 normal = new Vec3(2, 1, 1).sub(new Vec3(2, 2, 2)).cross(new Vec3(2, 2, 2).sub(Vec3.Zero()));
		float light = Math.max(0, normal.dot(new Vec3(-0.3, -0.6, -0.9))) * 0.1f;

		Vec3 amb = new Vec3(0.25f, 0.25f, 0.25f);
		int expectCol = new Color((int)(Math.max(0, Math.min(255, 255 * (amb.getX() + light)))),0, 0).getRGB();
		float[] expected = {0, 0, 0, 0, expectCol, expectCol, 0, 0, 0};

		// Create vertices
		Vertex v1 = new Vertex(new Vec3(0, 0, 0));
		Vertex v2 = new Vertex(new Vec3(2, 2, 2));
		Vertex v3 = new Vertex(new Vec3(2, 1, 1));
		v1.generateWP(Mat4.createIdentity(), Mat4.createIdentity());
		v2.generateWP(Mat4.createIdentity(), Mat4.createIdentity());
		v3.generateWP(Mat4.createIdentity(), Mat4.createIdentity());

		// Add to list
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);

		// Create Buffers
		int[] viewport = {0, 0, 0,
						  0, 0, 0,
						  0, 0, 0};
		float[][] zBuffer = {{Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE},
							 {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE},
							 {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE}};

		// Create lighting
		int[][] shadowMap = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
		ArrayList<Light> lights = new ArrayList<Light>();
		//lights.add(new Light(new Vec3(1, 1, -5), Vec3.UnitZ(), Team.GUARD));

		// Create and draw polygon
		Polygon p = new Polygon(0, 1, 2);
		p.draw(viewport, zBuffer, 3, 3, Color.RED, vertices, lights, Team.GUARD, Team.GUARD, shadowMap, 1);

		for (int i = 0; i < viewport.length; ++i){
			assertTrue(viewport[i] == expected[i]);
		}
	}

	/***
	 * 	Vertex tests
	 */
	@Test
	public void testVertex_Constructor_1(){
		Vertex v = new Vertex(Vec3.One());
		assertTrue(v.getLocal().equals(Vec3.One()));
	}

	@Test
	public void testVertex_Constructor_2(){
		Vertex v = new Vertex(1.f, 1.f, 1.f);
		assertTrue(v.getLocal().equals(Vec3.One()));
	}

	@Test
	public void testVertex_world(){
		Mat4 world = Mat4.createTranslate(1, 1, 1);
		Vertex v = new Vertex(Vec3.Zero());
		v.generateWP(world, Mat4.createIdentity());
		assertTrue(v.getWorld().equals(Vec3.One()));
	}

	@Test
	public void testVertex_projected(){
		Mat4 proj = Mat4.createOrtho(-1, 1, 1, -1, -1, 1);
		Vertex v = new Vertex(Vec3.One());
		v.generateWP(Mat4.createIdentity(), proj);
		assertTrue(v.getProjected().equals(proj.mul(Vec3.One())));
	}
}
