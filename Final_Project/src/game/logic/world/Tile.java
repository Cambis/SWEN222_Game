package game.logic.world;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;

public interface Tile {

	/**
	 * Items that the player could be interacting with.
	 *
	 * @author Callum Gill 300316407 2015
	 *
	 */
	public enum Interaction {

		NONE(-1), DOOR(0), CHEST(1), TERMINAL(2);

		private int value;

		private Interaction(int value) {
			this.value = value;
		}

		public final int getValue() {
			return value;
		}
	}

	/**
	 * Defines happens when player is on tile and interacts with tile
	 * @param p player interacting with the tile
	 */
	public void onInteract(Player p);

	/**
	 * Returns if player can enter tile
	 * @param player
	 * @return boolean if player can enter tile or not
	 */
	public boolean canEnter(Player player);

	/**
	 * Defines what happens to the player on enter
	 * @param p player
	 */
	public void onEnter(Player p);

	/**
	 * Defines what happens to the player on exit
	 * (Often used to remove buffs/debuffs of tile)
	 * @param p
	 */
	public void onExit(Player p);

	/**
	 * Returns if tile should block light
	 * @return
	 */
	public boolean blockLight();

	/**
	 * returns tile's model
	 * @return
	 */
	public R_AbstractModel getModel();

	/**
	 * Returns tile's ID
	 * @return
	 */
	public int getID();
}
