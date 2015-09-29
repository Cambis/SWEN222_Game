package renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import renderer.math.Mat4;
import renderer.math.Vec3;

/**
 * Provides a class for rendering a 3d scene to a buffered image.
 *
 * @author Stephen Thompson
 */
public class Renderer {
	// The map of all models inside the scene
	private HashMap<String, R_AbstractModel> modelMap;

	// The map of all cameras inside the scene
	private HashMap<String, R_AbstractCamera> cameraMap;

	// The currently selected camera to use
	private R_AbstractCamera currentCam;

	// Defines the rendered image's width and height
	private int width;
	private int height;

	/**
	 * Constructs a new Renderer object. The new renderer object has a default camera named "default".
	 *
	 * @param width		the width of the rendered image
	 * @param height	the height of the rendered image
	 */
	public Renderer(int width, int height){
		this.width = width;
		this.height = height;
		this.currentCam = new R_OrthoCamera("default", new Vec3(1, 1, 1), Vec3.Zero(), Vec3.UnitY(),
				  												 	1, 1000, 1, (1.f*width/height));

		modelMap = new HashMap<String, R_AbstractModel>();
		cameraMap = new HashMap<String, R_AbstractCamera>();
		addCamera(currentCam);
	}

	/**
	 * Adds a new model to the scene, returning false if a model with the same name already exists
	 *
	 * @param m		the new model
	 * @return 		whether the model was successfully added or not
	 */
	public boolean addModel(R_AbstractModel m){
		if (modelMap.containsKey(m.getName())){
			return false;
		}
		modelMap.put(m.getName(), m);
		return true;
	}

	/**
	 * Gets the model with the name "name" in the scene
	 *
	 * @param name	the name of the model to get
	 * @return 		the model with the name "name"
	 * 				or null if the model does not exist
	 */
	public R_AbstractModel getModel(String name){
		return modelMap.get(name);
	}

	/**
	 * Removes the model with the passed in name from the scene, returning false if a model does not exist
	 *
	 * @param m		the model's name
	 * @return 		whether the model was successfully removed or not
	 */
	public boolean deleteModel(String name){
		if (modelMap.containsKey(name)){
			return false;
		}
		modelMap.remove(name);
		return true;
	}

	/**
	 * Adds a new camera to the scene, returning false if a camera with the same name already exists
	 *
	 * @param c		the new camera
	 * @return 		whether the camera was successfully added or not
	 */
	public boolean addCamera(R_AbstractCamera c){
		if (cameraMap.containsKey(c.getName())){
			return false;
		}
		cameraMap.put(c.getName(), c);
		return true;
	}

	/**
	 * Removes the camera with the passed in name from the scene, returning false if a camera does not exist
	 *
	 * @param c		the camera's name
	 * @return 		whether the camera was successfully removed or not
	 */
	public boolean deleteCamera(String name){
		if (currentCam.getName().equals(name) || cameraMap.containsKey(name)){
			return false;
		}
		cameraMap.remove(name);
		return true;
	}

	/**
	 * Gets the camera with the name "name" in the scene
	 *
	 * @param name	the name of the camera to get
	 * @return 		the camera with the name "name"
	 * 				or null if the camera does not exist
	 */
	public R_AbstractCamera getCamera(String name){
		return cameraMap.get(name);
	}

	/**
	 * Sets the current camera used by the renderer to render the scene.
	 * Returns true if the camera is successfully set.
	 *
	 * @param name			the camera's name
	 * @return boolean
	 */
	public boolean setCamera(String name){
		if (!cameraMap.containsKey(name)){
			return false;
		}
		currentCam = cameraMap.get(name);
		return true;
	}

	/**
	 * Changes the size of the render.
	 *
	 * @param width		the new width of the render
	 * @param height	the new height of the render
	 */
	public void resize(int width, int height){
		this.width = width;
		this.height = height;
	}

	/**
	 * Renders the scene and returns a buffered image of the render
	 * @return	the BufferedImage of the rendered scene
	 */
	public BufferedImage render(){
		BufferedImage viewport = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		float zBuffer[][] = new float[width][height];

		long timeBefore = System.currentTimeMillis();
		Graphics2D g = (Graphics2D)viewport.getGraphics();
		g.clearRect(0, 0, width, height);

		// Refresh Buffers
		for (int x = 0; x < width; ++x){
			for (int y = 0; y < height; ++y){
				zBuffer[x][y] = Float.MAX_VALUE;
			}
		}

		/** Create Matrix Stack **/
		Mat4 matrixStack = Mat4.createIdentity();
		// Scale the image to fit the window
		matrixStack = matrixStack.mul(Mat4.createScale(width/2, height/2, 1));
		// Move the image to fit inside the window
		matrixStack = matrixStack.mul(Mat4.createTranslate(1, 1, 1));
		// Project image to window coordinates
		matrixStack = matrixStack.mul(currentCam.getProjection());
		// transform image to camera coordinates
		matrixStack = matrixStack.mul(currentCam.getLookAt());

		final Mat4 matrix = matrixStack;
		// Draw Model
		for (R_AbstractModel m : modelMap.values()){
			m.draw(viewport, zBuffer, matrix);
		}

		long timeAfter = 1000/Math.max(1, System.currentTimeMillis()-timeBefore);
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + timeAfter, 25, 25);
		return viewport;
	}
}
