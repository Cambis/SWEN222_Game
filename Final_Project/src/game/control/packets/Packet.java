package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * Information between the server and client will be sent by subclasses of this
 * class.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public abstract class Packet {

	/**
	 * Types of packets that can be sent. Packet data format is shown in each
	 * subclass.
	 *
	 * @author Cameron Bryers 300326848 MMXV
	 *
	 */
	public static enum PacketType {

		INVALID(-1), // Invalid operation
		LOGIN(00), // Login to the server
		DISCONNECT(01), // Disconnect from the server
		MOVE(02), // Player moving
		ENGAGE(03), // Player engaging another
		DAMAGE(04), // Player taking damage
		HEAL(05), // Player healing
		INTERACT(06), // Player interacting with a item
		EQUIP(07), // Player equipping an item
		PICKUP(10), // Player picking up an item
		GAME_START(20), // Game starting
		GAME_OVER(21), // Game over
		LOAD_LEVEL(22), // Loading a level
		RECIEVE_ID(23), // Receiving a unique ID
		TEAM_ASSIGN(24); // Assigning teams

		private final int packetID;

		private PacketType(int packetID) {
			this.packetID = packetID;
		}

		public int getID() {
			return packetID;
		}
	}

	public final byte packetID;

	/**
	 * Default Constructor, creates a new Packet.
	 *
	 * @param packetID
	 *            - ID that corresponds to the type of packet
	 */
	public Packet(int packetID) {
		this.packetID = (byte) packetID;
	}

	/**
	 * Write data to a specific client
	 *
	 * @param client
	 *            - client to be written to
	 */
	public abstract void writeData(GameClient client);

	/**
	 * Write data to all the clients on this server
	 *
	 * @param server
	 *            - server to be written to
	 */
	public abstract void writeData(GameServer server);

	/**
	 * Return a byte representation of the packet
	 *
	 * @return packet message as a byte array
	 */
	public abstract byte[] getData();

	/**
	 * Return a String representation of the packet
	 *
	 * @param data
	 *            - message from the packet
	 * @return String representation of the message
	 */
	public String readData(byte[] data) {
		String message = new String(data);

		// Cutoff the two numbers at the start
		return message.substring(2).trim();
	}

	/**
	 * Determine the type of packet given a packetID
	 *
	 * @param packetID
	 *            - unique ID of the packet
	 * @return PacketType that the ID corresponds to
	 */
	public static PacketType lookupPacket(String packetID) {
		try {
			return lookupPacket(Integer.parseInt(packetID));
		} catch (NumberFormatException e) {
			return PacketType.INVALID;
		}
	}

	/**
	 * Determine the type of packet given a enum constant
	 *
	 * @param id
	 *            - enum constant of the packet
	 * @return PacketType that the id corresponds to
	 */
	public static PacketType lookupPacket(int id) {
		for (PacketType p : PacketType.values()) {
			if (p.getID() == id)
				return p;
		}
		return PacketType.INVALID;
	}
}
