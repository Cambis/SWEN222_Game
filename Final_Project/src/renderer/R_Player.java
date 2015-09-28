package renderer;

import renderer.math.Vec3;

public class R_Player extends R_AbstractModel{

	public enum Team{
		GUARD, SPY
	}

	private final Team side;

	public R_Player(String name, String modelFilePath, Team team, Vec3 position, Vec3 orientation) {
		super(name, modelFilePath, position, orientation);
		side = team;
	}

	public Team getSide() {
		return side;
	}
}
