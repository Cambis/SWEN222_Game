/**
 *
 */
package game.control;


/**
 * Represents a player in the game. This class should do the player logic and
 * not have anything to do with the client/ server.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Player {

	public enum Direction {

		NORTH(1), EAST(2), SOUTH(3), WEST(4);

		private int value;

		private Direction(int value) {
			this.value = value;
		}

		public final int getValue() {
			return value;
		}

	}
	
	private final String username;
	
	private double x, y;
	private Direction direction;
	private boolean isMoving;

	public Player(String username, double x, double y, Direction direction) {
		this.username = username;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public String getUsername() {
		return username;
	}

	public final double getX() {
		return x;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final double getY() {
		return y;
	}

	public final void setY(double y) {
		this.y = y;
	}

	public final Direction getDirection() {
		return direction;
	}

	public final void setDirection(Direction direction) {
		this.direction = direction;
	}

	public final boolean isMoving() {
		return isMoving;
	}

	public final void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}


}
