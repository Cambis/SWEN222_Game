package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * Information between the server and client will be sent by subclasses of this
 * class.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public abstract class Packet {

	/**
	 * Types of packets that can be sent. Packet data format is shown in each
	 * subclass.
	 *
	 * @author Bieleski, Bryers, Gill & Thompson MMXV.
	 *
	 */
	public static enum PacketType {

		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);

		private final int packetID;

		private PacketType(int packetID) {
			this.packetID = packetID;
		}

		public int getID() {
			return packetID;
		}
	}

	public final byte packetID;

	public Packet(int packetID) {
		this.packetID = (byte) packetID;
	}

	/**
	 * Write data to a specific client
	 *
	 * @param client
	 */
	public abstract void writeData(GameClient client);

	/**
	 * Write data to all the clients on this server
	 *
	 * @param server
	 */
	public abstract void writeData(GameServer server);

	/**
	 * Return a byte representation of the packet
	 *
	 * @return
	 */
	public abstract byte[] getData();

	/**
	 * Return a String representation of the packet
	 *
	 * @param data
	 * @return
	 */
	public String readData(byte[] data) {
		String message = new String(data);

		// Cutoff the two numbers at the start
		return message.substring(2);
	}

	/**
	 * Determine the type of packet given a packetID
	 *
	 * @param packetID
	 * @return
	 */
	public static PacketType lookupPacket(String packetID) {
		try {
			return lookupPacket(Integer.parseInt(packetID));
		} catch (NumberFormatException e) {
			return PacketType.INVALID;
		}
	}

	/**
	 * Helper method
	 *
	 * @param id
	 * @return
	 */
	public static PacketType lookupPacket(int id) {
		for (PacketType p : PacketType.values()) {
			if (p.getID() == id)
				return p;
		}
		return PacketType.INVALID;
	}
}
