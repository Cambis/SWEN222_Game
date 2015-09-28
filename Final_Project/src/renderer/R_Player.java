package renderer;

import renderer.abstractClasses.R_AbstractModel;
import renderer.math.Vec3;

public class R_Player extends R_AbstractModel{

	public enum Team{
		GUARD, SPY
	}

	private final Team side;

	public R_Player(String name, Team team, Vec3 position, Vec3 orientation) {
		super(name, position, orientation);
		side = team;
	}

	public Team getSide() {
		return side;
	}
}
