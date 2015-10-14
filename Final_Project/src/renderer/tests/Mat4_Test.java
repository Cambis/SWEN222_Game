package renderer.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import renderer.math.Mat4;
import renderer.math.Vec3;

/**
 * This class partially tests the mat4 class
 * 
 * @author Stephen Thompson
 * ID: 300332215
 */
public class Mat4_Test {

	@Test
	public void testIdentity(){
		assertTrue(Vec3.One().equals(Mat4.createIdentity().mul(Vec3.One())));
	}
	
	@Test
	public void testTranslate(){
		assertTrue(Vec3.One().equals(Mat4.createTranslate(Vec3.One()).mul(Vec3.Zero())));
	}
	
	@Test
	public void testScale(){
		assertTrue(new Vec3(2, 2, 2).equals(Mat4.createScale(2, 2, 2).mul(Vec3.One())));
		assertTrue(new Vec3(2, 2, 2).equals(Mat4.createScale(new Vec3(2, 2, 2)).mul(Vec3.One())));
	}
	
	@Test
	public void testMultiply(){
		Mat4 mult = Mat4.createScale(2, 2, 2);
		mult = mult.mul(Mat4.createTranslate(1, 1, 1));
		assertTrue(new Vec3(2, 2, 2).equals(mult.mul(Vec3.Zero())));
	}
}
