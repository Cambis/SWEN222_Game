package game.control.packets;

import game.control.GameClient;
import game.control.GameServer;

/**
 * This packet is sent when players are assigned teams.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 *         TEAM_ASSIGN = 24 + String username + int team(0:1)
 */
public class Packet24TeamAssign extends Packet {

	private final String[] dataArray;
	private String[] players, teams;

	/**
	 * Constructor intended for sending data
	 *
	 * @param data
	 *            - message to be sent
	 */
	public Packet24TeamAssign(byte[] data) {
		super(24);
		dataArray = readData(data).split(",");
	}

	/**
	 * Constructor intended for receiving data.
	 *
	 * @param players
	 *            - array of player usernames
	 * @param teamseam allocation
	 *            - team allocation GUARD "0" SPY "1"
	 */
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

	/**
	 * Gets the all of the players usernames on the server
	 *
	 * @return player usernames in an array
	 */
	public final String[] getPlayers() {

		String[] players = new String[dataArray.length / 2];

		for (int i = 0; i < players.length; i++) {
			players[i] = dataArray[i * 2];
		}

		return players;
	}

	/**
	 * Gets the teams that the players on the server are assigned to
	 *
	 * @return team allocation GUARD "0" SPY "1"
	 */
	public final String[] getTeams() {

		String[] teams = new String[dataArray.length / 2];

		for (int i = 0; i < teams.length; i++)
			teams[i] = dataArray[(i * 2) + 1];

		return teams;
	}

}
