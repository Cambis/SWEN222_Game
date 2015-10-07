package game.logic.weapons;

import game.logic.Level;
import game.logic.Player;
import game.logic.Room;

public class Lazor {

	private final double lazorSpeed = 1000;

	private double x;
	private double y;
	private double direction;
	private Room room;

	public Lazor (double x, double y, double direction, Room room, Player shooter){

		this.x = x;
		this.y = y;
		this.direction = direction;
		this.room = room;

	}


	/**
	 * This method moved the lazor along each tick
	 */
	public void tick (){

		double newY = y + lazorSpeed * Math.cos(Math.toRadians(direction));
		double newX = x + lazorSpeed * Math.sin(Math.toRadians(direction));

		//TODO Checks for collision are to be done up in wherever the players are stored.

	}

	//TODO how to draw lazor?

}
