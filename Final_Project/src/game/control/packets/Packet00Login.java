package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent to the server when a player connects to a game.
 *
 * LOGIN - 00 + (String username) + "," + int id + "," + double x + "," + double z + "," + double rotation
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet00Login extends Packet {

	private final String username;
	private final double x, z, rotation;
	private final int uid;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 */
	public Packet00Login(byte[] data) {
		super(00);

		String[] dataArray = readData(data).split(",");

		this.username = dataArray[0];
		this.uid = Integer.parseInt(dataArray[1]);
		this.x = Double.parseDouble(dataArray[2]);
		this.z = Double.parseDouble(dataArray[3]);
		this.rotation = Double.parseDouble(dataArray[4]);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param username
	 */
	public Packet00Login(String username, int uid, double x, double z, double rotation) {
		super(00);
		this.username = username;
		this.uid = uid;
		this.x = x;
		this.z = z;
		this.rotation = rotation;
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
		return ("00" + this.username + "," + uid + "," + x + "," + z + "," + rotation).getBytes();
	}

	public String getUsername() {
		return username.trim();
	}

	public final double getX() {
		return x;
	}

	public final double getZ() {
		return z;
	}

	public final double getRotation() {
		return rotation;
	}

	public final int getID() {
		return uid;
	}
}
