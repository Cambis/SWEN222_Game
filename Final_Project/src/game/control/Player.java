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

	private final String username;

	public Player(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
