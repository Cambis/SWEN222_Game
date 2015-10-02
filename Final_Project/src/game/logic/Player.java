/**
 *
 */
package game.logic;

import java.awt.Color;
import java.util.ArrayList;

import game.logic.items.Item;
import game.logic.world.Door;
import game.logic.world.Tile;
// import game.logic.weapons.Weapon;
import renderer.*;
import renderer.math.Vec3;

/**
 * Represents a player in the game. This class should do the player logic and
 * not have anything to do with the client/ server.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Player {

	// public enum Direction {
	//
	// NORTH(1), EAST(2), SOUTH(3), WEST(4);
	//
	// private int value;
	//
	// private Direction(int value) {
	// this.value = value;
	// }
	//
	// public final int getValue() {
	// return value;
	// }
	//
	// }

	private final String username;
	public static final double TURN_SPEED = 1;

	private double moveSpeed = 1;
	private R_Player model;

	private double x, y;
	private double rotation;
	private Room currentRoom = null;
	private boolean isMoving, turnLeft, turnRight, moveFoward, sprint;

	//Andrew's Stuff
	private boolean isShooting;
	private boolean isUsing;
	private Tile previousDoor;
	private boolean onDoor = false;

	// private Weapon currentWeapon;
	// private Weapon sideWeapon;
	// private Weapon mainWeapon;
	private boolean isSide;
	private ArrayList <Item> inventory;
	private int currentItemIndex;

	private int cooldown;

	public Player(String username, double x, double y, double rotation) {
		this.username = username;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}

	/**
	 * Gets name of the player
	 *
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets x-position
	 *
	 * @return
	 */
	public final double getX() {
		return x;
	}

	/**
	 * Sets x-position
	 *
	 * @param x
	 */
	public final void setX(double x) {
		if (model != null)
			model.getPosition().setX((float) x);
		this.x = x;
	}

	/**
	 * Sets players current room
	 */
	public final void setRoom(Room r) {
		currentRoom = r;
	}

	/**
	 * Sets players current room
	 */
	public Room getRoom() {
		return currentRoom;
	}

	/**
	 * get y-position
	 *
	 * @return
	 */
	public final double getY() {
		return y;
	}

	/**
	 * set y-position
	 *
	 * @param y
	 */
	public final void setY(double y) {
		if (model != null)
			model.getPosition().setZ((float) y);
		this.y = y;
	}

	/**
	 * Gets rotation/direction of player 0 being up y-axis
	 *
	 * @return
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * turn left at turnspeed
	 */
	public void setTurnLeft(boolean val) {
		turnLeft = val;
	}

	/**
	 * turn right at turnspeed
	 */
	public void setTurnRight(boolean val) {
		turnRight = val;
	}

	public void setFoward(boolean val) {
		moveFoward = val;
	}

	public final boolean isMoving() {
		return isMoving;
	}

	public final void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	//Andrew's bit working on now
	public final void setShooting(boolean isShooting) {
		this.isShooting = isShooting;
	}
	public final void setUsing(boolean isUsing) {
		this.isUsing = isUsing;
	}
	public final void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}


	/**
	 * Tick method called every tick, should move player, shoot if able and
	 * update timers.
	 */
	public void tick() {
		System.out.println("Player ticking");

		//Player Movement:
		if (turnLeft && !turnRight) {
			isMoving = true;
			rotation -= TURN_SPEED;
			model.getOrientation().setY((float) rotation);
		}
		if (turnRight && !turnLeft) {
			isMoving = true;
			rotation += TURN_SPEED;
			model.getOrientation().setY((float) rotation);
		}
		if (moveFoward) {
			isMoving = true;
			double newY = y + moveSpeed * Math.cos(Math.toRadians(rotation));
			double newX = x + moveSpeed * Math.sin(Math.toRadians(rotation));
			if (currentRoom != null
					&& currentRoom.validPosition(this, newX, newY)) {
				model.getPosition().setX((float) newX);
				model.getPosition().setZ((float) newY);
			}
		}

		//Check for shooting:
		if (isShooting){
			if (cooldown == 0){
				// shootCurrentGun();
			}
			isShooting = false;
		}

		//Check for using:
		if (isUsing) {
			//TODO Check if mouse is over an item
			//TODO Check if mouse is in range of player (How to access the mouse from here?)
			// interact();
			isUsing = false;
		}

		//Cooldown for player's gun
		if (cooldown > 0){
			cooldown --;
		}
		if (cooldown < 0){
			cooldown = 0;
		}

		//Check for doors. If in a door, move player to spawn location of door:
		if (currentRoom.validPosition(this, x, y)){
			Tile currentTile = currentRoom.getTile(this, x, y);
			if (currentTile instanceof Door && !onDoor){
				currentRoom = ((Door) currentTile).getTargetRoom();

				x = ((Door) currentTile).getX();
				y = ((Door) currentTile).getY();
				rotation = ((Door) currentTile).getDirection();
				previousDoor = currentRoom.getTile(this, x, y);
				onDoor = true;
			}
		}
		//Check to see if the player has moved of the door. If they have, they can reenter the door again.
		if (currentRoom.getTile(this, x, y) != previousDoor){
			onDoor = false;
		}

		//Draw in new position:
		model.setPosition(new Vec3((float)x, (float)y, (float)0));

	}

//	private final void shootCurrentGun(){
////			currentWeapon.fire(rotation, x, y, currentLevel); //TODO Level pointer?
//			setCooldown(currentWeapon.getCooldown());
//	}
//
//	private final void interact(){
//		//TODO Identify what is being interacted with?
//		//TODO
//	}
//
//	public void swapWeapon() {
//		if (isSide) {
//			if (mainWeapon != null){
//				currentWeapon = mainWeapon;
//				isSide = false;
//			}
//		} else {
//			currentWeapon = sideWeapon;
//			isSide = true;
//		}
//		//TODO Graphics for swapping between main and side?
//	}

	public void selectItem(int i) {
		currentItemIndex = i;
		//TODO Display change in selected item from inventory
	}

	public void dropItem() {
		if (inventory.get(currentItemIndex) != null){
			Item droppedItem = inventory.get(currentItemIndex);
			inventory.remove(currentItemIndex);
			// TODO Drop item on floor right below player.
		}
	}

	public final R_Player getModel() {
		return (model == null) ? null : model;
	}

	public final void setModel(R_Player model) {
		this.model = model;
	}




}
