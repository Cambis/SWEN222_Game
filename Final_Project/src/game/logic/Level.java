package game.logic;

import game.control.PlayerMP;
import game.control.packets.Packet;
import game.control.packets.Packet02Move;
import game.control.packets.Packet03Engage;
import game.control.packets.Packet04Damage;
import game.control.packets.Packet06Interact;
import game.control.packets.Packet10Pickup;
import game.logic.items.Item;
import game.logic.world.Tile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import renderer.R_Player;
import renderer.R_Player.Team;
import renderer.math.Vec3;
import resource.ResourceLoader;

/**
 * Represents the level that the game is played on.
 *
 * @author Cameron Bryers 3000326848 MMXV
 * @author Callum Gill 300316407 2015
 *
 */
public class Level {

	// Rooms in the level
	private List<Room> rooms = new ArrayList<Room>();

	// Players in the level
	private List<Player> players = new ArrayList<Player>();

	// Spawn points in the level
	private Queue<SpawnPoint> spawns = new ArrayDeque<SpawnPoint>();

	// Reference back to the game
	private StealthGame game;

	// True IFF level is loaded
	protected boolean readyToRender = false;
	float val = 0;

	/**
	 * Default constructor, parsed in a reference to the main game class.
	 *
	 * @param game
	 *            - reference to the main game
	 */
	public Level(StealthGame game) {
		this.game = game;
	}

	/**
	 * Given a configuration file, load the rooms, spawn points and players.
	 *
	 * @param filename
	 *            - name of configuration file
	 */
	public void loadRooms(String filename) {

		rooms.clear();
		// System.out.println("Loading level");
		try {
			Scanner sc;

			if (StealthGame.EXPORT)
				sc = new Scanner(ResourceLoader.load(filename));
			else
				sc = new Scanner(new File(filename));

			// int roomNum = 0;

			// Load rooms
			while (sc.hasNext()) {
				String roomFile = sc.nextLine();
				rooms.add(new Room("res/levels/" + roomFile));
			}

			sc.close();
		} catch (IOException e) {
			System.out.println("Level - Error loading file - IOException : "
					+ e.getMessage());
		}

		// Initilise door destinations
		for (Room r : rooms) {
			r.initilizeDoors(rooms);
			spawns.addAll(r.getSpawns());
		}

		System.out.println("*** Done Loading level ***");
	}

	/**
	 * Gobbles string in scanner, returns if string found or not and moves
	 * scanner along if it does
	 *
	 * @param pat
	 * @param s
	 * @return
	 */
	public static boolean gobble(String pat, Scanner s) {
		if (s.hasNext(pat)) {
			s.next();
			return true;
		}
		return false;
	}

	/**
	 * Add a player to the level.
	 *
	 * @param p
	 *            - player to be added
	 */
	public void addPlayer(Player p) {

		players.add(p);

		// TODO set rooms properly
		// System.out.println("Player given room");
		p.setRoom(rooms.get(0));

	}

	/**
	 * Remove a player from level.
	 *
	 * @param p
	 *            - player to be removed
	 * @return true if player succesfully removed
	 */
	public boolean removePlayer(Player p) {
		return players.remove(p);
	}

	/**
	 * Remove a player from the level given a username.
	 *
	 * @param name
	 *            - username of the player
	 * @return true if player is removed
	 */
	public boolean removePlayer(String name) {
		for (Player p : players) {
			if (p.getUsername().equals(name)) {
				return players.remove(p);
			}
		}
		return false;
	}

	/**
	 * Move a player in the world given a 3D vector.
	 *
	 * @param username
	 *            - username of player to be moved
	 * @param x
	 *            - player's x coordinate (Tile x, Renderer x)
	 * @param y
	 *            - player's y coordinate (Tile y, Renderer z)
	 * @param z
	 *            - player's z coordinate (Tile z, Renderer y)
	 * @param rot
	 *            - rotation around (z axis -> Tile, y axis -> Renderer)
	 */
	public void movePlayer(String username, double x, double y, double z,
			double rot) {
		Player p = getPlayer(username);

		// Check if the player is moving
		if (p.getX() != x || p.getY() != y || p.getRotation() != rot)
			p.setMoving(true);
		else
			p.setMoving(false);

		p.setX(x);
		p.setY(y);
		p.setZ(z);
		p.setRot(rot);
	}

	/**
	 * Move a player in the level given their ID. Could not make this work over
	 * the server, Do not use.
	 *
	 * @param username
	 *            - player to be moved
	 * @param x
	 *            - player's x coordinate
	 * @param z
	 *            - player's z coordinate
	 * @param rot
	 *            - player's rotation around the y axis
	 */
	@Deprecated
	public void movePlayer(int id, double x, double y, double z, double rot) {
		movePlayer(getPlayer(id).getUsername(), x, y, z, rot);
	}

	/**
	 * Handles an interact packet sent from the server. Interactions are then
	 * performed by the appropriate player on the appropriate tile.
	 *
	 * @param username
	 *            - username of the player interacting
	 * @param ID
	 *            - ID of the tile to interact with
	 */
	public void handleInteract(String username, int ID) {

		Player player = getPlayer(username);

		// We do not want to handle an interact from the player on this computer
		if (player.equals(game.getPlayer()))
			return;

		Room currentRoom = player.getRoom();
		Tile tile = currentRoom.getTile(player, ID);

		if (tile != null) {
			// System.out.println(tile.getClass().getName());
			tile.onInteract(player);
		}

		for (Player p : players) {
			// System.out.println(p.getUsername() + " is in: "
			// + p.getRoom().getName());
		}
	}

	/**
	 * Handles a pickup packet from the server. The level then gives the
	 * appropriate player the item parsed in the packet.
	 *
	 * @param username
	 *            - username of the player picking up the item
	 * @param tileID
	 *            - ID of the tile that the item was on
	 * @param itemID
	 *            - ID of the item to be picked up
	 */
	public void handlePickUp(String username, int tileID, int itemID) {

		Player player = getPlayer(username);

		// We do not want to handle a pickup from the player on this computer
		if (player.equals(game.getPlayer()))
			return;

		Room currentRoom = player.getRoom();
		Tile tile = currentRoom.getTile(player, tileID);

		if (tile != null)
			tile.onEnter(player);
		else
			System.err.println("Tile is NULL");
	}

	/**
	 * Handles a damage packet from the server. The method then gives the
	 * appropriate amount of damage to the appropriate player.
	 *
	 * @param username
	 *            - username of player affected
	 * @param damage
	 *            - amount of damage to deal
	 */
	public void handleDamage(String username, double damage) {
		Player player = getPlayer(username);
		if (player.isAlive())
			player.takeDamage(damage);
		else if (!player.isAlive())
			game.getRenderer().deleteModel(username);
	}

	/**
	 * Get a player given their username.
	 *
	 * @param username
	 *            - username of the player
	 * @return null if no Player with the username exists
	 */
	private Player getPlayer(String username) {
		for (Player p : players)
			if (p.getUsername().equals(username))
				return p;

		return null;
	}

	/**
	 * Get a player given their ID.
	 *
	 * @param ID
	 *            - ID of the player
	 * @return null if no Player with the username exists
	 */
	private Player getPlayer(int ID) {
		for (Player p : players)
			if (((PlayerMP) p).getID() == ID)
				return p;

		return null;
	}

	/**
	 * Go through each player and check what action they are doing. Also, go
	 * through each tile and update it.
	 */
	public void tick() {

		// System.out.println(game.getPlayer().getUsername() + "'s game");

		// for (Player p : players)
		// System.out.println(p.getUsername() + "is in: " +
		// p.getRoom().getName());
		// Go through players
		for (Player p : players) {
			if (!p.isAlive()) {
				game.r_removeModel(p.getUsername());
			}

			// Only render the player if they are alive
			if (readyToRender && p.isAlive()) {

				// If the player is in the same room as the player on this
				// computer, add them to the renderer
				if (p.getRoom().equals(game.getPlayer().getRoom())) {
					if (game.r_addModel(p.getModel())) {
						// System.out.println("Adding " + p.getUsername()
						//		+ "'s model in "
						//		+ game.getPlayer().getUsername() + "'s game");
					}
				}

				// If the player is not in the same room as the
				// player on this computer, remove them from the
				// renderer
				else if (!p.getRoom().equals(game.getPlayer().getRoom())) {
					game.r_removeModel(p.getUsername());
				}

				// Update the room
				if (!p.isRoomLoaded() && p.equals(game.getPlayer())) {

					if (p.getOldRoom() != null)
						p.getOldRoom().removeTiles(game.getRenderer());

					p.getRoom().initTiles(game.getRenderer());
					p.setRoomLoaded(true);
				}

				// Update player
				p.tick();

				// Packet to be sent to the server
				Packet packet = null;

				// Player shooting
				if (p.isShooting()) {
					packet = new Packet03Engage(p.getUsername());
					// packet.writeData(game.getClient());
				}

				// Player interacting
				// Check if player is interacting with a tile
				if (p.isInteracting() && p.getRoom() != null
						&& p.equals(game.getPlayer())
						&& p.getRoom().validPosition(p, p.getX(), p.getY())) {
					Tile tile = p.getRoom().getTile(p, p.getX(), p.getY());
					// isInteracting = false;
					// tile.onInteract(p);

					// Find the type of interaction
					switch (p.getInteraction()) {
					case CHEST:
					case DOOR:
					case TERMINAL:
						packet = new Packet06Interact(p.getUsername(),
								tile.getID());
						break;

					case NONE:
					default:
						break;

					}

					// Interact with the tile
					tile.onInteract(p);

					// Reset the interaction back to NONE
					p.resetInteraction();

					p.setInteracting(false);

					if (packet != null)
						packet.writeData(game.getClient());
				}

				// Check health
				if (!p.getUsername().equals(game.getPlayer().getUsername())) {
					Player pl = getPlayer(game.getPlayer().getUsername());

					if (pl.getRoom().equals(p.getRoom())
							&& pl.getSide() == Team.GUARD
							&& p.getSide() == Team.SPY
							&& pl.inRange(p.getX(), p.getY())) {
						// getPlayer(game.getPlayer().getUsername())
						// .takeDamage(0.1);
						packet = new Packet04Damage(p.getUsername(),
								p.getUsername(), 0.5);
						packet.writeData(game.getClient());
					}
				}

				// Player moving
				if (p.isMoving()) {
					packet = new Packet02Move(p.getUsername(),
							((PlayerMP) p).getID(), p.getX(), p.getY(),
							p.getZ(), true, p.getRotation());
					packet.writeData(game.getClient());
				}

				// Player picking up item
				if (p.itemPickedUp()) {
					Tile tile = p.getRoom().getTile(p, p.getX(), p.getY());
					Item last = p.getLastItem();
					p.setItemPickedUp(false);
					packet = new Packet10Pickup(p.getUsername(), tile.getID(),
							last.getID());
					packet.writeData(game.getClient());
				}

				// Finally tick through the room that the player is in
				p.getRoom().tick(game.getRenderer(), game.getPlayer());
			}
		}
	}

	/**
	 * Set the teams and assign the players models according to their teams.
	 *
	 * @param players
	 *            - array of player usernames.
	 * @param teams
	 *            - team allocating, "0" for guard and "1" for spy.
	 */
	public void setTeams(String[] players, String[] teams) {

		// System.out.println("Players: " + players.length + ", Teams: "
		// + teams.length);
		//
		// for (String s : teams)
		// System.out.print(s + ", ");
		// System.out.println("===================");
		//
		// System.out.println(this.players.size());
		//
		// for (String s : players)
		// System.out.print(s + ", ");
		// System.out.println("===================");

		for (int i = 0; i < players.length; i++) {

			// Assign the team
			Player p = getPlayer(players[i]);

			if (p == null)
				System.err.println("Player " + players[i] + " is null");

			p.setSide((teams[i].equals("0") ? Team.GUARD : Team.SPY));

			// The string will reference a pre-loaded model in the renderer
			String model = (teams[i].equals("0") ? "Guard" : "Spy");

			// Translation and rotations
			SpawnPoint spawn = getNextSpawn(p.getSide());
			p.setRoom(spawn.room);
			p.setX(spawn.x);
			p.setY(spawn.y);
			Vec3 trans = new Vec3(p.getX(), 0, p.getY());
			Vec3 rot = new Vec3(0, -p.getRotation(), 0);
			Vec3 scale = new Vec3(0.1, 0.1, 0.1);

			// Player model

			// Sets up the renderer for drawing the teams correctly
			R_Player.Team rteam = R_Player.Team.SPY;
			if (p.getSide() == Team.GUARD) {
				rteam = R_Player.Team.GUARD;
			}
			R_Player pl = new R_Player(p.getUsername(),
					game.getR_ModelData(model), rteam, trans, rot, scale);

			// Assign the model to the player and the renderer
			p.setModel(pl);
			// game.r_addModel(pl);
		}

		// for (Player p : this.players) {
		// if (p.getRoom().equals(game.getPlayer().getRoom()))
		// game.r_addModel(p.getModel());
		// }
		// for (Player p : this.players) {
		// System.out.println(p.getUsername() + " is a "
		// + p.getSide().toString());
		// }

		// Now that the models have been loaded we can finally render the level
		readyToRender = true;
	}

	/**
	 * Gets next spawn in queue
	 *
	 * @param team
	 * @return
	 */
	private SpawnPoint getNextSpawn(Team team) {
		for (int i = 0; i < spawns.size(); i++) {
			SpawnPoint spawn = spawns.poll();
			if (spawn.team == team) {
				spawns.offer(spawn);
				return spawn;
			}
			spawns.offer(spawn);
		}
		return null;
	}
}
