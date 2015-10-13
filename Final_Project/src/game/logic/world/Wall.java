package game.logic.world;

import java.awt.Color;
import java.util.PriorityQueue;
import java.util.Stack;

import game.logic.Player;
import game.logic.items.Item;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class Wall implements Tile {

	private R_Model model;

	private double x, y;

	private int tileNum;

	/*public Wall(double xPos, double yPos, int tileNum) {
		this.x = xPos;
		this.y = yPos;
		this.tileNum = tileNum;
	}*/
	public Wall(double xPos, double yPos, R_ModelColorData data, int tileNum) {
		model = new R_Model("BasicFloor" + tileNum, data, new Vec3(xPos, 0,
				yPos), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
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
	public R_AbstractModelData getModelData() {
		return new R_ModelColorData("BasicWall", "res/models/BasicWall.obj",
				Color.WHITE);
	}

	@Override
	public R_AbstractModel getModel() {
		return model;
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
