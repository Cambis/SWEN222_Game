package game.control;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class should do the multiplayer connecting side.
 *
 * @author Bieleski, Bryers, Gill & Thompson MMXV.
 *
 */
public class PlayerMP extends Player {

	private final InetAddress ipAddress;
	private final int port;

	public PlayerMP(InetAddress ipAddress, int port) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
	}

	//@Override
	public void tick() {
		// super.tick();
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public int getPort() {
		return port;
	}
}
