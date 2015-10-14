package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when a player is healed.
 *
 * HEAL - 05 + String username + "," + double healAmount
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Packet05Heal extends Packet {

	private String username;
	private double healAmount;

	/**
	 * Constructor intended for sending data.
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet05Heal(byte data[]) {
		super(05);

		// Having commas in the byte array makes it easier to read the
		// information off it
		String[] dataArray = readData(data).split(",");

		// Read the information
		username = dataArray[0];
		healAmount = Double.parseDouble(dataArray[1]);
	}

	/**
	 * Constructor intended for receiving data.
	 *
	 * @param username
	 *            - username of player
	 * @param healAmount
	 *            - amount the the player is healed
	 */
	public Packet05Heal(String username, double healAmount) {
		super(05);
		this.username = username;
		this.healAmount = healAmount;
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
		return ("05" + username + "," + healAmount).getBytes();
	}

	/**
	 * Get the player's username.
	 *
	 * @return player's username
	 */
	public final String getUsername() {
		return username.trim();
	}

	/**
	 * Get the heal amount for the player.
	 *
	 * @return amount to heal
	 */
	public final double getHealAmount() {
		return healAmount;
	}
}
