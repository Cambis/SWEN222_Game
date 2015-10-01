package game.logic.world;

import game.logic.Player;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class Wall implements Tile{

	private R_Model model;

	public Wall(int xPos, int yPos, R_ModelColorData data){
		model = new R_Model("BasicWall", data, new Vec3(xPos, yPos, 0), Vec3.Zero(), Vec3.One());
	}



	@Override
	public boolean canEnter(Player player) {
		return false;
	}

	@Override
	public R_Model getModel() {
		return model;
	}

}
