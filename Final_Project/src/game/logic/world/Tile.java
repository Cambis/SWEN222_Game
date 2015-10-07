package game.logic.world;

import game.logic.Player;
import renderer.R_Model;


public interface Tile {

	public R_Model getModel();

	public boolean canEnter(Player player);

	public void onInteract(Player p);

	public void onEnter(Player p);

	public void onExit(Player p);

}
