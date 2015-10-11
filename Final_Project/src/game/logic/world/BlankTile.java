package game.logic.world;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import game.logic.Player;
import game.logic.Room;
import game.logic.items.Item;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import renderer.math.Vec3;

public class BlankTile implements Tile {

	private R_Model model;

	public BlankTile() {

	}

	@Override
	public boolean canInteract(Player player) {
		return false;
	}

	@Override
	public void onInteract(Player p) {
		return;
	}

	@Override
	public R_AbstractModelData getModelData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R_AbstractModel getModel() {
		return null;
	}

	@Override
	public boolean canEnter(Player player) {
		return false;
	}

	@Override
	public void onEnter(Player p) {
		//Do nothing
	}

	@Override
	public void onExit(Player p) {
		return;
	}
}
