package game.control.packets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import game.control.GameClient;
import game.control.GameServer;

/**
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 * TEAM_ASSIGN = 24 + String username + int team(0:1)
 */
public class Packet24TeamAssign extends Packet {

	private final String[] dataArray;
	private String[] players, teams;

	public Packet24TeamAssign(byte[] data) {
		super(24);
		dataArray = readData(data).split(",");
	}

	public Packet24TeamAssign(String[] playersToTeam) {
		super(24);
		dataArray = playersToTeam;
	}

	public Packet24TeamAssign(String[] players, String[] teams) {

		super(24);

		dataArray = new String[players.length + teams.length];
		this.players = players;
		this.teams = teams;

		for (int i = 0; i < players.length; i++) {
			dataArray[i * 2] = players[i];
			dataArray[(i * 2) + 1] = teams[i];
		}
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
		String data = "24";

		for (int i = 0; i < dataArray.length; i++)
			data += dataArray[i] + ",";

		return data.getBytes();
	}

	public final String[] getPlayers() {

		String[] players = new String[dataArray.length / 2];

		for (int i = 0; i < players.length; i++) {
			players[i] = dataArray[i * 2];
		}

		return players;
	}

	public final String[] getTeams() {

		String[] teams = new String[dataArray.length / 2];

		for (int i = 0; i < teams.length; i++)
			teams[i] = dataArray[(i * 2) + 1];

		return teams;
	}

}
