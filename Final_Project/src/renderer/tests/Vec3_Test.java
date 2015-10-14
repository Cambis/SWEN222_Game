package renderer.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import renderer.math.Vec3;

public class Vec3_Test {

	/***
	 * 	add tests
	 */
	@Test
	public void testAdd1(){
		Vec3 end = new Vec3(2, 2, 2);
		assertTrue(Vec3.One().add(Vec3.One()).equals(end));
	}

	/***
	 * 	subtract tests
	 */
	@Test
	public void testSub1(){
		assertTrue(Vec3.One().sub(Vec3.One()).equals(Vec3.Zero()));
	}

	/***
	 * 	multiply tests
	 */
	@Test
	public void testMul1(){
		assertTrue(Vec3.One().mul(Vec3.One()).equals(Vec3.One()));
	}

	/***
	 * 	divide tests
	 */
	@Test
	public void testDiv1(){
		assertTrue(Vec3.One().div(Vec3.One()).equals(Vec3.One()));
	}

	/***
	 * 	Dot product tests
	 */
	@Test
	public void testDot_One(){
		assertTrue(1 == Vec3.One().dot(Vec3.One()));
	}

	@Test
	public void testDot_MinusOne(){
		assertTrue(-1 == Vec3.One().dot(Vec3.Zero().sub(Vec3.One())));
	}

	/***
	 * 	Cross product tests
	 */
	@Test
	public void testCross(){
		assertTrue(Vec3.UnitX().cross(Vec3.UnitY()).equals(Vec3.UnitZ()));
	}

	/***
	 * 	Constructor tests
	 */
	@Test
	public void testConstructor_1(){
		assertTrue(new Vec3(Vec3.One()).equals(Vec3.One()));
	}

	@Test
	public void testConstructor_2(){
		assertTrue(new Vec3(1.0, 1.0, 1.0).equals(Vec3.One()));
	}

	/***
	 * 	Getter tests
	 */
	@Test
	public void testGetters(){
		Vec3 one = Vec3.One();
		assertTrue(1 == one.getX());
		assertTrue(1 == one.getY());
		assertTrue(1 == one.getZ());
	}

	/***
	 * 	Setter tests
	 */
	@Test
	public void testSetters(){
		Vec3 one = Vec3.One();
		one.setX(0.f);
		one.setY(0.5f);
		one.setZ(0.7f);
		assertTrue(one.equals(new Vec3(0, 0.5, 0.7)));
	}

	/***
	 * 	vector length tests
	 */
	@Test
	public void testLength(){
		assertTrue(Vec3.UnitY().mag() == 1);
	}

	/***
	 * 	Normalize tests
	 */
	@Test
	public void testNormalize(){
		assertTrue(new Vec3(2, 0, 0).normalize().mag() == 1);
	}

	/***
	 * 	Lerp tests
	 */
	@Test
	public void testLerp_1(){
		assertTrue(Vec3.Lerp(Vec3.One(), Vec3.Zero(), 0).equals(Vec3.One()));
	}

	@Test
	public void testLerp_2(){
		assertTrue(Vec3.Lerp(Vec3.One(), Vec3.Zero(), 1).equals(Vec3.Zero()));
	}

	@Test
	public void testLerp_3(){
		assertTrue(Vec3.Lerp(Vec3.One(), Vec3.Zero(), 0.5f).equals(new Vec3(0.5, 0.5, 0.5)));
	}
}
