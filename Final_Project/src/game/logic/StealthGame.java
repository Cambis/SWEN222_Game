package game.logic;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.List;

import renderer.*;
import renderer.math.Vec3;
import game.control.GameClient;
import game.control.GameServer;
import game.control.PlayerMP;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.view.MainFrame;
import game.view.WindowHandler;

public class StealthGame implements Runnable {

	// Debugging mode
	public static final boolean DEBUG = true;

	// The minimum amount of players to play the game,
	// (TODO: should be >= 2, it is 1 for testing purposes)
	public static final int MIN_PLAYERS = 1;

	// Amount of nano seconds per tick
	public static final double NS_PER_TICK = 1000000000D / 60D;

	// This is the client that connects to the server
	private GameClient client;

	// Server that the clients connect to, it is only created if the player is
	// hosting a game.
	private GameServer server;

	// True IFF player is hosting
	private boolean isHost = false;

	// Is the game running?
	private boolean running = false;

	// Ticks during the game
	private Thread thread;

	// Primary game display
	private MainFrame mainFrame;

	// Handles disconnecting from the server
	private WindowHandler windowHandler;

	// Renders the entire scene
	private Renderer renderer;

	// Generates the current level
	private Level level;

	// We only need a reference to the player on the client here
	private Player player;

	/**
	 * Default constructor, is called by GameClient.
	 *
	 * @param isHost
	 *            - true if user is hosting a game
	 * @param username
	 *            - username for the player
	 */
	public StealthGame(boolean isHost, String username) {
		this.isHost = isHost;
		player = new PlayerMP(username, 0, 0, 0, null, -1);
		init();
	}

	/**
	 * Sets up the game
	 */
	private void init() {

		initRenderer();
		level = new Level(this);
		mainFrame = new MainFrame();

		// Set up window handler
		windowHandler = new WindowHandler(mainFrame) {

			@Override
			public void windowClosing(WindowEvent e) {
				Packet01Disconnect packet = new Packet01Disconnect(
						player.getUsername());
				packet.writeData(client);
				stop();
			}
		};

		mainFrame.addKeyListener(mainFrameListener);
	}

	/**
	 * Sets up the renderer
	 */
	private void initRenderer() {

		renderer = new Renderer(MainFrame.WIDTH, MainFrame.HEIGHT);
		R_OrthoCamera ortho = new R_OrthoCamera("MainCamera", new Vec3(50, 50,
				50), Vec3.Zero(), Vec3.UnitY(), 1, 1000, 2);
		r_addCamera(ortho);
		r_setCamera(ortho.getName());

		// Adds a new model
		R_ModelColorData modelData = new R_ModelColorData("Test",
				"res/Guard.obj", Color.RED);
		r_addModelData(modelData);
	}

	/**
	 * Sets up the server/ client
	 */
	public synchronized void start() {

		running = true;

		thread = new Thread(this, "Test");
		thread.start();

		// Server is created if user is a host
		if (isHost) {
			server = new GameServer(this);
			server.start();
		}

		// Client should always be created
		client = new GameClient("localhost", this);
		client.start();

		// Login to the server
		Packet00Login login = new Packet00Login(player.getUsername());
		if (server != null)
			server.addConnection((PlayerMP) player, login);
		login.writeData(client);
	}

	/**
	 * Called to stop the game
	 */
	public synchronized void stop() {

		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (DEBUG)
			System.out.println(player.getUsername() + " THREAD IS "
					+ (thread.isAlive() ? "ALIVE" : "DEAD"));
	}

	/**
	 * Main loop that runs the game, due to the nature of the Runnable interface
	 * this function is automatically called in the super() constructor.
	 */
	@Override
	public void run() {

		long lastTime = System.nanoTime();

		// For debugging
		int ticks = 0, frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (running) {

			// Calculate when the system should tick and render
			long now = System.nanoTime();
			delta += (now - lastTime) / NS_PER_TICK;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta--;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				// if(DEBUG) System.out.println(ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}

	/**
	 * Updates the current game state.
	 */
	public void tick() {
		level.tick();
		// player.getRoom().draw(renderer);
	}

	/**
	 * Gets the rendered image of the current scene (as a BufferedImage) and
	 * sends it to the main frame.
	 */
	public void render() {
		mainFrame.setImage(renderer.render());
	}

	/** HELPER METHODS **/

	/**
	 * Adds player to the current level
	 *
	 * @param p
	 */
	public synchronized void addPlayer(Player p) {
		level.addPlayer(p);
	}

	/**
	 * Removes player from level
	 *
	 * @param p
	 */
	public void removePlayer(Player p) {
		level.removePlayer(p);
	}

	/**
	 * Removes player from level that matches the username
	 *
	 * @param p
	 */
	public void removePlayer(String name) {
		level.removePlayer(name);
	}

	/**
	 * loads level from specified filepath
	 *
	 * @param filepath
	 */
	public void loadLevel(String filepath) {
		level.loadRooms(filepath);
		level.addPlayer(player);
		player.getRoom().initTiles(renderer);
	}

	/**
	 * Positions a player
	 *
	 * @param username
	 * @param x
	 * @param z
	 * @param rot
	 */
	public synchronized void movePlayer(String username, double x, double z,
			double rot) {
		level.movePlayer(username, x, z, rot);
	}

	/**
	 * gets local client
	 *
	 * @return
	 */
	public final GameClient getClient() {
		return client;
	}

	/**
	 * gets the server
	 *
	 * @return
	 */
	public final GameServer getServer() {
		return server;
	}

	/** RENDERER METHODS **/

	public boolean r_addCamera(R_AbstractCamera camera) {
		return renderer.addCamera(camera);
	}

	public void r_setCamera(String camera) {
		renderer.setCamera(camera);
	}

	public boolean r_addModel(R_AbstractModel model) {
		return renderer.addModel(model);
	}

	public boolean r_removeModel(String model) {
		return false;
	}

	public boolean r_addModelData(R_AbstractModelData modelData) {
		return renderer.addModelData(modelData);
	}

	public R_AbstractModelData getR_ModelData(String name) {
		return renderer.getModelData(name);
	}

	private KeyListener mainFrameListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println(e.getKeyCode());
			switch (e.getKeyCode()) {
			case 37:// Left
				player.setTurnLeft(true);
				break;
			case 38:// Up
				player.setFoward(true);
				break;
			case 39:// Right
				player.setTurnRight(true);
				break;
			case 40:// Down
				player.setBackward(true);
				break;
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			// System.out.println(e.getKeyCode());
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:// Left
				player.setTurnLeft(false);
				break;
			case KeyEvent.VK_UP:// Up
				player.setFoward(false);
				break;
			case KeyEvent.VK_RIGHT:// Right
				player.setTurnRight(false);
				break;
			case KeyEvent.VK_DOWN:// Down
				player.setBackward(false);
				break;
			case KeyEvent.VK_1:// 1
				player.selectItem(1);
				break;
			case KeyEvent.VK_2:// 2
				player.selectItem(2);
				break;
			case KeyEvent.VK_3:// 3
				player.selectItem(3);
				break;
			case KeyEvent.VK_4:// 4
				player.selectItem(4);
				break;
			case KeyEvent.VK_E:// E
				// player.swapWeapon();
				break;
			case KeyEvent.VK_Q:// Q
				player.dropItem();
			}
		}
	};

	// Andrew: Implementing mouse listener
	private MouseListener mainFrameMouseListener = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				player.setShooting(true);
			} else if (e.getButton() == MouseEvent.BUTTON2) {
				player.setUsing(true);
			}
		}

	};
}