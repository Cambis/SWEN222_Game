package game.logic.world;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import game.logic.Player;
import game.logic.items.Item;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class BasicFloor implements Tile {

	private R_Model model;

	// Items on the tile
	private Stack<Item> inventory;

	// Items to be removed from the renderer
	private Queue<Item> inventoryTaken;

	public BasicFloor(double xPos, double yPos, R_ModelColorData data, int tileNum) {
		inventory = new Stack<Item>();
		inventoryTaken = new PriorityQueue<Item>();
		model = new R_Model("BasicFloor"+ tileNum, data, new Vec3(xPos, 0, yPos), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
	}

	@Override
	public boolean canEnter(Player player) {
		return true;
	}

	@Override
	public R_Model getModel() {
		return model;
	}

	@Override
	public void onInteract(Player p) {
		if(!inventory.isEmpty()){
			Item item = inventory.pop();
			p.addItem(item);
			inventoryTaken.offer(item);
		}
	}

	@Override
	public void onEnter(Player p) {
		//Do Nothing
	}

	@Override
	public void onExit(Player p) {
		// Do Nothing

	}

	public boolean addItem(Item item) {
		return inventory.add(item);
	}

	public final Stack<Item> getItems() {
		return inventory;
	}

	public final Queue<Item> getItemsToRemove() {
		return inventoryTaken;
	}

}
