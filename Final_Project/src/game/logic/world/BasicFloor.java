package game.logic.world;

import java.util.List;

import game.logic.Player;
import game.logic.items.Item;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class BasicFloor implements Tile{

	private R_Model model;
	private List<Item> inventory;

	public BasicFloor(double xPos, double yPos, R_ModelColorData data, int tileNum){
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
			//TODO Give p item
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

}
