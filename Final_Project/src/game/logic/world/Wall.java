package game.logic.world;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class Wall implements Tile {

	private R_Model model;

	public Wall(double xPos, double yPos, R_ModelColorData data, int tileNum) {
		model = new R_Model("BasicWall" + tileNum, data,
				new Vec3(xPos, 0, yPos), Vec3.Zero(),
				new Vec3(0.1f, 0.1f, 0.1f));
	}

	@Override
	public boolean canEnter(Player player) {
		return false;
	}

	@Override
	public void onInteract(Player p) {
		return;
	}

	@Override
	public void onEnter(Player p) {
		return;
	}

	@Override
	public void onExit(Player p) {
		// Do Nothing

	}

	@Override
	public boolean canInteract(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public R_AbstractModelData getModelData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R_AbstractModel getModel() {
		return model;
	}
}
