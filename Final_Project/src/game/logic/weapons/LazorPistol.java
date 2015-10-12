package game.logic.weapons;

import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import game.logic.Level;
import game.logic.Player;
import game.logic.Room;
import game.logic.items.Item;

public class LazorPistol extends Weapon implements Item {

	private int cooldownTime = 100;

	public LazorPistol (){

	}

	@Override
	public void fire(double rotation, double x, double y, Room room, Player shooter) {
		//TODO How to create a new lazor? Where is the new lazor stored?

//		room.createLazer(x, y, rotation, shooter);


	}

	@Override
	public void interact(Player p){
		return;
	}

	@Override
	public int getCooldown() {
		return cooldownTime;
	}

	@Override
	public void setPosition(double x, double z) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
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

	@Override
	public boolean canPickUp(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
