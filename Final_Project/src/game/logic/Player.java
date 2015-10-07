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
import renderer.R_Player.Team;
import renderer.math.Vec3;

/**
 * Represents a player in the game. This class should do the player logic and
 * not have anything to do with the client/ server.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Player {

	private final String username;

	private Team side;

	public static final double TURN_SPEED = 0.05;
	public static final double MAX_VELOCITY = 0.1;

	private double moveSpeed = 0.05;
	private double accel = 0.1;

	// Player model that is drawn in the Renderer
	private R_Player model;

	// Position
	private double x, y;
	private double xBoundingBox, yBoundingBox;
	private double rotation;
	private Room currentRoom = null;
	private boolean isMoving, turnLeft, turnRight, moveFoward, moveBackward,
			sprint;

	// Andrew's Stuff
	private boolean isShooting;
	private boolean isUsing;
	private Tile previousDoor;
	private boolean onDoor = false;

	// private Weapon currentWeapon;
	// private Weapon sideWeapon;
	// private Weapon mainWeapon;
	private boolean isSide;
	private ArrayList<Item> inventory;
	private int currentItemIndex;
	private int cooldown;

	public Player(String username, double x, double y, double rotation) {
		this.username = username;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}

	public void resetSpeed() {
		// System.out.println("Resetting speed");
		accel = 0;
	}

	/**
	 * Tick method called every tick, should move player, shoot if able and
	 * update timers.
	 */
	public void tick() {

		// Player Movement:
		if (turnLeft && !turnRight) {
			isMoving = true;
			rotation += TURN_SPEED;
			model.getOrientation().setY((float) rotation);
		}

		if (turnRight && !turnLeft) {
			isMoving = true;
			rotation -= TURN_SPEED;
			model.getOrientation().setY((float) rotation);
		}

		if (moveFoward) {
			isMoving = true;
			accel = (accel < 1) ? accel + 0.1 : 1;
			// System.out.println("ACCEL: " + accel);
			double newY = y + (MAX_VELOCITY * accel) * Math.cos(rotation);
			double newX = x + (MAX_VELOCITY * accel) * Math.sin(rotation);
			if (currentRoom != null
				 && currentRoom.validPosition(this, newX, newY)) {
				// System.out.println("old x: " + model.getPosition().getX());
				model.getPosition().setX((float) newX);
				model.getPosition().setZ((float) newY);
				// System.out.println("new x: " + model.getPosition().getX());
				x = newX;
				y = newY;
			}
		}
		if (moveBackward) {
			isMoving = true;
			accel = (accel < 1) ? accel + 0.1 : 1;
			double newY = y - (MAX_VELOCITY * accel) * Math.cos(rotation);
			double newX = x - (MAX_VELOCITY * accel) * Math.sin(rotation);
			if (currentRoom != null
				 && currentRoom.validPosition(this, newX, newY)) {
				// System.out.println("old x: " + model.getPosition().getX());
				model.getPosition().setX((float) newX);
				model.getPosition().setZ((float) newY);
				// System.out.println("new x: " + model.getPosition().getX());
				x = newX;
				y = newY;
			}
		}

		// Check for shooting:
		if (isShooting) {
			if (cooldown == 0) {
				// shootCurrentGun();
			}
			isShooting = false;
		}

		// Check for using:
		if (isUsing) {
			// TODO Check if mouse is over an item
			// TODO Check if mouse is in range of player (How to access the
			// mouse from here?)
			// interact();
			isUsing = false;
		}

		// Cooldown for player's gun
		if (cooldown > 0) {
			cooldown--;
		}
		if (cooldown < 0) {
			cooldown = 0;
		}

		// Check for doors. If in a door, move player to spawn location of door:
//		if (currentRoom != null) {
//			if (currentRoom.validPosition(this, x, y)) {
//				Tile currentTile = currentRoom.getTile(this, x, y);
//				if (currentTile instanceof Door && !onDoor) {
//					currentRoom = ((Door) currentTile).getTargetRoom();
//
//					x = ((Door) currentTile).getX();
//					y = ((Door) currentTile).getY();
//					rotation = ((Door) currentTile).getDirection();
//					previousDoor = currentRoom.getTile(this, x, y);
//					onDoor = true;
//				}
//			}
//			// Check to see if the player has moved of the door. If they have,
//			// they can reenter the door again.
//			if (currentRoom.getTile(this, x, y) != previousDoor) {
//				onDoor = false;
//			}
//		}

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

	public void setRot(double rot) {
		if (model != null)
			model.getOrientation().setY((float) rot);
		this.rotation = rot;
		// System.out.println(username + " rot: " + rot);
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

	public void setBackward(boolean val) {
		moveBackward = val;
	}

	public final boolean isMoving() {
		return isMoving;
	}

	public final void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
		if (!this.isMoving)
			resetSpeed();
	}

	// Andrew's bit working on now
	public final void setShooting(boolean isShooting) {
		this.isShooting = isShooting;
	}

	public final void setUsing(boolean isUsing) {
		this.isUsing = isUsing;
	}

	public final void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	// private final void shootCurrentGun(){
	// // currentWeapon.fire(rotation, x, y, currentLevel); //TODO Level
	// pointer?
	// setCooldown(currentWeapon.getCooldown());
	// }
	//
	// private final void interact(){
	// //TODO Identify what is being interacted with?
	// //TODO
	// }
	//
	// public void swapWeapon() {
	// if (isSide) {
	// if (mainWeapon != null){
	// currentWeapon = mainWeapon;
	// isSide = false;
	// }
	// } else {
	// currentWeapon = sideWeapon;
	// isSide = true;
	// }
	// //TODO Graphics for swapping between main and side?
	// }

	public void selectItem(int i) {
		currentItemIndex = i;
		// TODO Display change in selected item from inventory
	}

	public void dropItem() {
		if (inventory.get(currentItemIndex) != null) {
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

	public final Team getSide() {
		return side;
	}

	public void setSide(Team side) {
		this.side = side;
	}
}
