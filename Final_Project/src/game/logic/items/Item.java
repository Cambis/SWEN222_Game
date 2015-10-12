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
	 * Set the starting position of the item in the level (if it has one)
	 *
	 * @param x
	 *            - x coordinate
	 * @param z
	 *            - z coordinate
	 */
	public void setPosition(double x, double z);


	/**
	 * Defines the behavior with the player when the player interacts wit item
	 */
	public void interact(Player p);

	public int getID();

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
	 * Check if a player can pick up item.
	 *
	 * @param player
	 *            - player attempting to pick item up
	 * @return
	 */
	public boolean canPickUp(Player player);

}
