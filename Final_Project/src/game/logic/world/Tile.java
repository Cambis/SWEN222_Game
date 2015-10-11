package game.logic.world;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;


public interface Tile {

	public boolean canInteract(Player player);

	public void onInteract(Player p);

	public boolean canEnter(Player player);

	public void onEnter(Player p);

	public void onExit(Player p);

	public R_AbstractModelData getModelData();

	public R_AbstractModel getModel();

	public int getID();
}
