package game.control;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

	public PlayerMP(String username, double x, double y, Direction direction,
			InetAddress ipAddress, int port) {
		super(username, x, y, direction);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public PlayerMP(String username, InetAddress ipAddress, int port) {
		this(username, 0, 0, Direction.NORTH, ipAddress, port);
	}

	// @Override
	public void tick() {
		// super.tick();
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
}
