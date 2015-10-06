package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

public class Packet23RecieveID extends Packet {

	private int uid;

	public Packet23RecieveID(byte[] data) {
		super(23);
		this.uid = Integer.parseInt(readData(data).trim());
	}

	public Packet23RecieveID(int id) {
		super(23);
		this.uid = id;
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
		return ("23" + uid).getBytes();
	}

	public final int getID() {
		return uid;
	}
}
