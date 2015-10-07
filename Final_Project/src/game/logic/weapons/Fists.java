package game.logic.weapons;

import game.logic.Level;
import game.logic.Player;
import game.logic.Room;
import game.logic.items.Item;

public class Fists extends Weapon implements Item {

	private int cooldownTime = 20;

	public Fists (){

	}

	@Override
	public void fire(double rotation, double x, double y, Room room, Player shooter) {
		//TODO Punching implementation
		//TODO Display punch
	}

	@Override
	public int getCooldown() {
		return cooldownTime;
	}

}
