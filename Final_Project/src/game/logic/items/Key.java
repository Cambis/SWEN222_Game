package game.logic.items;

import java.awt.Color;

import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;
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

	private double x = 0, z = 0;

	public Key(int ID) {
		this.ID = ID;
	}

	public Key(int ID, double x, double z) {
		this(ID);
		setPosition(x, z);
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
		return this.ID;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof Key){
			return ((Key)o).getID()==this.ID;
		}
		return false;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.KEY;
	}

	@Override
	public R_AbstractModelData getModelData() {
		return new R_ModelColorData("KEY", "res/models/key.obj", new Color(218, 165, 32));
	}

	@Override
	public R_AbstractModel getModel() {
		return new R_Model("KEY: " + ID, getModelData(), new Vec3(x, 0.1, z), Vec3.Zero(), new Vec3(0.03, 0.03, 0.03));
	}
}