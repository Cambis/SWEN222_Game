package renderer;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import renderer.R_Player.Team;
import renderer.math.Mat4;
import renderer.math.Vec3;

/**
 * Provides a class for rendering a 3d scene to a buffered image.
 *
 * @author Stephen Thompson
 */
public class Renderer {
	// The map of all model data stored inside the scene
	private ConcurrentHashMap<String, R_AbstractModelData> modelDataMap;

	// The map of all models inside the scene
	private ConcurrentHashMap<String, R_AbstractModel> modelMap;

	// The map of all cameras inside the scene
	private ConcurrentHashMap<String, R_AbstractCamera> cameraMap;

	// The map of all players inside the scene
	private ConcurrentHashMap<String, R_Player> playerMap;

	// The currently selected camera to use
	private R_AbstractCamera currentCam;

	// The currently team to view;
	private R_Player.Team side = R_Player.Team.SPY;

	// The map
	private int[][] shadowMap = new int[1][1];
	private int tileSize = 10;

	// Defines the rendered image's width and height
	private int width;
	private int height;

	/**
	 * Constructs a new Renderer object. The new renderer object has a default
	 * camera named "default".
	 *
	 * @param width
	 *            the width of the rendered image
	 * @param height
	 *            the height of the rendered image
	 */
	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
		this.currentCam = new R_OrthoCamera("default", new Vec3(1, 1, 1),
				Vec3.Zero(), Vec3.UnitY(), 1, 1000, 1);
		currentCam.setAspect(1.f * width / height);

		modelDataMap = new ConcurrentHashMap<String, R_AbstractModelData>();
		modelMap = new ConcurrentHashMap<String, R_AbstractModel>();
		cameraMap = new ConcurrentHashMap<String, R_AbstractCamera>();
		playerMap = new ConcurrentHashMap<String, R_Player>();
		addCamera(currentCam);
	}

	/**
	 * Adds a new model to the scene, returning false if a model with the same
	 * name already exists
	 *
	 * @param m
	 *            the new model
	 * @return whether the model was successfully added or not
	 */
	public boolean addModel(R_AbstractModel m) {
		if (modelMap.containsKey(m.getName())) {
			return false;
		}
		modelMap.put(m.getName(), m);
		if (m instanceof R_Player) {
			playerMap.put(m.getName(), (R_Player) m);
		}
		return true;
	}

	/**
	 * Gets the model with the name "name" in the scene
	 *
	 * @param name
	 *            the name of the model to get
	 * @return the model with the name "name" or null if the model does not
	 *         exist
	 */
	public R_AbstractModel getModel(String name) {
		return modelMap.get(name);
	}

	/**
	 * Removes the model with the passed in name from the scene, returning false
	 * if a model does not exist
	 *
	 * @param m
	 *            the model's name
	 * @return whether the model was successfully removed or not
	 */
	public boolean deleteModel(String name) {
		if (!modelMap.containsKey(name)) {
			return false;
		}
		if (!playerMap.containsKey(name)) {
			playerMap.remove(name);
		}
		modelMap.remove(name);
		return true;
	}

	/**
	 * Adds a new model to the scene, returning false if a model with the same
	 * name already exists
	 *
	 * @param m
	 *            the new model
	 * @return whether the model was successfully added or not
	 */
	public boolean addModelData(R_AbstractModelData m) {
		if (modelDataMap.containsKey(m.getName())) {
			return false;
		}
		modelDataMap.put(m.getName(), m);
		return true;
	}

	/**
	 * Gets the model with the name "name" in the scene
	 *
	 * @param name
	 *            the name of the model to get
	 * @return the model with the name "name" or null if the model does not
	 *         exist
	 */
	public R_AbstractModelData getModelData(String name) {
		return modelDataMap.get(name);
	}

	/**
	 * Adds a new camera to the scene, returning false if a camera with the same
	 * name already exists
	 *
	 * @param c
	 *            the new camera
	 * @return whether the camera was successfully added or not
	 */
	public boolean addCamera(R_AbstractCamera c) {
		if (cameraMap.containsKey(c.getName())) {
			return false;
		}
		cameraMap.put(c.getName(), c);
		return true;
	}

	/**
	 * Removes the camera with the passed in name from the scene, returning
	 * false if a camera does not exist
	 *
	 * @param c
	 *            the camera's name
	 * @return whether the camera was successfully removed or not
	 */
	public boolean deleteCamera(String name) {
		if (currentCam.getName().equals(name) || cameraMap.containsKey(name)) {
			return false;
		}
		cameraMap.remove(name);
		return true;
	}

	/**
	 * Gets the camera with the name "name" in the scene
	 *
	 * @param name
	 *            the name of the camera to get
	 * @return the camera with the name "name" or null if the camera does not
	 *         exist
	 */
	public R_AbstractCamera getCamera(String name) {
		return cameraMap.get(name);
	}

	/**
	 * Sets the current camera used by the renderer to render the scene. Returns
	 * true if the camera is successfully set.
	 *
	 * @param name
	 *            the camera's name
	 * @return boolean
	 */
	public boolean setCamera(String name) {
		if (!cameraMap.containsKey(name)) {
			return false;
		}
		currentCam = cameraMap.get(name);
		currentCam.setAspect(1.f * width / height);
		return true;
	}

	/**
	 * Changes the size of the render.
	 *
	 * @param width
	 *            the new width of the render
	 * @param height
	 *            the new height of the render
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		currentCam.setAspect(1.f * width / height);
	}

	/**
	 * Sets the map grid for shadow mapping
	 *
	 * @param newShadowMap the array representing the walls on the map
	 */
	public void setMap(int[][] newShadowMap){
		shadowMap = newShadowMap;
	}

	/**
	 * Renders the scene and returns a buffered image of the render
	 *
	 * @return the BufferedImage of the rendered scene
	 */
	public BufferedImage render() {
		BufferedImage viewport = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		float zBuffer[][] = new float[width][height];

		// Refresh Buffers
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				zBuffer[x][y] = Float.MAX_VALUE;
			}
		}

		/** Create Matrix Stack **/
		Mat4 matrixStack = Mat4.createIdentity();
		// Scale the image to fit the window
		matrixStack = matrixStack.mul(Mat4
				.createScale(width / 2, height / 2, 1));
		// Move the image to fit inside the window
		matrixStack = matrixStack.mul(Mat4.createTranslate(1, 1, 1));
		// Project image to window coordinates
		matrixStack = matrixStack.mul(currentCam.getProjection());
		// transform image to camera coordinates
		matrixStack = matrixStack.mul(currentCam.getLookAt());

		int[] buf = ((DataBufferInt) viewport.getRaster().getDataBuffer())
				.getData();

		List<Light> lights = new ArrayList<Light>();
		for (R_Player m : playerMap.values()) {
			lights.add(new Light(m.getPosition(), m.getOrientation(), m.getSide()));
		}

		// Draw Model
		final Mat4 matrix = matrixStack;
		for (R_AbstractModel m : modelMap.values()) {
			R_Player.Team visible = R_Player.Team.SCENE;
			if (m instanceof R_Player){
				visible = ((R_Player)m).getSide();
			}
			m.draw(buf, zBuffer, viewport.getWidth(), viewport.getHeight(),
					matrix, lights, side, visible, shadowMap, tileSize);
		}

		return viewport;
	}

	public void setTeam(Team rteam) {
		side = rteam;
	}
}
