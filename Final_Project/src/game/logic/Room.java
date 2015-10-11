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

import game.logic.weapons.Lazor;
import game.logic.items.Item;
import game.logic.items.Key;
import game.logic.world.BasicFloor;
import game.logic.world.BlankTile;
import game.logic.world.Door;
import game.logic.world.Tile;
import game.logic.world.Wall;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import renderer.Renderer;
import renderer.Renderer.*;
import resource.ResourceLoader;

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
	private R_ModelColorData floorData = new R_ModelColorData("Floor",
			"res/models/BasicFloor.obj", Color.GRAY);
	private R_ModelColorData doorData1 = new R_ModelColorData("Door1",
			"res/models/BasicFloor.obj", Color.ORANGE);
	private R_ModelColorData doorData2 = new R_ModelColorData("Door2",
			"res/models/BasicFloor.obj", Color.GREEN);
	private R_ModelColorData wallData = new R_ModelColorData("BasicWall",
			"res/models/BasicWall.obj", Color.RED);
	private R_ModelColorData lazerData = new R_ModelColorData("Lazer",
			"res/models/lazer.obj", Color.RED);

	public Room(String filename) {
		loadTiles(filename);
		setPlayersInRoom(new ArrayList<Player>());
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

					int i = s.nextInt();

					if (i == 0) {
						tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE
								* SCALE, yPos * TILE_SIZE * SCALE, floorData,
								tileNum);
					} else if (i == 1) {
						tiles[xPos][yPos] = new Wall(xPos * TILE_SIZE * SCALE,
								yPos * TILE_SIZE * SCALE, tileNum);
					} else if (i > 1) {
						tiles[xPos][yPos] = new Door(xPos * TILE_SIZE * SCALE,
								yPos * TILE_SIZE * SCALE, tileNum, i);
						doors.add((Door) tiles[xPos][yPos]);
					}
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
						// Find the item
						switch(str){
						case "-":
							tiles[xPos][yPos] = new BlankTile();
							break;
						case "S":
							tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE
									* SCALE, yPos * TILE_SIZE * SCALE, floorData,
									tileNum);
							spawns.add(new SpawnPoint(this, xPos, yPos, Team.SPY));
							break;
						case "G":
							tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE
									* SCALE, yPos * TILE_SIZE * SCALE, floorData,
									tileNum);
							spawns.add(new SpawnPoint(this, xPos, yPos, Team.GUARD));
							break;
						default:
							char itemKey = str.charAt(0);
							Item item = null;
							item = parseItem(xPos, yPos, itemKey, item);
							if (item != null) {
								tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE
										* SCALE, yPos * TILE_SIZE * SCALE,
										floorData, tileNum);

								// Add the item to the room
								((BasicFloor) tiles[xPos][yPos]).addItem(item);
							}
							break;
						}
						xPos++;
						tileNum++;

						if (xPos >= xSize) {
							xPos = 0;
							yPos++;
						}
					} else {
						if (str.equals("door")) {
							// Load door
							if (s.hasNextInt()) {
								int doorId = s.nextInt();
								if (s.hasNext()) {
									String roomName = s.next();
									int targetX = s.nextInt();
									int targetY = s.nextInt();
									for (Door d : doors) {
										if (d.doorID == doorId) {
											// Give door a room destination in
											// map to be assigned in level
											doorDests.put(d, roomName);
											d.setTargetPos(targetX, targetY);
										}
									}
								}
							}
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
	 * initilize door destinations in this room
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

	public List<SpawnPoint> getSpawns(){
		return spawns;
	}

	private Item parseItem(int xPos, int yPos, char itemKey, Item item) {
		switch (itemKey) {
		case 'K': // Key
			item = new Key(10, xPos * TILE_SIZE * SCALE, yPos * TILE_SIZE
					* SCALE);
			break;

		default:
			break;
		}
		return item;
	}

	/**
	 * returns if player can be in position in room
	 *
	 * @param p
	 * @param x
	 *            , y
	 */
	public boolean validPosition(Player p, double x, double y) {
		Tile tile = getTile(p, x, y);
		return tile != null && tile.canEnter(p);
	}

	/**
	 * Create models for the tiles
	 *
	 * @param r
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
				if (tile instanceof Wall) {
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
				if(tile.getModel()!=null){
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
	public void tick(Renderer r) {

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				Tile tile = tiles[i][j];

				if (tile instanceof BasicFloor) {

					BasicFloor floor = (BasicFloor) tile;

					// If there are any items to remove, remove them from the
					// renderer
					if (!floor.getItemsToRemove().isEmpty()) {
						for (Item item : floor.getItemsToRemove()) {
							r.deleteModel(item.getModel().getName());
							floor.getItemsToRemove().poll();
						}
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
	public Tile getTile(Player p, double x, double y) {
		double scaledTileSize = TILE_SIZE * SCALE;
		if (x < -(scaledTileSize / 2) || y < -(scaledTileSize / 2)) {
			return null;
		}
		int tileX = (int) ((x + (scaledTileSize / 2)) / scaledTileSize);
		int tileY = (int) ((y + (scaledTileSize / 2)) / scaledTileSize);
		// Out of bounds of array
		if (tileX >= tiles.length || tileX < 0 || tileY >= tiles[tileX].length
				|| tileY < 0) {
			System.out.println("Trying to leave map!!!");
			return null;
		}
		return tiles[tileX][tileY];
	}

	public final int getTilesXSize() {
		return xSize;
	}

	public final int getTilesYSize() {
		return ySize;
	}

	public List<Player> getPlayersInRoom() {
		return playersInRoom;
	}

	public void setPlayersInRoom(List<Player> playersInRoom) {
		this.playersInRoom = playersInRoom;
	}

	public void addPlayer(Player inPlayer) {
		getPlayersInRoom().add(inPlayer);
	}

	public void removePlayer(Player outPlayer) {
		getPlayersInRoom().remove(outPlayer);

	}

	public String getName() {
		return name;
	}

	public void tick() {
		// for(Lazor l : lazers){
		// l.tick();
		// }
	}
}
