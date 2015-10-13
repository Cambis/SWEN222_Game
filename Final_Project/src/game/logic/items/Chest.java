package game.logic.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import renderer.math.Vec3;

public class Chest implements Item {

	private List<Item> inventory = new ArrayList<Item>();
	private double x = 0, z = 0;
	private boolean opened = false;
	private R_AbstractModel openedModel;
	private R_AbstractModel closedModel;
	private R_ModelColorData openedModelData = new R_ModelColorData("ChestLocked", "res/models/ChestOpen.obj", Color.ORANGE.darker());
	private R_ModelColorData closedModelData = new R_ModelColorData("ChestLocked", "res/models/ChestClosed.obj", Color.ORANGE.darker());;

	public Chest(List<Item> inventory, double x, double y){
		this.inventory.addAll(inventory);
		setPosition(x,y);
	}

	public Chest(Item item, double x, double y){
		this.inventory.add(item);
		setPosition(x,y);
		openedModel = new R_Model("Chest"+x+y, openedModelData,
				new Vec3(x, 0, y), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
		closedModel = new R_Model("Chest"+x+y, closedModelData,
				new Vec3(x, 0, y), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
	}

	@Override
	public void setPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public void interact(Player p) {
		if(p.getSide()==Team.GUARD){
			return;
		}
		for(Item i : inventory){
			p.addItem(i);
			System.out.println("You got an item out of the chest.");
		}
		inventory.clear();
		opened = true;

	}

	@Override
	public R_AbstractModelData getModelData() {
		if(opened){
			return openedModelData;
		}
		return closedModelData;
	}

	@Override
	public R_AbstractModel getModel() {
		if(opened){
			return openedModel;
		}
		return closedModel;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canPickUp(Player p) {
		return false;
	}

}
