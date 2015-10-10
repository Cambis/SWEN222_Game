package game.logic;

import game.control.PlayerMP;
import game.control.packets.Packet;
import game.control.packets.Packet02Move;
import game.control.packets.Packet03Engage;
import game.control.packets.Packet06Interact;
import game.control.packets.Packet07Equip;
import game.logic.items.Item;
import game.logic.world.Door;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.*;

import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_OrthoCamera;
import renderer.R_Player;
import renderer.R_Player.Team;
import renderer.math.Vec3;

/**
 * Represents the level that the game is played on.
 *
 * @author Bryers and Gill MMXV.
 *
 */
public class Level {

	private List<Room> rooms = new ArrayList<Room>();
	private List<Player> players = new ArrayList<Player>();
	private StealthGame game;

	protected boolean readyToRender = false;

	// Testing
	private R_Player playerMod;
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
		System.out.println("Loading level");
		try {
			Scanner s = new Scanner(new File(filename));
			int roomNum = 0;
			while (s.hasNext()) {
				String roomFile = s.nextLine();
				rooms.add(new Room("res/levels/" + roomFile));
			}
		} catch (IOException e) {
			System.out.println("Level - Error loading file - IOException : "
					+ e.getMessage());
		}
		//Initilise door destinations
		for(Room r : rooms){
			r.initilizeDoors(rooms);
		}
		System.out.println("Done Loading level");
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
		System.out.println("Player given room");
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
	public void movePlayer(String username, double x, double z, double rot) {
		Player p = getPlayer(username);

		// Check if the player is moving
		if (p.getX() != x || p.getY() != z || p.getRotation() != rot)
			p.setMoving(true);
		else
			p.setMoving(false);

		p.setX(x);
		p.setY(z);
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
	public void movePlayer(int id, double x, double z, double rot) {
		movePlayer(getPlayer(id).getUsername(), x, z, rot);
	}

	public void handleInteract(String username, int ID) {

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

		// Go through players
		for (Player p : players) {

			// Only render the player if they are alive
			if (readyToRender && p.isAlive()) {

				// Update player
				p.tick();

				// Packet to be sent to the server
				Packet packet;

				// Player moving
				if (p.isMoving()) {
					packet = new Packet02Move(p.getUsername(),
							((PlayerMP) p).getID(), p.getX(), p.getY(), 0,
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
					Item last = p.getLastItem();
					p.setItemPickedUp(false);
					packet = new Packet06Interact(p.getUsername(), last.getID());
					// packet.writeData(game.getClient());
				}
			}
		}

		// Go through rooms
		for (Room r : rooms) {
			r.tick(game.getRenderer());
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

		for (int i = 0; i < players.length; i++) {

			// Assign the team
			Player p = getPlayer(players[i]);
			p.setSide((teams[i].equals("0") ? Team.GUARD : Team.SPY));

			// The string will reference a pre-loaded model in the renderer
			String model = (teams[i].equals("0") ? "Guard" : "Spy");

			// Translation and rotations
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
			game.r_addModel(pl);
		}

		// for (Player p : this.players) {
		// System.out.println(p.getUsername() + " is a "
		// + p.getSide().toString());
		// }

		// Now that the models have been loaded we can finally render the level
		readyToRender = true;
	}
}
