package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * Packet sent when a player takes damage.
 *
 * DAMAGE - 04 + "," + String hitPlayer + "," + String attacker + "," + double
 * damage
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public class Packet04Damage extends Packet {

	private String hitPlayer;
	private String attacker;
	private double damage;

	/**
	 * Constructor intended for sending data.
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet04Damage(byte data[]) {

		super(04);

		// Having commas in the byte array makes it easier to read the
		// information off it
		String[] dataArray = readData(data).split(",");

		// Read the information
		hitPlayer = dataArray[0];
		attacker = dataArray[1];
		damage = Double.parseDouble(dataArray[2]);
	}

	/**
	 * Constructor intended for receiving data
	 *
	 * @param hitPlayer
	 *            - player that is hit
	 * @param attacker
	 *            - player that is attacking
	 * @param damage
	 *            - damage hit player receives
	 */
	public Packet04Damage(String hitPlayer, String attacker, double damage) {
		super(04);
		this.hitPlayer = hitPlayer;
		this.attacker = attacker;
		this.damage = damage;
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
		return ("04" + hitPlayer + "," + attacker + "," + damage).getBytes();
	}

	/**
	 * Get the hitPlayer's username.
	 *
	 * @return player's username
	 */
	public final String getHitPlayer() {
		return hitPlayer.trim();
	}

	/**
	 * Get the attackingPlayer's username.
	 *
	 * @return player's username
	 */
	public final String getAttacker() {
		return attacker.trim();
	}

	/**
	 * Damage that the player sustains.
	 *
	 * @return damage for player
	 */
	public final double getDamage() {
		return damage;
	}
}
