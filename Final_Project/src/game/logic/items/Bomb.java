package game.logic.items;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;

public class Bomb implements Item {

	@Override
	public void setPosition(double x, double z) {

	}

	@Override
	public void interact(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public R_AbstractModelData getModelData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R_AbstractModel getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canPickUp(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
