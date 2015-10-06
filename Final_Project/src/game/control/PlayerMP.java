package game.control;

import java.net.InetAddress;
import java.net.UnknownHostException;

import game.logic.Player;

/**
 * This class should do the multiplayer connecting side, player logic should be
 * left to Player.java.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class PlayerMP extends Player {

	private InetAddress ipAddress;
	private int port;
	private int uid = 00000;

	public PlayerMP(String username, double x, double y, double rotation,
			InetAddress ipAddress, int port) {
		super(username, 0.2, 0.2, rotation);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public PlayerMP(String username, InetAddress ipAddress, int port) {
		this(username, 0, 0, 0, ipAddress, port);
	}

	@Override
	public void tick() {
		super.tick();
	}

	// TODO Cameron's tick packet
	private void sendTickPacket() {

	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public final InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public final int getPort() {
		return port;
	}

	public void setID(int id) {
		uid = id;
	}

	public final int getID() {
		return uid;
	}
}
