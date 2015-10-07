package game.logic.items;

import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;

/**
 * Represents a target that the spies have to interact with to win the game. If
 * the target is equipped the spy has to take it to an exit tile.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Target implements Item {

	private final int ID;

	public Target(final int ID) {
		this.ID = ID;
	}

	@Override
	public void setPosition(double x, double z) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.TARGET;
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
