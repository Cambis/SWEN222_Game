package game.model;

import renderer.R_Model;


public interface Tile {

	public R_Model getModel();

	public boolean canEnter(Player player);

}
