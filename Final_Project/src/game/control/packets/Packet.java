package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

public abstract class Packet {

	public static enum PacketType {
		INVALID(-1), LOGIN(00), DISCONNECT(01);

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

	public abstract void writeData(GameClient client);

	public abstract void writeData(GameServer server);

	public abstract byte[] getData();

	public String readData(byte[] data) {
		String message = new String(data);

		// Cutoff the two numbers at the start
		return message.substring(2);
	}

	public static PacketType lookupPacket(String packetID) {
		try {
			return lookupPacket(Integer.parseInt(packetID));
		} catch (NumberFormatException e) {
			return PacketType.INVALID;
		}
	}

	public static PacketType lookupPacket(int id) {
		for (PacketType p : PacketType.values()) {
			if (p.getID() == id)
				return p;
		}
		return PacketType.INVALID;
	}
}
