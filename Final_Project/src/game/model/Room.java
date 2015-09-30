package game.model;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Room {

	private final int TILE_SIZE = 10;

	private Tile[][] tiles;
	private int xSize = 0;
	private int ySize = 0;
	private String name = "This is a unnamed room";

	public Room(String file){

	}

	/**
	 * Load tiles from room file
	 * @param filename
	 */
	private void loadTiles(String filename){
		try {
			Scanner s = new Scanner(new File(filename));
			if(s.hasNext()){
				name = s.next();
			}else{
				System.out.println("Room has no name");
			}
			xSize = s.nextInt();
			ySize = s.nextInt();
			tiles = new Tile[xSize * 2][ySize];
			int xPos = 0;
			int yPos = 0;
			while (s.hasNext()) {
				if (s.hasNextInt()) {
					int i = s.nextInt();
					if (i == 0) {
						tiles[xPos][yPos] = new BasicFloor();
					} else if (i == 1) {
						tiles[xPos][yPos] = new Wall();
					} else {
						System.out.println("Error loading file (invalid int - "
								+ i + ", xPos = " + xPos + ", yPox = " + yPos
								+ ")");
					}
					// Else square is a player or target
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading file - IOException : " + e.getMessage());
		}
	}

	/**
	 * returns if player can be in position in room
	 * @param p
	 * @param x, y
	 */
	public boolean validPosition(Player p, double x, double y){
		int tileX = (int)(x/TILE_SIZE);
		int tileY = (int)(y/TILE_SIZE);
		return tiles[tileX][tileY].canEnter(p);
	}

}
