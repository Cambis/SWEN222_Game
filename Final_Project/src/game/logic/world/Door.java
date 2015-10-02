package game.logic.world;

import game.logic.Player;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class Door implements Tile{

	private R_Model model;

	public Door(){

	}

	@Override
	public boolean canEnter(Player player) {
		return true;
	}

	@Override
	public R_Model getModel() {
		return model;
	}

}
