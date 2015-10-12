package game.logic.items;

import java.util.ArrayList;
import java.util.List;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;

public class Chest implements Item {

	private List<Item> inventory = new ArrayList<Item>();

	public Chest(List<Item> inventory, double x, double y){
		this.inventory.addAll(inventory);
		setPosition(x,y);
	}

	@Override
	public void setPosition(double x, double z) {
		// TODO Auto-generated method stub

	}

	@Override
	public void interact(Player p) {
		for(Item i : inventory){
			p.addItem(i);
		}

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemType getItemType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R_AbstractModelData getModelData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R_AbstractModel getModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
