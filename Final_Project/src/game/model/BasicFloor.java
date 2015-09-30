package game.model;

import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class BasicFloor implements Tile{

	private R_Model model;

	public BasicFloor(int xPos, int yPos, R_ModelColorData data){
		model = new R_Model("BasicFloor", data, new Vec3(xPos, yPos, 0), Vec3.Zero(), Vec3.One());
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
