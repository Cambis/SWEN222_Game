package game.logic.weapons;

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

		new Lazor(x, y, rotation, room, shooter);


	}

	@Override
	public int getCooldown() {
		return cooldownTime;
	}

}
