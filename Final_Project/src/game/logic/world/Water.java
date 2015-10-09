package game.logic.world;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;

public class Water implements Tile {

	@Override
	public boolean canEnter(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onInteract(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnter(Player p) {
		p.multiplySpeed(0.5);
	}

	@Override
	public void onExit(Player p) {
		p.multiplySpeed(2);
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
		// TODO Auto-generated method stub
		return null;
	}
}
