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
	private double x, z;

	private int numOfSteps = 0;
	private boolean isMoving;
	private double direction;

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

		this.x = Double.parseDouble(dataArray[1]);
		this.z = Double.parseDouble(dataArray[2]);

		this.numOfSteps = Integer.parseInt(dataArray[3]);
		this.isMoving = Integer.parseInt(dataArray[4]) == 1;
		this.direction = Double.parseDouble(dataArray[5]);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param data
	 */
	public Packet02Move(String username, double x, double y, int numOfSteps,
			boolean isMoving, double direction) {
		super(02);
		this.username = username;
		this.x = x;
		this.z = y;
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
		return ("02" + username + "," + x + "," + z + "," + numOfSteps + ","
				+ (isMoving ? 1 : 0) + "," + direction).getBytes();
	}

	public String getUsername() {
		return username;
	}

	public double getX() {
		return x;
	}

	public double getZ() {
		return z;
	}

	public int getNumOfSteps() {
		return numOfSteps;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public double getDirection() {
		return direction;
	}

}
