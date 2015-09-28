package renderer;

import java.awt.Color;

import renderer.math.Vec3;

public class R_Player extends R_AbstractModel{

	public enum Team{
		GUARD, SPY
	}

	private final Team side;

	public R_Player(String name, String modelFilePath, Team team, Vec3 position, Vec3 orientation, Vec3 scale, Color col) {
		super(name, modelFilePath, position, orientation, scale, col);
		side = team;
	}

	public Team getSide() {
		return side;
	}
}
