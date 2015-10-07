package game.logic.world;

import game.logic.Player;
import renderer.R_Model;

public class Water implements Tile {

	@Override
	public R_Model getModel() {
		// TODO Auto-generated method stub
		return null;
	}

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



}
