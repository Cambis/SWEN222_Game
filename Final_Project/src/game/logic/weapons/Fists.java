package game.logic.weapons;

import game.logic.Level;
import game.logic.items.Item;

public class Fists extends Weapon implements Item {
	
	private int cooldownTime = 20;
	
	public Fists (){
		
	}

	@Override
	public void fire(double rotation, double x, double y, Level currentLevel) {
		//TODO Punching implementation
		//TODO Display punch
	}

	@Override
	public int getCooldown() {
		return cooldownTime;
	}

}
