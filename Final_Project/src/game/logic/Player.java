/**
 *
 */
package game.logic;

import game.logic.items.Chest;
import game.logic.items.Item;
import game.logic.items.Terminal;
import game.logic.weapons.LazorPistol;
import game.logic.weapons.Weapon;
import game.logic.world.BasicFloor;
import game.logic.world.Door;
import game.logic.world.Tile;
import game.logic.world.Tile.Interaction;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import renderer.R_Player;
import renderer.R_Player.Team;
import renderer.math.Vec3;

/**
 * Represents a player in the game. This class should do the player logic and
 * not have anything to do with the client/ server.
 *
 * @author Cameron Bryers 300326848 MMXV
 * @author Callum Gill MMXV
 * @author Andrew Bieleski MMXV
 *
 */
public class Player {

	// Username of the player, note each player should have a unique username.
	private final String username;

	private Team side;

	public static final double TURN_SPEED = 0.07;
	private double MAX_VELOCITY = 0.1;
	private double score = 0;

	private double moveSpeed = 0.05;
	private double accel = 0.1;

	// Player model that is drawn in the Renderer
	private R_Player model;

	// Player's health
	private double health = 100;

	// Position
	private double x, y, z;
	public final double BOUNDING_BOX_X = 0.05;
	public final double BOUNDING_BOX_Y = 0.05;
	private double rotation;

	// Current room that the player is in
	private Room currentRoom = null;

	// Previous room that the player was in
	private Room previousRoom = null;

	private boolean isMoving, turnLeft, turnRight, moveFoward, moveBackward,
			sprint;

	// Andrew's Stuff
	private boolean isShooting;
	private boolean isUsing;
	private boolean canMove = true;
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

	// True IFF player is interacting with something
	private boolean isInteracting;

	// Types of interaction
	private Interaction interaction = Interaction.NONE;

	// True IFF the player's room is loaded
	private boolean roomLoaded;

	public Player(String username, double x, double y, double rotation) {
		this.username = username;
		this.x = x;
		this.y = y;
		this.z = 0;
		this.rotation = rotation;

		currentWeapon = new LazorPistol();

		this.inventory = new ArrayList<Item>();
		this.weaponInventory = new ArrayList<Item>();

	}

	/**
	 * Reset the speed of player
	 */
	public void resetSpeed() {
		// System.out.println("Resetting speed");
		accel = 0;
	}

	/**
	 * Give player some points (Note: entering negative values decreases score)
	 *
	 * @param val
	 */
	public void addPoints(int val) {
		score += val;
	}

	/**
	 * Returns the score the player is on
	 *
	 * @return
	 */
	public double getPoints() {
		return score;
	}

	/**
	 * Tick method called every tick, should move player, shoot if able and
	 * update timers.
	 */
	public void tick() {
		// Check if room has been loaded if not don't tick
		if (!roomLoaded) {
			return;
		}

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

		// Interacting
		if (getRoom() != null && getRoom().validPosition(this, getX(), getY())
				&& isInteracting) {
			Tile tile = getRoom().getTile(this, getX(), getY());

			if (tile instanceof Door) {
				interaction = Interaction.DOOR;
			}

			if (tile instanceof BasicFloor) {
				BasicFloor floor = (BasicFloor) tile;

				Item item = null;

				if (!floor.getItems().isEmpty())
					item = floor.getItems().peek();

				if (item instanceof Chest)
					interaction = Interaction.CHEST;

				if (item instanceof Terminal)
					interaction = Interaction.TERMINAL;
			}
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
		// if (currentRoom != null) {
		// if (currentRoom.validPosition(this, x, y)) {
		// Tile currentTile = currentRoom.getTile(this, x, y);
		// if (currentTile instanceof Door && !onDoor) {
		// currentRoom = ((Door) currentTile).getTargetRoom();
		//
		// x = ((Door) currentTile).getX();
		// y = ((Door) currentTile).getY();
		// rotation = ((Door) currentTile).getDirection();
		// previousDoor = currentRoom.getTile(this, x, y);
		// onDoor = true;
		// }
		// }
		// // Check to see if the player has moved of the door. If they have,
		// // they can reenter the door again.
		// if (currentRoom.getTile(this, x, y) != previousDoor) {
		// onDoor = false;
		// }
		// }
		// currentRoom.tick();
	}

	/**
	 * Attempts to move the player to position x, y.
	 *
	 * @param y
	 * @param x
	 */
	private void move(double y, double x) {
		if (currentRoom == null) {
			return;
		}
		Tile oldTile = currentRoom.getTile(this, this.x, this.y);
		double newX = this.x;
		double newY = this.y;
		if (currentRoom.validPosition(this, x, y)) {
			newX = x;
			newY = y;
		} else if (currentRoom.validPosition(this, this.x, y)) {
			newY = y;
		} else if (currentRoom.validPosition(this, x, this.y)) {
			newX = x;
		}

		model.getPosition().setX((float) newX);
		model.getPosition().setZ((float) newY);
		// Apply Enter and Exit tile modifiers
		Tile newTile = currentRoom.getTile(this, newX, newY);
		this.x = newX;
		this.y = newY;
		if (oldTile != newTile) {
			oldTile.onExit(this);
			newTile.onEnter(this);
		}
	}

	/**
	 * Use weapon
	 */
	private void useCurrentWeapon() {
		currentWeapon.fire(rotation, x, y, currentRoom, this);
	}

	/**
	 * Check if a player is near another player.
	 *
	 * @param x
	 *            - other player x
	 * @param y
	 *            - other player y
	 * @return true if contact zone contains other player
	 */
	public boolean inRange(double x, double y) {
		Vec3 one = new Vec3(x - this.x, 0, y - this.y);
		Vec3 two = new Vec3(Math.sin(rotation), 0, Math.cos(rotation));

//		if (one.dot(two) > 0.82)
//			System.out.println("Dot true");
//
//		if (inRange(x, y, 2))
//			System.out.println("Range true");

		if (one.dot(two) > 0.2 && inRange(x, y, 2))
			return true;

		return false;
	}

	/**
	 * Check if a position is near a player.
	 *
	 * @param x
	 *            - x coordinate
	 * @param y
	 *            - y coordinate
	 * @param radius
	 *            - radius of contact zone
	 * @return true if contact zone contains position
	 */
	public boolean inRange(double x, double y, double radius) {
		Ellipse2D contactZone = new Ellipse2D.Double(this.x / 2, this.y / 2,
				radius, radius);
		return contactZone.contains(x, y);
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
	 * Add item to players inventory
	 *
	 * @param item
	 */
	public void addItem(Item item) {
		inventory.add(item);
		itemPickedUp = true;
	}

	/**
	 * Gets the last item in player inventory
	 *
	 * @return
	 */
	public final Item getLastItem() {
		return inventory.get(inventory.size() - 1);
	}

	/**
	 * Returns an unmodifiable inventory of player
	 *
	 * @return
	 */
	public final List<Item> getInventory() {
		return Collections.unmodifiableList(inventory);
	}

	/**
	 * Sets itemPickedUp
	 *
	 * @param status
	 */
	public void setItemPickedUp(boolean status) {
		itemPickedUp = status;
	}

	/**
	 * Returns itemPickedUp
	 *
	 * @return
	 */
	public final boolean itemPickedUp() {
		return itemPickedUp;
	}

	/**
	 * Sets interacting
	 *
	 * @param isInteracting
	 */
	public void setInteracting(boolean isInteracting) {
		this.isInteracting = isInteracting;
	}

	/**
	 * Returns if player is interacting
	 *
	 * @return
	 */
	public final boolean isInteracting() {
		return this.isInteracting;
	}

	/**
	 * Resets players interaction to be NONE
	 */
	public void resetInteraction() {
		this.interaction = Interaction.NONE;
	}

	/**
	 * Gets type of interaction
	 *
	 * @return
	 */
	public final Interaction getInteraction() {
		return this.interaction;
	}

	/**
	 * Sets players current room
	 */
	public final void setRoom(Room r) {
		previousRoom = currentRoom;
		currentRoom = r;
	}

	/**
	 * Sets players current room
	 */
	public final Room getRoom() {
		return currentRoom;
	}

	/**
	 * Returns previous room
	 *
	 * @return
	 */
	public final Room getOldRoom() {
		return previousRoom;
	}

	/**
	 * Set if room loaded
	 *
	 * @param roomLoaded
	 */
	public void setRoomLoaded(boolean roomLoaded) {
		this.roomLoaded = roomLoaded;
	}

	/**
	 * Checks if room loaded
	 *
	 * @return
	 */
	public final boolean isRoomLoaded() {
		return this.roomLoaded;
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
	 * Sets z-position
	 *
	 * @param z
	 */
	public final void setZ(double z) {
		if (model != null)
			model.getPosition().setY((float) z);
		this.z = z;
	}

	/**
	 * Returns z-position
	 *
	 * @return
	 */
	public final double getZ() {
		return this.z;
	}

	/**
	 * Sets rotation of player
	 *
	 * @param rot
	 *            - Value to set players rotation to in degrees
	 */
	public void setRot(double rot) {
		if (model != null)
			model.getOrientation().setY((float) rot);
		this.rotation = rot;
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

	/**
	 * Set player moveing foward
	 */
	public void setFoward(boolean val) {
		moveFoward = val;
	}

	/**
	 * Multiply speed of player by value
	 *
	 * @param val
	 */
	public void multiplySpeed(double val) {
		MAX_VELOCITY *= val;
	}

	/**
	 * Set going backwards
	 *
	 * @param val
	 */
	public void setBackward(boolean val) {
		moveBackward = val;
	}

	/**
	 * Set isMoving
	 *
	 * @return
	 */
	public final boolean isMoving() {
		return isMoving;
	}

	/**
	 * Sets if player is moving ???
	 *
	 * @param isMoving
	 */
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
	// //TODO Graphics for sp.setX(targetX);
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

		// We don't want the player to be super dead
		if (health <= 0)
			return;

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
