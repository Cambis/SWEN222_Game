package game.logic;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import game.logic.items.Item;
import game.logic.items.Key;
import game.logic.world.BasicFloor;
import game.logic.world.Tile;
import game.logic.world.Wall;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import renderer.Renderer;
import renderer.Renderer.*;

public class Room {

	private static final int TILE_SIZE = 10;
	private static final double SCALE = 0.02;

	private Tile[][] tiles;
	private int xSize = 0;
	private int ySize = 0;
	private String name = "This is a unnamed room";

	private List<Player> playersInRoom;

	// Models
	private R_ModelColorData floorData = new R_ModelColorData("Floor",
			"res/models/BasicFloor.obj", Color.GRAY);
	private R_ModelColorData wallData = new R_ModelColorData("BasicWall",
			"res/models/BasicWall.obj", Color.RED);

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
			Scanner s = new Scanner(new File(filename));
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

			while (s.hasNext()) {

				if (s.hasNextInt()) {

					int i = s.nextInt();

					if (i == 0) {
						tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE
								* SCALE, yPos * TILE_SIZE * SCALE, floorData,
								tileNum);
					} else if (i == 1) {
						tiles[xPos][yPos] = new Wall(xPos * TILE_SIZE * SCALE,
								yPos * TILE_SIZE * SCALE, wallData, tileNum);
					} else {
						System.out.println("Error loading file (invalid int - "
								+ i + ", xPos = " + xPos + ", yPox = " + yPos
								+ ")");
					}

					xPos++;
					tileNum++;

					if (xPos >= xSize) {
						xPos = 0;
						yPos++;
					}
				}
				// There is an item on this tile, we can assume that the tile
				// itself is a BasicFloor
				else {

					// Find the item
					char itemKey = s.next().charAt(0);
					Item item = null;

					switch (itemKey) {
					case 'K': // Key
						item = new Key(10, xPos * TILE_SIZE * SCALE, yPos
								* TILE_SIZE * SCALE);
						break;

					default:
						break;
					}

					if (item != null) {
						tiles[xPos][yPos] = new BasicFloor(xPos * TILE_SIZE
								* SCALE, yPos * TILE_SIZE * SCALE, floorData,
								tileNum);

						// Add the item to the room
						((BasicFloor) tiles[xPos][yPos]).addItem(item);

						xPos++;
						tileNum++;

						if (xPos >= xSize) {
							xPos = 0;
							yPos++;
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Room - Error loading file - IOException : "
					+ e.getMessage());
		}
	}

	/**
	 * returns if player can be in position in room
	 *
	 * @param p
	 * @param x
	 *            , y
	 */
	public boolean validPosition(Player p, double x, double y) {
		return getTile(p, x, y).canEnter(p);
	}

	/**
	 * Create models for the tiles
	 *
	 * @param r
	 */
	public void initTiles(Renderer r) {
		int[][] shadowMap = new int[tiles.length][tiles[0].length];
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
		int tileX = (int) ((x + (scaledTileSize / 2)) / scaledTileSize);
		int tileY = (int) ((y + (scaledTileSize / 2)) / scaledTileSize);
		return tiles[tileX][tileY];
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
}
