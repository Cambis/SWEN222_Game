package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when a client has moved.
 *
 * MOVE - 02 + "," + String username + "," + int pos.x + "," + int pos.y + "," +
 * int numOfSteps + "," + int isMoving(1:0) + "," + int dir(1:2:3:4)
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet02Move extends Packet {

	private String username;
	private int x, y;

	private int numOfSteps = 0;
	private boolean isMoving;
	private int direction;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 */
	public Packet02Move(byte[] data) {

		super(02);

		// Having commas in the byte array makes it easier to read the
		// information off it
		String[] dataArray = readData(data).split(",");

		// Read the information
		this.username = dataArray[0];

		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);

		this.numOfSteps = Integer.parseInt(dataArray[3]);
		this.isMoving = Integer.parseInt(dataArray[4]) == 1;
		this.direction = Integer.parseInt(dataArray[5]);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param data
	 */
	public Packet02Move(String username, int x, int y, int numOfSteps,
			boolean isMoving, int direction) {
		super(02);
		this.username = username;
		this.x = x;
		this.y = y;
		this.numOfSteps = numOfSteps;
		this.isMoving = isMoving;
		this.direction = direction;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + username + "," + x + "," + y + "," + numOfSteps + ","
				+ (isMoving ? 1 : 0) + "," + direction).getBytes();
	}

	public final String getUsername() {
		return username;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final int getNumOfSteps() {
		return numOfSteps;
	}

	public final boolean isMoving() {
		return isMoving;
	}

	public final int getDirection() {
		return direction;
	}
}
