package game.logic.world;

import game.logic.Player;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class BasicFloor implements Tile{

	private R_Model model;

	public BasicFloor(float xPos, float yPos, R_ModelColorData data, int tileNum){
		model = new R_Model("BasicFloor"+ tileNum, data, new Vec3(xPos, 0, yPos), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
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
