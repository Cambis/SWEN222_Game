package renderer;

import renderer.math.Vec3;

/**
 * This class represents a player model. Player models emit either a spotlight or a point light depending
 * on team the player is on.
 *
 * @author Stephen Thompson
 */
public class R_Player extends R_AbstractModel{

	// The Teams
	public enum Team{
		GUARD, SPY
	}

	// The side the player is on
	private final Team side;

	public R_Player(String name, R_AbstractModelData model, Team team, Vec3 position, Vec3 orientation, Vec3 scale) {
		super(name, model, position, orientation, scale);
		side = team;
	}

	/**
	 * @return		The side that the player is on
	 */
	public Team getSide() {
		return side;
	}
}
