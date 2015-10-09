/**
 *
 */
package game.logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.logic.items.Item;
import game.logic.weapons.LazorPistol;
import game.logic.weapons.Weapon;
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
 * @author Bieleski, Bryers, Gill and Thompson MMXV.
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

	// Player's health
	private double health = 100;

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
	private List<Item> inventory;
	private List<Item> weaponInventory;
	private Weapon currentWeapon;
	private int currentWeaponIndex;
	private int cooldown;

	// Has the player picked up an item?
	private boolean itemPickedUp;

	public Player(String username, double x, double y, double rotation) {
		this.username = username;
		this.x = x;
		this.y = y;
		this.rotation = rotation;

		currentWeapon = new LazorPistol();

		this.inventory = new ArrayList<Item>();
		this.weaponInventory = new ArrayList<Item>();
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

		// Turning Left
		if (turnLeft && !turnRight) {
			isMoving = true;
			rotation += TURN_SPEED;
			model.getOrientation().setY((float) rotation);
		}

		// Turning Right
		if (turnRight && !turnLeft) {
			isMoving = true;
			rotation -= TURN_SPEED;
			model.getOrientation().setY((float) rotation);
		}

		// Moving Forward
		if (moveFoward) {
			isMoving = true;
			accel = (accel < 1) ? accel + 0.1 : 1;
			double newY = y + (MAX_VELOCITY * accel) * Math.cos(rotation);
			double newX = x + (MAX_VELOCITY * accel) * Math.sin(rotation);
			move(newY, newX);
		}

		// Moving Backward
		if (moveBackward) {
			isMoving = true;
			accel = (accel < 1) ? accel + 0.1 : 1;
			double newY = y - (MAX_VELOCITY * accel) * Math.cos(rotation);
			double newX = x - (MAX_VELOCITY * accel) * Math.sin(rotation);
			move(newY, newX);
		}

		// Check if the player is over an item
		if (getRoom() != null && getRoom().validPosition(this, getX(), getY())) {
			Tile tile = getRoom().getTile(this, getX(), getY());
			tile.onEnter(this);
		}

		// Check for shooting:
		if (isShooting) {
			if (cooldown <= 0) {
				useCurrentWeapon();
			}
			isShooting = false;
		}

		// Check for using:
		if (isUsing) {
			// TODO Check if mouse is over an item
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
		currentRoom.tick();
	}

	private void move(double newY, double newX) {
		if (currentRoom != null && currentRoom.validPosition(this, newX, newY)) {
			// System.out.println("old x: " + model.getPosition().getX());
			model.getPosition().setX((float) newX);
			model.getPosition().setZ((float) newY);
			// Apply Enter and Exit tile modifiers
			Tile oldTile = currentRoom.getTile(this, x, y);
			Tile newTile = currentRoom.getTile(this, newX, newY);
			// if(oldTile!=newTile){
			// oldTile.onExit(this);
			// newTile.onEnter(this);
			// }
			// System.out.println("new x: " + model.getPosition().getX());
			x = newX;
			y = newY;
		}
	}

	private void useCurrentWeapon() {
		currentWeapon.fire(rotation, x, y, currentRoom, this);
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

	public void addItem(Item item) {
		inventory.add(item);
		itemPickedUp = true;
	}

	public final Item getLastItem() {
		return inventory.get(inventory.size() - 1);
	}

	public final List<Item> getInventory() {
		return Collections.unmodifiableList(inventory);
	}

	public void setItemPickedUp(boolean status) {
		itemPickedUp = status;
	}

	public final boolean itemPickedUp() {
		return itemPickedUp;
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

	public void multiplySpeed(double val) {
		moveSpeed *= val;
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

	public final boolean isShooting() {
		return this.isShooting;
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

	// public void selectItem(int i) {
	// currentItemIndex = i;
	// // TODO Display change in selected item from inventory
	// }
	//
	// public void dropItem() {
	// if (inventory.get(currentItemIndex) != null) {
	// Item droppedItem = inventory.get(currentItemIndex);
	// inventory.remove(currentItemIndex);
	// // TODO Drop item on floor right below player.
	// }
	// }

	/** RENDER HELPERS **/

	public final R_Player getModel() {
		return model;
	}

	public final void setModel(R_Player model) {
		this.model = model;
	}

	/** HEALTH HELPERS **/

	public void takeDamage(double damage) {
		this.health -= damage;
	}

	public void heal(double heal) {
		this.health = ((health + heal) < 100) ? health + heal : 100;
	}

	public final double getHealth() {
		return this.health;
	}

	public final boolean isAlive() {
		return this.health > 0;
	}

	/** TEAM HELPERS **/

	public final Team getSide() {
		return side;
	}

	public void setSide(Team side) {
		this.side = side;
	}
}
