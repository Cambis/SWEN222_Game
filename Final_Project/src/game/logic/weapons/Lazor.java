package game.logic.weapons;

import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;
import game.logic.Level;
import game.logic.Player;
import game.logic.Room;

public class Lazor {

	private final double lazorSpeed = 1000;

	private R_Model model;

	private double x;
	private double y;
	private double direction;

	public Lazor (double x, double y, double direction, Player shooter, R_ModelColorData modelData, int index){

		this.x = x;
		this.y = y;
		this.direction = direction;
		model = new R_Model("lazer"+index, modelData, new Vec3(x, 0.5, y), new Vec3(0, direction, 0), new Vec3(0.2, 0.2, 0.2));
	}


	/**
	 * This method moved the lazor along each tick
	 */
	public void tick (){

		double newY = y + lazorSpeed * Math.cos(Math.toRadians(direction));
		double newX = x + lazorSpeed * Math.sin(Math.toRadians(direction));

		//TODO Checks for collision are to be done up in wherever the players are stored.

	}

	public R_Model getModel(){
		return model;
	}
	//TODO how to draw lazor?

}
