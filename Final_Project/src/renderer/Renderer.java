package renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import renderer.abstractClasses.R_AbstractCamera;
import renderer.abstractClasses.R_AbstractModel;
import renderer.geometry.Model;

public class Renderer {
	private HashMap<String, R_AbstractCamera> cameraMap;
	private R_AbstractCamera currentCam;

	private int width;
	private int height;
	private Model m;
	float val = 0;

	public Renderer(int width, int height){
		this.width = width;
		this.height = height;
		m = new Model("res/monkey.obj");
	}

	public boolean addModel(R_AbstractModel m){
		return false;
	}

	public boolean deleteModel(){
		return false;
	}

	public boolean addCamera(R_AbstractCamera c){
		return false;
	}

	public boolean deleteCamera(){
		return false;
	}

	public boolean setCamera(){
		return false;
	}

	public void resize(int width, int height){
		this.width = width;
		this.height = height;
	}

	public BufferedImage render(){
		BufferedImage viewport = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		float zBuffer[][] = new float[width][height];

		Graphics2D g = (Graphics2D)viewport.getGraphics();
		g.clearRect(0, 0, width, height);

		// Refresh Buffers
		for (int x = 0; x < width; ++x){
			for (int y = 0; y < height; ++y){
				zBuffer[x][y] = Float.MAX_VALUE;
				//viewport.setRGB(x, y, new Color((float)Math.random(), (float)Math.random(), (float)Math.random()).getRGB());
			}
		}

		// Draw Model
		m.rotate(val);
		val += 0.01f;
		m.draw(viewport, zBuffer);
		return viewport;
	}
}
