package game.logic.world;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;

public interface Tile {

	/**
	 * Items that the player could be interacting with.
	 *
	 * @author Cameron Bryers 300326848 MMXV
	 *
	 */
	public enum Interaction {

		NONE(-1), DOOR(0), CHEST(1);

		private int value;

		private Interaction(int value) {
			this.value = value;
		}

		public final int getValue() {
			return value;
		}
	}

	public boolean canInteract(Player player);

	public void onInteract(Player p);

	public boolean canEnter(Player player);

	public void onEnter(Player p);

	public void onExit(Player p);

	public boolean blockLight();

	public R_AbstractModelData getModelData();

	public R_AbstractModel getModel();

	public int getID();
}
