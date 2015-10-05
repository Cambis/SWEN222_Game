package game.logic;

import game.control.packets.Packet02Move;
import game.logic.world.Tile;
import game.logic.world.Wall;

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

public class Level {

	private List<Room> rooms = new ArrayList<Room>();
	private List<Player> players = new ArrayList<Player>();
	private StealthGame game;

	// Testing
	private R_Player playerMod;
	float val = 0;

	public Level(StealthGame game) {
		this.game = game;
		setupRender();
	}

	public Level(StealthGame game, String filename) {
		this.game = game;
		loadRooms(filename);
		setupRender();
	}

	public void loadRooms(String filename) {

		rooms.clear();
		System.out.println("Loading level");
		try {
			Scanner s = new Scanner(new File(filename));
			int roomNum = 0;
			while (s.hasNext()) {
				String roomFile = s.nextLine();
				rooms.add(new Room("res/Levels/" + roomFile));

			}
		} catch (IOException e) {
			System.out.println("Level - Error loading file - IOException : "
					+ e.getMessage());
		}
		System.out.println("Done Loading level");
	}

	// R_Model model; // FIXME very very bad
	private void setupRender() {
		// R_OrthoCamera ortho = new R_OrthoCamera("MainCamera", new Vec3(50,
		// 50, 50), Vec3.Zero(), Vec3.UnitY(), 1, 1000, 2);
		// game.r_addCamera(ortho);
		// game.r_setCamera(ortho.getName());
		//
		// // Adds a new model
		// R_ModelColorData modelData = new R_ModelColorData("Test",
		// "res/Guard.obj", Color.RED);
		// game.r_addModelData(modelData);

		// model = new R_Model("Work", (R_ModelColorData)
		// game.getR_ModelData("Test"), Vec3.Zero(), Vec3.Zero(), Vec3.One());
		// game.r_addModel(model);
	}

	/**
	 * Gobbles string in scanner, returns if string found or not and moves
	 * scanner along if it does
	 *
	 * @param pat
	 * @param s
	 * @return
	 */
	private boolean gobble(String pat, Scanner s) {
		if (s.hasNext(pat)) {
			s.next();
			return true;
		}
		return false;
	}

	public void addPlayer(Player p) {
		players.add(p);

		// Assign a model to the player
		R_Player pl = new R_Player(p.getUsername(),
				game.getR_ModelData("Test"), Team.GUARD, Vec3.Zero(),
				Vec3.Zero(), new Vec3(0.1, 0.1, 0.1));
		p.setModel(pl);

		// TODO set rooms properly
		System.out.println("Player given room");
		p.setRoom(rooms.get(0));
		game.r_addModel(pl);

		// IFF the minimum amount of players is reached, run the game.
		// FIXME
		// if (players.size() >= StealthGame.MIN_PLAYERS)
		// game.run();
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
	 * Move a player across the level given a position and a rotation.
	 *
	 * @param username
	 *            - Identifies the player
	 * @param x
	 *            - New x coordinate
	 * @param z
	 *            - New y coordinate
	 * @param rot
	 *            - Amount to rotate by (radians)
	 */
	public void movePlayer(String username, double x, double z, double rot) {
		Player p = getPlayer(username);

		// Check if the player is moving
		if (p.getX() != x || p.getY() != z || p.getRotation() != rot)
			p.setMoving(true);
		else
			p.setMoving(false);

		if (detectCollisions(p, x, z)) {
			p.setX(x);
			p.setY(z);
		}
		p.setRot(rot);
	}

	/**
	 * Detects collisions.
	 *
	 * @param player
	 * @param x
	 * @param y
	 * @return true iff no collisons were detected
	 */
	private boolean detectCollisions(Player player, double x, double y) {

		// FIXME: does not work correctly
		for (Room r : rooms) {
			Tile t = r.getTile(player, x, y);
			if (t instanceof Wall) {
				return false;
			}
		}
		return true;
	}

	private Player getPlayer(String username) {
		for (Player p : players)
			if (p.getUsername().equals(username))
				return p;

		return null;
	}

	public void tick() {
		// Go through each player and check what action they are doing.
		for (Player p : players) {
			p.tick();
			if (p.isMoving()) {
				Packet02Move packet = new Packet02Move(p.getUsername(),
						p.getX(), p.getY(), 0, true, p.getRotation());
				packet.writeData(game.getClient());
			}
		}
		// model.getOrientation().setY(model.getOrientation().getY()+0.01f);
	}

	// public static void main(String[] args){
	// Level lvl = new Level(new StealthGame(false, "TestUser"));
	// lvl.loadRooms("res/Levels/test1.lvl");
	// }
}
