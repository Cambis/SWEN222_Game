package game.logic.world;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class Vent implements Tile {

	private R_Model model;

	public Vent(int xPos, int yPos, R_ModelColorData data) {
		model = new R_Model("Vent", data, new Vec3(xPos, yPos, 0), Vec3.Zero(),
				Vec3.One());
	}

	@Override
	public boolean canEnter(Player player) { // Note : Whomever made this class
												// to check the player, thanks
												// so much. Makes vents a
												// breeze.
	// if (player instanceof runner){
	// return true;
	// } else {
	// return false;
	// }*/
		// TODO Strategies for guards and runners? Makes it easier for hackers
		// later.
		return true;
	}

	@Override
	public void onInteract(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnter(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit(Player p) {
		// TODO Auto-generated method stub

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

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
