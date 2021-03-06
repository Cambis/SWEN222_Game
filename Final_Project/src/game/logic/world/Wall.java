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

/**
 * @author Callum Gill 300316407 2015
 *
 */
public class Wall implements Tile {

	private R_Model model;

	private double x, y;

	private int tileNum;

	/**
	 * Creates a wall at xpos, ypos with modeldata as the model
	 * @param xPos xposition
	 * @param yPos yposition
	 * @param data model data to create model of wall
	 * @param tileNum tile number
	 */
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
		//Do Nothing
	}

	@Override
	public void onEnter(Player p) {
		//Do Nothing
	}

	@Override
	public void onExit(Player p) {
		// Do Nothing

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
