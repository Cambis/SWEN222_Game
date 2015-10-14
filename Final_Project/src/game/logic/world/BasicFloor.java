package game.logic.world;

import game.logic.Player;
import game.logic.items.Item;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import renderer.R_AbstractModel;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

/**
 *
 * @author Callum Gill 300316407 2015
 *
 */
public class BasicFloor implements Tile {

	private R_Model model;

	// Items on the tile
	private Stack<Item> inventory;

	// Items to be removed from the renderer
	private Queue<Item> inventoryTaken;
	private Queue<Item> itemsToUpdate;

	private int tileNum;

	/**
	 *  Creates a basic floor on which both teams can enter and items
	 *  can be dropped onto
	 * @param xPos xposition to create tile
	 * @param yPos yposition to create tile
	 * @param data model data of tile
	 * @param tileNum tile number
	 */
	public BasicFloor(double xPos, double yPos, R_ModelColorData data,
			int tileNum) {
		inventory = new Stack<Item>();
		inventoryTaken = new PriorityQueue<Item>();
		itemsToUpdate = new PriorityQueue<Item>();
		model = new R_Model("BasicFloor" + tileNum, data, new Vec3(xPos, 0,
				yPos), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
		this.tileNum = tileNum;
	}


	@Override
	public void onInteract(Player p) {
		for (Item i : inventory) {
			i.interact(p);
			itemsToUpdate.offer(i);
		}
	}

	@Override
	public R_AbstractModel getModel() {
		return model;
	}

	@Override
	public boolean canEnter(Player player) {
		return true;
	}

	@Override
	public void onEnter(Player p) {
		if (!inventory.isEmpty() && inventory.peek().canPickUp(p)) {
			Item item = inventory.pop();
			p.addItem(item);
			inventoryTaken.offer(item);
		}
	}

	@Override
	public void onExit(Player p) {
		//Do Nothing
	}

	/**
	 * Add item to floorspace
	 * @param item to add
	 * @return if item added
	 */
	public boolean addItem(Item item) {
		return inventory.add(item);
	}

	/**
	 * get stack of items from floorspace
	 * @return items on floor space
	 */
	public final Stack<Item> getItems() {
		return inventory;
	}

	/**
	 * Items to remove from floor (already removed from usable items)
	 * @return
	 */
	public final Queue<Item> getItemsToRemove() {
		return inventoryTaken;
	}

	/**
	 * Items that need their model tobe updated
	 * @return items to be updated
	 */
	public final Queue<Item> getItemsToUpdate() {
		return itemsToUpdate;
	}

	@Override
	public int getID() {
		return this.tileNum;
	}

	@Override
	public boolean blockLight() {
		return false;
	}
}
