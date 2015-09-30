/**
 *
 */
package game.model;

import renderer.*;


/**
 * Represents a player in the game. This class should do the player logic and
 * not have anything to do with the client/ server.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Player {

//	public enum Direction {
//
//		NORTH(1), EAST(2), SOUTH(3), WEST(4);
//
//		private int value;
//
//		private Direction(int value) {
//			this.value = value;
//		}
//
//		public final int getValue() {
//			return value;
//		}
//
//	}

	private final String username;
	public static final double TURN_SPEED = 1;

	private double moveSpeed = 1;
	private R_Player model;

	private double x, y;
	private double rotation;
	private boolean isMoving, turnLeft, turnRight, moveFoward, sprint;

	public Player(String username, double x, double y, double rotation) {
		this.username = username;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}

	/**
	 * Gets name of the player
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets x-position
	 * @return
	 */
	public final double getX() {
		return x;
	}

	/**
	 * Sets x-position
	 * @param x
	 */
	public final void setX(double x) {
		model.getPosition().setX((float)x);
		this.x = x;
	}

	/**
	 * get y-position
	 * @return
	 */
	public final double getY() {
		return y;
	}

	/**
	 * set y-position
	 * @param y
	 */
	public final void setY(double y) {
		model.getPosition().setZ((float)y);
		this.y = y;
	}

	/**
	 * Gets rotation/direction of player
	 * 0 being up y-axis
	 * @return
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * turn left at turnspeed
	 */
	public void setTurnLeft(boolean val){
		turnLeft = val;
	}

	/**
	 * turn right at turnspeed
	 */
	public void setTurnRight(boolean val){
		turnRight = val;
	}

	public void setFoward(boolean val){
		moveFoward = val;
	}

	public final boolean isMoving() {
		return isMoving;
	}

	public final void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	/**
	 * Tick method called every tick, should move player, shoot if able and update timers.
	 */
	public void tick(){
		if(turnLeft && !turnRight){
			rotation-=TURN_SPEED;
			model.getOrientation().setY((float) rotation);
		}
		if(turnRight && !turnLeft){
			rotation+=TURN_SPEED;
			model.getOrientation().setY((float) rotation);
		}
		if(moveFoward){
			y+=moveSpeed*Math.cos(Math.toRadians(rotation));
			x+=moveSpeed*Math.sin(Math.toRadians(rotation));

			model.getPosition().setX((float)x);
			model.getPosition().setZ((float)y);
		}
	}

}