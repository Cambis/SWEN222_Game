package game.logic;

import game.control.PlayerMP;
import game.control.packets.Packet;
import game.control.packets.Packet02Move;
import game.control.packets.Packet03Engage;
import game.control.packets.Packet06Interact;
import game.control.packets.Packet10Pickup;
import game.logic.items.Item;
import game.logic.world.BasicFloor;
import game.logic.world.Door;
import game.logic.world.Tile;
import game.logic.world.Tile.Interaction;

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
 * @author Bryers and Gill MMXV.
 *
 */
public class Level {

	private List<Room> rooms = new ArrayList<Room>();
	private List<Player> players = new ArrayList<Player>();
	private Queue<SpawnPoint> spawns = new ArrayDeque<SpawnPoint>();
	private StealthGame game;

	protected boolean readyToRender = false;
	private boolean levelLoaded = false;

	float val = 0;

	public Level(StealthGame game) {
		this.game = game;
	}

	public Level(StealthGame game, String filename) {
		this.game = game;
		loadRooms(filename);
	}

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

	public void addPlayer(Player p) {

		players.add(p);

		// TODO set rooms properly
		// System.out.println("Player given room");
		p.setRoom(rooms.get(0));

	}

	public boolean removePlayer(Player p) {
		return players.remove(p);
	}

	public boolean removePlayer(String name) {
		for (Player p : players) {
			if (p.getUsername().equals(name)) {
				players.remove(p);
				return true;
			}
		}
		return false;
	}

	/**
	 * Move a player in the level given their username.
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
	public void movePlayer(String username, double x, double y, double z, double rot) {
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
	 * Move a player in the level given their ID.
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
	public void movePlayer(int id, double x, double y, double z, double rot) {
		movePlayer(getPlayer(id).getUsername(), x, y, z, rot);
	}

	public void handleInteract(String username, int ID) {

		Player player = getPlayer(username);

		// We do not want to handle an interact from the player on this computer
		if (player.equals(game.getPlayer()))
			return;

		System.out.println(game.getPlayer().getUsername()
				+ " recieved shit from the: " + username);

		Room currentRoom = player.getRoom();
		Tile tile = currentRoom.getTile(player, ID);

		if (tile != null) {
			System.out.println(tile.getClass().getName());
			tile.onInteract(player);
		}

		for (Player p : players)
			System.out.println(p.getUsername() + " is in: "
					+ p.getRoom().getName());
	}

	public void handlePickUp(String username, int tileID, int itemID) {

		Player player = getPlayer(username);

		System.out.println(game.getPlayer().getUsername()
				+ " recieved pickup shit from the: " + username);

		// We do not want to handle a pickup from the player on this computer
		if (player.equals(game.getPlayer()))
			return;

		Room currentRoom = player.getRoom();
		Tile tile = currentRoom.getTile(player, tileID);
		tile.onEnter(player);
	}

	private Player getPlayer(String username) {
		for (Player p : players)
			if (p.getUsername().equals(username))
				return p;

		return null;
	}

	private Player getPlayer(int id) {
		for (Player p : players)
			if (((PlayerMP) p).getID() == id)
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

			// Only render the player if they are alive
			if (readyToRender && p.isAlive()) {

				// If the player is in the same room as the player on this
				// computer, add them to the renderer
				if (p.getRoom().equals(game.getPlayer().getRoom())) {
					if (game.r_addModel(p.getModel())) {
						System.out.println("Adding " + p.getUsername()
								+ "'s model in "
								+ game.getPlayer().getUsername() + "'s game");
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

				// Player moving
				if (p.isMoving()) {
					packet = new Packet02Move(p.getUsername(),
							((PlayerMP) p).getID(), p.getX(), p.getY(), p.getZ(),
							true, p.getRotation());
					packet.writeData(game.getClient());
				}

				// Player shooting
				if (p.isShooting()) {
					packet = new Packet03Engage(p.getUsername());
					// packet.writeData(game.getClient());
				}

				// Player picking up item
				if (p.itemPickedUp()) {
					Tile tile = p.getRoom().getTile(p, p.getX(), p.getY());
					Item last = p.getLastItem();
					p.setItemPickedUp(false);
					packet = new Packet10Pickup(p.getUsername(), tile.getID(), last.getID());
					packet.writeData(game.getClient());
				}

				// Player interacting
				// Check if player is interacting with a tile
				if (p.isInteracting() && p.getRoom() != null
						&& p.equals(game.getPlayer())
						&& p.getRoom().validPosition(p, p.getX(), p.getY())) {
					Tile tile = p.getRoom().getTile(p, p.getX(), p.getY());
					// isInteracting = false;
					// tile.onInteract(p);

					switch (p.getInteraction()) {
					case DOOR:
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

					System.out.println(p.getRoom()
							.getTile(p, p.getX(), p.getY()).getID());
					p.setInteracting(false);

					if (packet != null)
						packet.writeData(game.getClient());
				}

				// Finally tick through the room that the player is in
				p.getRoom().tick(game.getRenderer());
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

//		for (Player p : this.players) {
//			if (p.getRoom().equals(game.getPlayer().getRoom()))
//				game.r_addModel(p.getModel());
//		}
		// for (Player p : this.players) {
		// System.out.println(p.getUsername() + " is a "
		// + p.getSide().toString());
		// }

		// Now that the models have been loaded we can finally render the level
		readyToRender = true;
	}

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
