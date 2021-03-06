package game.logic;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import game.logic.items.Chest;
import game.logic.items.Item;
import game.logic.items.Key;
import game.logic.items.Terminal;
import game.logic.world.BasicFloor;
import game.logic.world.BlankTile;
import game.logic.world.Door;
import game.logic.world.Tile;
import game.logic.world.Wall;
import game.logic.world.Water;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_OrthoCamera;
import renderer.R_Player.Team;
import renderer.Renderer;
import renderer.Renderer.*;
import renderer.math.Vec3;
import resource.ResourceLoader;

/**
 * @author Callum Gill 300316407 2015
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Room {

	private static final int TILE_SIZE = 10;
	private static final double SCALE = 0.02;

	private Tile[][] tiles;
	private int xSize = 0;
	private int ySize = 0;
	private String name = "This is a unnamed room";

	private List<Player> playersInRoom;

	private Map<Door, String> doorDests = new HashMap<Door, String>();
	private List<Door> doors = new ArrayList<Door>();
	private List<SpawnPoint> spawns = new ArrayList<SpawnPoint>();

	// Shadow map to be parsed to the renderer
	int[][] shadowMap;

	// Models
	private R_ModelColorData wallData = new R_ModelColorData("Wall",
			"res/models/BasicWall.obj", Color.WHITE);
	private R_ModelColorData floorData = new R_ModelColorData("Floor",
			"res/models/BasicFloor.obj", Color.GRAY);
	private R_ModelColorData waterData = new R_ModelColorData("Water",
			"res/models/BasicFloor.obj", Color.CYAN);

	private R_ModelColorData treeData = new R_ModelColorData("Tree",
			"res/models/TreeTile.obj", new Color(100, 175, 90));
	private R_ModelColorData bigTreeData = new R_ModelColorData("BigTree",
			"res/models/BigTreeTile.obj", new Color(0.1f, 0.3f, 0.1f));
	private R_ModelColorData grassData = new R_ModelColorData("Grass",
			"res/models/GrassTile.obj", Color.GREEN);
	private R_ModelColorData pillerData = new R_ModelColorData("Piller",
			"res/models/PillerTile.obj", Color.WHITE);

	public Room(String filename) {
		loadTiles(filename);
		setPlayersInRoom(new ArrayList<Player>());
	}

	/**
	 * Creates empty room with no tiles
	 */
	public Room() {

	}

	/**
	 * Load tiles from room file
	 *
	 * @param filename
	 */
	private void loadTiles(String filename) {
		try {
			Scanner s;

			if (StealthGame.EXPORT)
				s = new Scanner(ResourceLoader.load(filename));
			else
				s = new Scanner(new File(filename));

			if (s.hasNextLine()) {
				name = s.nextLine();
			} else {
				System.out.println("Room has no name");
			}
			xSize = s.nextInt();
			ySize = s.nextInt();
			tiles = new Tile[xSize][ySize];
			int xPos = 0;
			int yPos = 0;
			int tileNum = 0;

			// List of doors to be given destinations

			while (s.hasNext()) {

				if (s.hasNextInt()) {
					parseInt(s, xPos, yPos, tileNum);

					xPos++;
					tileNum++;

					if (xPos >= xSize) {
						xPos = 0;
						yPos++;
					}
				}
				// Not a basic floor, door or wall
				else {
					String str = s.next();
					if (str.length() == 1) {
						parseChar(xPos, yPos, tileNum, str);

						xPos++;
						tileNum++;

						if (xPos >= xSize) {
							xPos = 0;
							yPos++;
						}
					} else {
						if (str.equals("door")) {
							parseDoorDestination(s);
						}
					}
				}
			}
			s.close();
		} catch (IOException e) {
			System.out.println("Room - Error loading file - IOException : "
					+ e.getMessage());
		}
	}

	/**
	 * Parses door information
	 *
	 * @param s
	 *            Scanner
	 */
	private void parseDoorDestination(Scanner s) {
		// Load door
		if (s.hasNextInt()) {
			int doorId = s.nextInt();
			int keyID = -1;
			if (s.hasNextInt()) {
				keyID = s.nextInt();
			}
			if (s.hasNext()) {
				String roomName = s.next();
				int targetX = s.nextInt();
				int targetY = s.nextInt();
				for (Door d : doors) {
					if (d.doorID == doorId) {
						// Give door a room destination in
						// map to be assigned in level
						if (keyID >= 0) {
							d.setLocked(true);
							d.setKey(keyID);
						}
						doorDests.put(d, roomName);
						d.setTargetPos(targetX, targetY);
					}
				}
			}
		}
	}

	/**
	 * Parses Char from room file
	 *
	 * @param xPos
	 *            tile x position
	 * @param yPos
	 *            tile y position
	 * @param tileNum
	 *            tile number
	 * @param str
	 *            string (should be length 1)
	 */
	private void parseChar(int xPos, int yPos, int tileNum, String str) {
		// Find the item
		switch (str) {
		case "-":
			tiles[xPos][yPos] = new BlankTile();
			break;
		case "w":
			tiles[xPos][yPos] = new Water(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, waterData, tileNum);
			break;
		case "t": // small tree
			tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, treeData, tileNum);
			break;

		case "b": // big tree
			tiles[xPos][yPos] = new Wall(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, bigTreeData, tileNum);
			break;
		case "p": // pillar
			tiles[xPos][yPos] = new Wall(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, pillerData, tileNum);
			break;
		case "g": // grass
			tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, grassData, tileNum);
			break;
		case "S":// Spy spawn
			tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, floorData, tileNum);
			spawns.add(new SpawnPoint(this, xPos, yPos, Team.SPY));
			break;
		case "G":// Guard spawn
			tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, floorData, tileNum);
			spawns.add(new SpawnPoint(this, xPos, yPos, Team.GUARD));
			break;
		default:
			// Item on a basic floor (Capital means item in chest)
			char itemKey = str.charAt(0);
			Item item = parseItem(xPos, yPos, itemKey);
			if (item != null) {
				if (itemKey <= 90 && itemKey >= 65) {// Put item
														// in
														// chest
					Chest chest = new Chest(item, xPos * TILE_SIZE * SCALE,
							yPos * TILE_SIZE * SCALE);
					item = chest;
				}
				tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE * SCALE,
						yPos * TILE_SIZE * SCALE, floorData, tileNum);

				// Add the item to the room
				((BasicFloor) tiles[xPos][yPos]).addItem(item);
			}
			break;
		}
	}

	/**
	 * Parses an Int from room file
	 *
	 * @param s
	 *            Scanner
	 * @param xPos
	 *            tile x position
	 * @param yPos
	 *            tile y position
	 * @param tileNum
	 *            tile number
	 */
	private void parseInt(Scanner s, int xPos, int yPos, int tileNum) {
		int i = s.nextInt();

		if (i == 0) {
			tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, floorData, tileNum);
		} else if (i == 1) {
			tiles[xPos][yPos] = new Wall(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, wallData, tileNum);
		} else if (i > 1) {
			tiles[xPos][yPos] = new Door(xPos * TILE_SIZE * SCALE, yPos
					* TILE_SIZE * SCALE, tileNum, i);
			doors.add((Door) tiles[xPos][yPos]);
		}
	}

	/**
	 * Initilize door destinations
	 *
	 * @param rooms
	 *            list of rooms created
	 */
	public void initilizeDoors(List<Room> rooms) {
		for (Door d : doors) {
			String destName = doorDests.get(d);
			System.out.println("Destination: " + destName);
			for (Room r : rooms) {
				if (r.getName().equals(destName)) {
					d.setTargetRoom(r);
				}
			}
		}
	}

	/**
	 * Returns spawn points in room
	 *
	 * @return
	 */
	public List<SpawnPoint> getSpawns() {
		return spawns;
	}

	/**
	 * Parses an item given a itemKey
	 *
	 * @param xPos
	 *            x-position (GRID co-ordinate)
	 * @param yPos
	 *            y-position (GRID)
	 * @param itemKey
	 *            items key from room file
	 * @return
	 */
	private Item parseItem(int xPos, int yPos, char itemKey) {
		char key = itemKey;
		if (Character.isAlphabetic(itemKey)) {
			key = Character.toLowerCase(itemKey);
		}
		Item item = null;
		if (key == 'a' || key == 'c' || key == 'd') {
			item = new Key(key - 97, xPos * TILE_SIZE * SCALE, yPos * TILE_SIZE
					* SCALE);
		} else if (key == '*') {
			item = new Terminal(xPos * TILE_SIZE * SCALE, yPos * TILE_SIZE
					* SCALE);
		}
		return item;
	}

	/**
	 * returns if player can be in position in room
	 *
	 * @param p
	 *            player
	 * @param x
	 *            player's x position (world co-odinate)
	 * @param y
	 *            player's y position (world co-odinate)
	 */
	public boolean validPosition(Player p, double x, double y) {

		Tile tile = getTile(p, x + p.BOUNDING_BOX_X, y);
		if (tile == null || !tile.canEnter(p)) {
			return false;
		}

		tile = getTile(p, x, y + p.BOUNDING_BOX_Y);
		if (tile == null || !tile.canEnter(p)) {
			return false;
		}

		tile = getTile(p, x - p.BOUNDING_BOX_X, y);
		if (tile == null || !tile.canEnter(p)) {
			return false;
		}

		tile = getTile(p, x, y - p.BOUNDING_BOX_Y);
		if (tile == null || !tile.canEnter(p)) {
			return false;
		}

		return true;
	}

	/**
	 * Create models for the tiles
	 *
	 * @param r
	 *            Renderer to add models to
	 */
	public void initTiles(Renderer r) {

		shadowMap = new int[tiles.length][tiles[0].length];

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				// r.addModel(tiles[i][j].getModel());
				Tile tile = tiles[i][j];

				// Add model of the tile
				r.addModel(tile.getModel());

				// Setup shadow map
				if (tile.blockLight()) {
					shadowMap[i][j] = 1;
				} else {
					shadowMap[i][j] = 0;
				}

				// If there are any items add them too
				if (tile instanceof BasicFloor) {
					BasicFloor floor = (BasicFloor) tile;
					if (!floor.getItems().isEmpty()) {
						for (Item item : floor.getItems()) {
							r.addModelData(item.getModelData());
							r.addModel(item.getModel());
						}
					}
				}
			}
		}
		r.getCamera("MainCamera").setTarget(
				new Vec3(tiles.length / 2 * 0.2, 0, tiles[0].length / 2 * 0.2));
		r.setMap(shadowMap);
	}

	/**
	 * Removes tiles from the renderer, this is called when the player has to
	 * switch rooms.
	 *
	 * @param r
	 *            - Renderer to remove tiles from
	 */
	public void removeTiles(Renderer r) {

		// Return if the room has not been rendered
		if (shadowMap == null) {
			return;
		}

		// Delete the shadow map
		// r.deleteMap(shadowMap);

		// Delete the tiles
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {

				Tile tile = tiles[i][j];

				// Delete model of the tile
				if (tile.getModel() != null) {
					r.deleteModel(tile.getModel().getName());
				}

				// If there are any items delete them too
				if (tile instanceof BasicFloor) {

					BasicFloor floor = (BasicFloor) tile;

					for (Item item : floor.getItems())
						r.deleteModel(item.getModel().getName());

				}
			}
		}
	}

	/**
	 * Tick through the tiles to see if there are any items to remove.
	 *
	 * @param r
	 *            - the renderer that is rendering the scene.
	 */
	public void tick(Renderer r, Player p) {

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				Tile tile = tiles[i][j];

				if (tile instanceof BasicFloor) {

					BasicFloor floor = (BasicFloor) tile;

					// If there are any items to remove, remove them from the
					// renderer
					for (Item item : floor.getItemsToRemove()) {
						r.deleteModel(item.getModel().getName());
						floor.getItemsToRemove().poll();
					}
					for (Item item : floor.getItemsToUpdate()) {
						r.deleteModel(item.getModel().getName());
						if (p.getRoom() == this)
							r.addModel(item.getModel());
						floor.getItemsToUpdate().poll();
					}
				}
			}
		}
	}

	// public void interactWithPosition

	/**
	 *
	 * @param p
	 * @param x
	 * @param y
	 * @return
	 */
	public final Tile getTile(Player p, double x, double y) {
		double scaledTileSize = TILE_SIZE * SCALE;
		if (x < -(scaledTileSize / 2) || y < -(scaledTileSize / 2)) {
			return null;
		}
		int tileX = (int) ((x + (scaledTileSize / 2)) / scaledTileSize);
		int tileY = (int) ((y + (scaledTileSize / 2)) / scaledTileSize);
		// Out of bounds of array
		if (tileX >= tiles.length || tileX < 0 || tileY >= tiles[tileX].length
				|| tileY < 0) {
			// System.out.println("Trying to leave map!!!");
			return null;
		}
		return tiles[tileX][tileY];
	}

	/**
	 * Returns tile that player is at and checks if tile ID matches ID
	 *
	 * @param p
	 * @param ID
	 * @return
	 */
	public final Tile getTile(Player p, int ID) {

		Tile tile = getTile(p, p.getX(), p.getY());
		if (tile != null && tile.getID() == ID)
			return tile;

		System.err.println("Returning Null");
		return null;
	}

	/**
	 * Get tile x size
	 *
	 * @return
	 */
	public final int getTilesXSize() {
		return xSize;
	}

	/**
	 * Get tile y size
	 *
	 * @return
	 */
	public final int getTilesYSize() {
		return ySize;
	}

	/**
	 * Returns players in room
	 *
	 * @return
	 */
	public List<Player> getPlayersInRoom() {
		return playersInRoom;
	}

	/**
	 * Sets players in room
	 *
	 * @param playersInRoom
	 */
	public void setPlayersInRoom(List<Player> playersInRoom) {
		this.playersInRoom = playersInRoom;
	}

	/**
	 * Add player to room
	 *
	 * @param inPlayer
	 */
	public void addPlayer(Player inPlayer) {
		getPlayersInRoom().add(inPlayer);
	}

	/**
	 * remove player from room
	 *
	 * @param outPlayer
	 */
	public void removePlayer(Player outPlayer) {
		getPlayersInRoom().remove(outPlayer);

	}

	public String getName() {
		return name;
	}
}
