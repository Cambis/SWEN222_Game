package game.logic.world;

import java.awt.Color;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class Wall implements Tile {

	private R_Model model;

	private double x, y;

	private int tileNum;

	public Wall(double xPos, double yPos, int tileNum) {
		this.x = xPos;
		this.y = yPos;
		this.tileNum = tileNum;
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
		return new R_ModelColorData("BasicWall", "res/models/BasicWall.obj",
				Color.WHITE);
	}

	@Override
	public R_AbstractModel getModel() {
		return new R_Model("BasicWall" + tileNum, getModelData(), new Vec3(x,
				0, y), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
	}

	@Override
	public final int getID() {
		return this.tileNum;
	}

	@Override
	public boolean blockLight() {
		// TODO Auto-generated method stub
		return true;
	}
}
