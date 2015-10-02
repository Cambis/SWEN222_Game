package game.logic;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import game.logic.world.BasicFloor;
import game.logic.world.Tile;
import game.logic.world.Wall;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.Renderer;
import renderer.Renderer.*;

public class Room {

	private final int TILE_SIZE = 2;

	private Tile[][] tiles;
	private int xSize = 0;
	private int ySize = 0;
	private String name = "This is a unnamed room";

	private List<Player> playersInRoom;

	//Models
	private R_ModelColorData floorData = new R_ModelColorData("Floor", "res/BasicFloor.obj", Color.GRAY);
	private R_ModelColorData wallData = new R_ModelColorData("Floor", "res/BasicWall.obj", Color.RED);

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
			while (s.hasNext()) {
				if (s.hasNextInt()) {
					int i = s.nextInt();
					if (i == 0) {
						tiles[xPos][yPos] = new BasicFloor(xPos*TILE_SIZE, yPos*TILE_SIZE, floorData);
					} else if (i == 1) {
						tiles[xPos][yPos] = new Wall(xPos*TILE_SIZE, yPos*TILE_SIZE, wallData);
					} else {
						System.out.println("Error loading file (invalid int - "
								+ i + ", xPos = " + xPos + ", yPox = " + yPos
								+ ")");
					}
					xPos++;
					if(xPos>=xSize){
						xPos=0;
						yPos++;
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
		int tileX = (int) (x / TILE_SIZE);
		int tileY = (int) (y / TILE_SIZE);
		return tiles[tileX][tileY].canEnter(p);
	}

	public void initTiles(Renderer r) {
		for(int i=0; i<tiles.length; i++){
			for(int j=0; j<tiles[0].length; j++){
				r.addModel(tiles[i][j].getModel());
			}
		}

	}

	public Tile getTile(Player inPlayer, double inX, double inY){

		if (validPosition(inPlayer, inX, inY)){
			int tileX = (int) (inX / TILE_SIZE);
			int tileY = (int) (inY / TILE_SIZE);
			return tiles[tileX][tileY];
		} else {
			return null;
		}

	}

	public List<Player> getPlayersInRoom() {
		return playersInRoom;
	}
	public void setPlayersInRoom(List<Player> playersInRoom) {
		this.playersInRoom = playersInRoom;
	}
	public void addPlayer(Player inPlayer){
		getPlayersInRoom().add(inPlayer);
	}
	public void removePlayer (Player outPlayer){
		getPlayersInRoom().remove(outPlayer);
	}
}
