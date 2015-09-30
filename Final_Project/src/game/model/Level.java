package game.model;

import game.control.packets.Packet02Move;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.*;

import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_OrthoCamera;
import renderer.R_Player;
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
	}

	public Level(StealthGame game, String filename) {
		this.game = game;
		loadRooms(filename);
	}

	public void loadRooms(String filename) {

		rooms.clear();

		try {
			Scanner s = new Scanner(new File(filename));
			int roomNum = 0;
			while (s.hasNext()) {
				String roomFile = s.nextLine();
				rooms.add(new Room(roomFile));
			}
		} catch (IOException e) {
			System.out.println("Error loading file - IOException : "
					+ e.getMessage());
		}

		// TODO add models and camera to renderer here
		// game.r_addCamera(camera);
		// game.r_addModel(model);
		// game.r_addModelData(modelData);

		/**
		 * // Adds a new orthographic camera R_OrthoCamera ortho = new
		 * R_OrthoCamera("MainCamera", Vec3.One(), Vec3.Zero(), Vec3.UnitY(), 1,
		 * 1000, 2); game.r_addCamera(ortho); game.r_setCamera(ortho.getName());
		 *
		 * // Adds a new model R_ModelColorData modelData = new
		 * R_ModelColorData("Test", "res/Guard", Color.RED);
		 * game.r_addModelData(modelData);
		 *
		 * R_Model model = new R_Model("TestModel", modelData, Vec3.Zero(),
		 * Vec3.Zero(), Vec3.One()); game.r_addModel(model);
		 **/

	}

	private void setupRender() {

		// Set up the orthographic camera
		R_OrthoCamera cam = new R_OrthoCamera("main", new Vec3(-25, 10, 25),
				Vec3.Zero(), Vec3.UnitY(), 1, 1000, 2);
		game.r_addCamera(cam);
		game.r_setCamera(cam.getName());

		// Create a player Model
		R_ModelColorData playModData = new R_ModelColorData("P1",
				"res/Guard.obj", new Color(0.3f, 1f, 0.5f));
		game.r_addModelData(playModData);
		playerMod = new R_Player("P1", playModData, R_Player.Team.GUARD,
				Vec3.Zero(), new Vec3(0, 110, 0), new Vec3(0.15f, 0.15f, 0.15f));
		game.r_addModel(playerMod);

		// Create a static Model
		R_ModelColorData sceneModData = new R_ModelColorData("T",
				"res/testLvL.obj", new Color(1f, 0.6f, 0.5f));
		game.r_addModelData(sceneModData);
		R_Model m2 = new R_Model("Scene", sceneModData, new Vec3(1.5f, 0, 0f),
				Vec3.Zero(), new Vec3(1.f, 1.f, 1.f));
		game.r_addModel(m2);
	}

	private void testRender() {

		// Set Orientation
		Vec3 rot = playerMod.getOrientation();
		rot = new Vec3(rot.getX(), val, rot.getZ());
		playerMod.setOrientation(rot);

		// Set Position
		Vec3 pos = playerMod.getPosition();
		pos = new Vec3(pos.getX(), (float) (Math.sin(val)), pos.getZ());
		playerMod.setPosition(pos);

		val = (val < 100.0f) ? val + 0.1f : 0.0f;
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
		// TODO set rooms properly
		p.setRoom(rooms.get(0));
	}

	public boolean removePlayer(Player p) {
		return players.remove(p);
	}

	public void movePlayer(String username, double x, double z, double rot) {
		Player p = getPlayer(username);
		p.setX(x);
		p.setY(z);
	}

	private Player getPlayer(String username) {
		for (Player p : players)
			if (p.getUsername().equals(username))
				return p;

		return null;
	}

	public void tick() {

		for (Player p : players) {
			p.tick();
			Packet02Move packet = new Packet02Move(p.getUsername(), p.getX(),
					p.getY(), 0, true, p.getRotation());
			packet.writeData(game.getClient());
			setupRender();
		}

		if (players.size() > 0)
			testRender();
	}
}
