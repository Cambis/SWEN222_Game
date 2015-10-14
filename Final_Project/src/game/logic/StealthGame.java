package game.logic;

import game.control.GameClient;
import game.control.GameServer;
import game.control.PlayerMP;
import game.control.packets.Packet00Login;
import game.control.packets.Packet01Disconnect;
import game.view.MainFrame;
import game.view.WindowHandler;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import renderer.R_AbstractCamera;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_ModelColorData;
import renderer.R_OrthoCamera;
import renderer.R_Player;
import renderer.R_Player.Team;
import renderer.Renderer;
import renderer.math.Mat4;
import renderer.math.Vec3;

/**
 * Main logic class, runs the actual game.
 *
 * @author Cameron Bryers 300326848 MMXV
 * @author Callum Gill 300316407 2015
 *
 */
public class StealthGame implements Runnable {

	/**
	 * Debugging mode.
	 */
	public static final boolean DEBUG = false;

	/**
	 * True IFF exporting to a runnable jar file.
	 */
	public static final boolean EXPORT = true;

	/**
	 * The minimum amount of players required to play the game.
	 */
	public static final int MIN_PLAYERS = 2;

	/**
	 * The maximum amount of players allowed to connect to a game.
	 */
	public static final int MAX_PLAYERS = 4;

	/**
	 * The amount of nano seconds per tick.
	 */
	public static final double NS_PER_TICK = 1000000000D / 60D;

	// Amount of players that the host chooses
	private int numOfPlayers;

	// This is the client that connects to the server
	private GameClient client;

	// Server that the clients connect to, it is only created if the player is
	// hosting a game.
	private GameServer server;

	// IPAddress entered by the user
	private final String ipAddress;

	// True IFF player is hosting
	private boolean isHost = false;

	// True IFF the game is running
	private boolean running = false;

	// True when the game has been set up.
	private boolean readyToRender = false;

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
	 * Default constructor, it is never called outside this class. The
	 * constructor is called by the static functions host() and client(), which
	 * specify it to create a host or a client.
	 *
	 * @param isHost
	 *            - true if the user is hosting a game
	 * @param username
	 *            - username of the user
	 * @param ipAddress
	 *            - IP address to connect to. Host is defaulted to "localhost"
	 *            while client parses the IP address of the host
	 * @param numOfPlayers
	 *            - number of players needed to start the game. Not used in the
	 *            client constructor.
	 */
	private StealthGame(boolean isHost, String username, String ipAddress,
			int numOfPlayers) {

		this.numOfPlayers = numOfPlayers;

		// Bounds checking for numOfPlayers
		if (numOfPlayers != -1) {
			this.numOfPlayers = (numOfPlayers >= MIN_PLAYERS) ? numOfPlayers
					: MIN_PLAYERS;
			this.numOfPlayers = (numOfPlayers <= MAX_PLAYERS) ? numOfPlayers
					: MAX_PLAYERS;
		}

		this.isHost = isHost;
		this.ipAddress = (ipAddress == null || ipAddress.length() == 0)
		// Running on one computer for testing
		? "localhost"
				// Running on multiple for playing the actual game
				: ipAddress;

		// Create the player
		player = new PlayerMP(username, 0, 0, 0, null, -1);
		init();
	}

	/**
	 * Host constructor, needs to be parsed a number of players. IP address is
	 * defaulted to "localhost".
	 *
	 * @param username
	 *            - username of the host
	 * @param numOfPlayers
	 *            - number of players required to start the game
	 * @return StealthGame for the host
	 */
	public static StealthGame host(String username, int numOfPlayers) {
		return new StealthGame(true, username, "localhost", numOfPlayers);
	}

	/**
	 * Client constructor, needs to be parsed an IP address to connect to.
	 *
	 * @param username
	 *            - username of the client
	 * @param ipAddress
	 *            - IP address of the host to connect to
	 * @return StealthGame for the client
	 */
	public static StealthGame client(String username, String ipAddress) {
		return new StealthGame(false, username, ipAddress, -1);
	}

	/**
	 * Sets up the game, creates the main frame and the window handler.
	 */
	private void init() {

		initRenderer();
		level = new Level(this);
		mainFrame = new MainFrame();
		mainFrame.setTitle("Stealth: " + player.getUsername());

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
		renderer.resize((int) mainFrame.getImageSize().getWidth(),
				(int) mainFrame.getImageSize().getHeight());
	}

	/**
	 * Sets up the renderer and loads models for spies and guards.
	 */
	private void initRenderer() {
		renderer = new Renderer(MainFrame.WIDTH, MainFrame.HEIGHT);
		R_OrthoCamera ortho = new R_OrthoCamera("MainCamera", new Vec3(50, 50,
				50), new Vec3(1, 0, 1), Vec3.UnitY(), 1, 1000, 1.25f);
		r_addCamera(ortho);
		r_setCamera(ortho.getName());

		// Add models

		// Spy
		R_ModelColorData spyModelData = new R_ModelColorData("Spy",
				"res/models/Spy.obj", new Color(225, 180, 105));
		r_addModelData(spyModelData);

		// Guard
		R_ModelColorData guardModelData = new R_ModelColorData("Guard",
				"res/models/Guard.obj", new Color(120, 175, 255));
		r_addModelData(guardModelData);

		readyToRender = true;
	}

	/**
	 * Sets up the server/ client and connects client to the server.
	 */
	public synchronized void start() {

		running = true;

		thread = new Thread(this, (isHost ? "HOST: " : "CLIENT: ")
				+ player.getUsername() + "'s game");
		thread.start();

		// Server is created if user is a host
		if (isHost) {
			server = new GameServer(this, numOfPlayers);
			server.start();
		}

		// Client should always be created
		client = new GameClient(ipAddress, this);
		client.start();

		// Login to the server
		Packet00Login login = new Packet00Login(player.getUsername(), -1, 0, 0,
				0);
		if (server != null)
			server.addConnection((PlayerMP) player, login);
		login.writeData(client);

		if (DEBUG)
			System.out.println(player.getUsername() + " running on "
					+ ipAddress);
	}

	/**
	 * Called to stop the game.
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
				// if(DEBUG) System.out.println(ticks + " ticks, " + frames +
				// " frames");
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
	}

	/**
	 * Gets the rendered image of the current scene (as a BufferedImage) and
	 * sends it to the main frame.
	 */
	public void render() {
		if (readyToRender)
			mainFrame.setImage(renderer.render());
	}

	/** HELPER METHODS **/

	/**
	 * Adds player to the current level.
	 *
	 * @param p
	 *            - player to be added to the level
	 */
	public synchronized void addPlayer(Player p) {
		level.addPlayer(p);
	}

	/**
	 * Removes player from level.
	 *
	 * @param p
	 *            - player to be removed from the level
	 */
	public void removePlayer(Player p) {
		level.removePlayer(p);
	}

	/**
	 * Removes player from level that matches the username.
	 *
	 * @param p
	 *            - player to be removed from the level
	 */
	public void removePlayer(String name) {
		level.removePlayer(name);
	}

	/**
	 * loads level from specified filepath.
	 *
	 * @param filepath
	 *            - path to level configuration file
	 */
	public void loadLevel(String filepath) {
		level.loadRooms(filepath);
		level.addPlayer(player);
	}

	/**
	 * Move a player in the world given a 3D vector.
	 *
	 * @param username
	 *            - username of player to be moved
	 * @param x
	 *            - player's x coordinate (Tile x, Renderer x)
	 * @param y
	 *            - player's y coordinate (Tile y, Renderer z)
	 * @param z
	 *            - player's z coordinate (Tile z, Renderer y)
	 * @param rot
	 *            - rotation around (z axis -> Tile, y axis -> Renderer)
	 * @see game.control.GameClient
	 */
	public synchronized void movePlayer(String username, double x, double y,
			double z, double rot) {
		level.movePlayer(username, x, y, z, rot);
	}

	/**
	 * Gets local client.
	 *
	 * @return local client
	 */
	public final GameClient getClient() {
		return client;
	}

	/**
	 * Gets the local server.
	 *
	 * @return null if user is not a host
	 */
	public final GameServer getServer() {
		return server;
	}

	/**
	 * Gets player created on this class.
	 *
	 * @return player created in this class
	 */
	public final Player getPlayer() {
		return player;
	}

	/** PACKET HANDLERS **/

	/**
	 * Set teams of the players
	 *
	 * @param players
	 *            - array of player usernames from the server
	 * @param teams
	 *            - team allocating, "0" for guard and "1" for spy.
	 * @see game.control.GameServer
	 */
	public void setTeams(String[] players, String[] teams) {

		for (int i = 0; i < players.length; i++) {
			if (players[i].equals(player.getUsername())) {
				player.setSide((teams[i].equals("0") ? Team.GUARD : Team.SPY));
				System.out.println(player.getUsername() + " teams is: "
						+ player.getSide().toString());
				println(player.getUsername() + " teams is: "
						+ player.getSide().toString());
			}
		}
		level.setTeams(players, teams);

		// Sets the renderer to draw the correct team
		R_Player.Team rteam = R_Player.Team.SPY;
		if (player.getSide() == Team.GUARD) {
			rteam = R_Player.Team.GUARD;
		}
		renderer.setTeam(rteam);
	}

	/**
	 * Handles an interact packet sent from the server. Interactions are then
	 * performed by the appropriate player on the appropriate tile.
	 *
	 * @param username
	 *            - username of the player interacting
	 * @param ID
	 *            - ID of the tile to interact with
	 * @see game.control.GameClient
	 */
	public void handleInteract(String username, int ID) {
		level.handleInteract(username, ID);
	}

	/**
	 * Handles a pickup packet from the server. The level then gives the
	 * appropriate player the item parsed in the packet.
	 *
	 * @param username
	 *            - username of the player picking up the item
	 * @param tileID
	 *            - ID of the tile that the item was on
	 * @param itemID
	 *            - ID of the item to be picked up
	 * @see game.control.GameClient
	 */
	public void handlePickUp(String username, int tileID, int itemID) {
		level.handlePickUp(username, tileID, itemID);
	}

	/**
	 * Handles a damage packet from the server. The method then gives the
	 * appropriate amount of damage to the appropriate player.
	 *
	 * @param username
	 *            - username of player affected
	 * @param damage
	 *            - amount of damage to deal
	 * @see game.control.GameClient
	 */
	public void handleDamage(String username, double damage) {
		if (player.getUsername().equals(username)) {
			if (player.isAlive())
				println(username + ", you are taking damage!! "
						+ player.getHealth() + "%");
			else
				println(username + " m8, you dead!");
		}

		level.handleDamage(username, damage);
	}

	/** RENDERER METHODS **/

	/**
	 * Add a camera to the renderer.
	 *
	 * @param camera
	 *            - camera being added to the scene
	 * @return true if camera was added successfully
	 * @see game.renderer.R_AbstractCamera
	 */
	public boolean r_addCamera(R_AbstractCamera camera) {
		return renderer.addCamera(camera);
	}

	/**
	 * Set the current camera to view from
	 *
	 * @param camera
	 *            - camera to view
	 * @see game.renderer.R_AbstractCamera
	 */
	public void r_setCamera(String camera) {
		renderer.setCamera(camera);
	}

	/**
	 * Add a model to the renderer.
	 *
	 * @param model
	 *            - model to be added
	 * @return true if model was successfully added
	 * @see game.renderer.R_AbstractModel
	 */
	public synchronized boolean r_addModel(R_AbstractModel model) {
		return renderer.addModel(model);
	}

	/**
	 * Remove a model from the renderer.
	 *
	 * @param model
	 *            - model to be removed
	 * @return true if model was removed successfully
	 * @see game.renderer.R_AbstractModel
	 */
	public boolean r_removeModel(String model) {
		return renderer.deleteModel(model);
	}

	/**
	 * Add modelData for a model in the renderer.
	 *
	 * @param modelData
	 *            - modelData to be added
	 * @return true if modelData was added successfully
	 * @see game.renderer.R_AbstractModelData
	 */
	public synchronized boolean r_addModelData(R_AbstractModelData modelData) {
		return renderer.addModelData(modelData);
	}

	/**
	 * Get the modelData from the renderer according to its name.
	 *
	 * @param name
	 *            - name of the modelData
	 * @return null if no instance of modelData has name
	 * @see game.renderer.R_AbstractModelData
	 */
	public R_AbstractModelData getR_ModelData(String name) {
		return renderer.getModelData(name);
	}

	/**
	 * Gets the reference to the renderer created in initRenderer()
	 *
	 * @return this.renderer
	 */
	public final Renderer getRenderer() {
		return renderer;
	}

	/**
	 * Prints a String to the text area in the mainframe text box
	 *
	 * @param message
	 *            - message to be printed
	 */
	public void println(String message) {
		mainFrame.println(message);
	}

	/**
	 * Listens for keyboard actions from the user.
	 */
	private KeyListener mainFrameListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT: // Left
			case KeyEvent.VK_A:
				player.setTurnLeft(true);
				break;

			case KeyEvent.VK_UP: // Up
			case KeyEvent.VK_W:
				player.setFoward(true);
				break;

			case KeyEvent.VK_RIGHT: // Right
			case KeyEvent.VK_D:
				player.setTurnRight(true);
				break;

			case KeyEvent.VK_DOWN: // Down
			case KeyEvent.VK_S:
				player.setBackward(true);
				break;

			case KeyEvent.VK_SPACE: // Shooting
				player.setShooting(true);
				break;

			case KeyEvent.VK_E: // Interact
				player.setInteracting(true);
				break;

			case KeyEvent.VK_Z: // rotate camera left
				renderer.getCamera("MainCamera").setPosition(
						Mat4.createRotationYAxis((float) Math.toRadians(5))
								.mul(renderer.getCamera("MainCamera")
										.getPosition()));
				break;
			case KeyEvent.VK_X: // rotate camera right
				renderer.getCamera("MainCamera").setPosition(
						Mat4.createRotationYAxis((float) Math.toRadians(-5))
								.mul(renderer.getCamera("MainCamera")
										.getPosition()));
				break;

			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT: // Left
			case KeyEvent.VK_A:
				player.setTurnLeft(false);
				break;
			case KeyEvent.VK_UP: // Up
			case KeyEvent.VK_W:
				player.setFoward(false);
				break;
			case KeyEvent.VK_RIGHT: // Right
			case KeyEvent.VK_D:
				player.setTurnRight(false);
				break;
			case KeyEvent.VK_DOWN: // Down
			case KeyEvent.VK_S:
				player.setBackward(false);
				break;
			}
		}
	};
}
