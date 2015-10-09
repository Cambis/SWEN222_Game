package game.logic.items;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Player.Team;

/**
 * Items are objects in the world that a player can interact with.
 *
 * @author Cameron Bryers 300326848 MMXV
 * @author Andrew Bieleski
 *
 */
public interface Item {

	/**
	 * Different types of items found in the game.
	 *
	 * @author Cameron Bryers 300326848 MMXV
	 *
	 */
	public enum ItemType {

		TARGET('T'), KEY('K');

		private char value;

		private ItemType(char value) {
			this.value = value;
		}

		public final char getValue() {
			return this.value;
		}
	}

	/**
	 * Set the starting position of the item in the level (if it has one)
	 *
	 * @param x
	 *            - x coordinate
	 * @param z
	 *            - z coordinate
	 */
	public void setPosition(double x, double z);

	public int getID();

	/**
	 * Get the item type that the item corresponds to.
	 *
	 * @return the item's ItemType
	 * @see ItemType
	 */
	public ItemType getItemType();

	/**
	 * Get the model data for the renderer.
	 *
	 * @return instance of R_AbstractModelData
	 */
	public R_AbstractModelData getModelData();

	/**
	 * Get the model to be rendered for the renderer.
	 *
	 * @return instance of R_AbstractModel
	 */
	public R_AbstractModel getModel();

	/**
	 * Check if a player can pick up a certain item.
	 *
	 * @param item
	 *            - item to be picked up
	 * @param player
	 *            - player attempting to pick item up
	 * @return
	 */
	public static boolean canPickUp(Item item, Player player) {

		Team team = player.getSide();

		switch (item.getItemType()) {

		// Items that spies can pick up
		case KEY:
		case TARGET:
			if (team.equals(Team.SPY))
				return true;
			break;

		default:
			break;

		}
		return false;
	}
}
