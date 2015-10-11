package renderer;

import renderer.math.Vec3;

/**
 * This class represents a player's spotlight
 *
 * @author Stephen Thompson
 *
 */
public class Light {
	// the light's position
	private Vec3 position;

	// the light's direction
	private Vec3 direction;

	// the team this light belongs to
	private R_Player.Team side;

	// the cutoff angle of the spotlight
	private float cutoff;

	/**
	 * The constructor for a light object
	 *
	 * @param position	the light's position
	 * @param direction the light's direction
	 * @param side		the light's team
	 */
	public Light(Vec3 position, Vec3 direction, R_Player.Team side) {
		super();
		float xx = (float)Math.sin(direction.getY());
		float zz = (float)Math.cos(direction.getY());

		this.position = new Vec3(position.getX()+xx*0.1f, 0.2f, position.getZ()+zz*0.1f);
		this.direction = new Vec3(xx, -0.5f, zz);
		this.side = side;
		this.cutoff = 0.8f;
	}

	/**
	 * Gets the light's position
	 * @return 	the light's position
	 */
	public Vec3 getPosition() {
		return position;
	}

	/**
	 * Sets the light's position
	 * @param position - the new position
	 */
	public void setPosition(Vec3 position) {
		this.position = position;
	}

	/**
	 * Gets the light's direction
	 * @return	the light's direction
	 */
	public Vec3 getDirection() {
		return direction;
	}

	/**
	 * Sets the light's direction
	 * @param direction	- the new direction
	 */
	public void setDirection(Vec3 direction) {
		this.direction = direction;
	}

	/**
	 * Gets the light's team
	 * @return 	the light's team
	 */
	public R_Player.Team getSide() {
		return side;
	}

	/**
	 * Sets the team of the player
	 *
	 * @param side	the new team side
	 */
	public void setSide(R_Player.Team side) {
		this.side = side;
	}

	/**
	 * Gets the cutoff angle of the spotlight
	 * @return the cutoff angle
	 */
	public float getCutoff() {
		return cutoff;
	}
}
