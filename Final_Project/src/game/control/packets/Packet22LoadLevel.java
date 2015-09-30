package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

public class Packet22LoadLevel extends Packet {

	private final String filename;

	public Packet22LoadLevel(byte data[]) {
		super(22);
		this.filename = readData(data);
	}

	public Packet22LoadLevel(int packetID, String filename) {
		super(packetID);
		this.filename = filename;
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
		return ("22" + filename).getBytes();
	}

	public String getFilename() {
		return filename;
	}

}
