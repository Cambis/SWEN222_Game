package game.logic.items;

import java.awt.Color;

import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import renderer.math.Vec3;
import game.logic.Player;
import game.logic.world.Door;

/**
 * A key opens a specific door. Not every door requires a key.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Key implements Item {

	private final int ID;

	private double x = 0, z = 0;

	private R_AbstractModel model;
	private R_ModelColorData modelData = new R_ModelColorData("KEY", "res/models/key.obj", new Color(218, 165, 32));

	/**
	 * Creates key with ID, ID specifies which door it can open
	 * @param ID
	 */
	public Key(int ID) {
		this.ID = ID;
		model = new R_Model("KEY: " + ID, modelData, new Vec3(x, 0.1, z), Vec3.Zero(), new Vec3(0.03, 0.03, 0.03));
	}

	/**
	 * Creates key with ID, ID specifies which door it can open
	 * @param ID
	 */
	public Key(int ID, double x, double z) {
		this.ID = ID;
		setPosition(x, z);
		model = new R_Model("KEY: " + ID, modelData, new Vec3(x, 0.1, z), Vec3.Zero(), new Vec3(0.03, 0.03, 0.03));
	}

	@Override
	public void setPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public void interact(Player p){
		return;
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
	public R_AbstractModelData getModelData() {
		return modelData;
	}

	@Override
	public R_AbstractModel getModel() {
		return model;
	}

	@Override
	public boolean canPickUp(Player p) {
		if(p.getSide()==Team.SPY){
			return true;
		}
		return false;
	}
}
