package game.logic.weapons;

import game.logic.Level;

public abstract class Weapon {
	
	/**
	 * Fires the current weapon at input position at input direction
	 * 
	 * @param rotation - The Rotation of the weapon
	 * @param x - The x-coord of the weapon
	 * @param y - The y-coord of the weapon
	 */
	abstract public void fire(double rotation, double x, double y, Level currentLevel);

	abstract public int getCooldown();

}
