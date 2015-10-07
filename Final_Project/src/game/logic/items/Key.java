package game.logic.items;

import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import game.logic.world.Door;

/**
 * A key opens a specific door. Not every door requires a key.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Key implements Item {

	private final int ID;
	private Door door;

	private double x, z;

	public Key(int ID) {
		this.ID = ID;
	}

	@Override
	public void setPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	public void setDoor(Door door) {
		this.door = door;
	}

	public final Door getDoor() {
		return this.door;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.KEY;
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
