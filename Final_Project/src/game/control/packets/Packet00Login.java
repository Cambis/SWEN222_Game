package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent to the server when a player connects to a game.
 *
 * LOGIN - 00 + (String username)
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class Packet00Login extends Packet {

	private final String username;
	private final double x, z, rotation;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 */
	public Packet00Login(byte[] data) {
		super(00);

		String[] dataArray = readData(data).split(",");

		this.username = dataArray[0];
		this.x = Double.parseDouble(dataArray[1]);
		this.z = Double.parseDouble(dataArray[2]);
		this.rotation = Double.parseDouble(dataArray[2]);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param username
	 */
	public Packet00Login(String username, double x, double z, double rotation) {
		super(00);
		this.username = username;
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
		return ("00" + this.username + "," + x + "," + z + "," + rotation).getBytes();
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
}
