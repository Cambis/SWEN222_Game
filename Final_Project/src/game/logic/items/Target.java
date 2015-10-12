package game.logic.items;

import game.logic.Player;

import java.awt.Color;

import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

/**
 * Represents a target that the spies have to interact with to win the game. If
 * the target is equipped the spy has to take it to an exit tile.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Target implements Item {

	private final int ID;
	private double x, z;

	public Target(final int ID) {
		this.ID = ID;
		this.x = 0;
		this.z = 0;
	}

	@Override
	public void setPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void interact(Player p){
		return;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.TARGET;
	}

	@Override
	public R_AbstractModelData getModelData() {
		return new R_ModelColorData("TARGET", "res/models/monkey.obj", Color.green);
	}

	@Override
	public R_AbstractModel getModel() {
		return new R_Model("TARGET: " + ID, getModelData(), new Vec3(x, 0, z), Vec3.Zero(), new Vec3(0.05, 0.05, 0.05));
	}
}
