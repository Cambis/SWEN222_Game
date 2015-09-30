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
		setupRender();
	}

	public Level(StealthGame game, String filename) {
		this.game = game;
		loadRooms(filename);
		setupRender();
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
	}
	R_Model model; // FIXME very very bad
	private void setupRender() {
		R_OrthoCamera ortho = new R_OrthoCamera("MainCamera", new Vec3(50, 50, 50), Vec3.Zero(), Vec3.UnitY(), 1, 1000, 2);
		game.r_addCamera(ortho);
		game.r_setCamera(ortho.getName());

		// Adds a new model
		R_ModelColorData modelData = new R_ModelColorData("Test", "res/Guard.obj", Color.RED);
		game.r_addModelData(modelData);

		model = new R_Model("Work", modelData, Vec3.Zero(), Vec3.Zero(), Vec3.One());
		game.r_addModel(model);
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
		// p.setRoom(rooms.get(0));
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
		// Go through each player and check what action they are doing.
		for (Player p : players) {
			p.tick();
			if (p.isMoving()) {
				Packet02Move packet = new Packet02Move(p.getUsername(),
						p.getX(), p.getY(), 0, true, p.getRotation());
				p.setMoving(false);
				packet.writeData(game.getClient());
			}
		}
		model.getOrientation().setY(model.getOrientation().getY()+0.01f);
	}
}
