import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_OrthoCamera;
import renderer.R_PerspectiveCamera;
import renderer.R_Player;
import renderer.Renderer;
import renderer.math.Vec3;


public class RendererTestWindow {
	Renderer r;

	public RendererTestWindow(){
		//1. Create the frame.
		JFrame frame = new JFrame("FrameDemo");

		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//3. Create components and put them in the frame.
		//...create emptyLabel...
		r = new Renderer(1024, 768);

		// Set up the orthographic camear
		R_OrthoCamera cam = new R_OrthoCamera("main", new Vec3(-25, 10, 25), Vec3.Zero(), Vec3.UnitY(), 1, 1000, 2);
		r.addCamera(cam);
		r.setCamera(cam.getName());

		// Create a player Model
		R_ModelColorData playModData = new R_ModelColorData("P1", "res/Guard.obj", new Color(0.3f, 1f, 0.5f));
		r.addModelData(playModData);
		R_Player playerMod = new R_Player("P1", playModData, R_Player.Team.GUARD, Vec3.Zero(), new Vec3(0, 110, 0), new Vec3(0.15f, 0.15f, 0.15f));
		r.addModel(playerMod);

		// Create a static Model
		R_ModelColorData sceneModData = new R_ModelColorData("T", "res/testLvL.obj", new Color(1f, 0.6f, 0.5f));
		r.addModelData(sceneModData);
		R_Model m2 = new R_Model("Scene", sceneModData, new Vec3(1.5f, 0, 0f), Vec3.Zero(), new Vec3(1.f, 1.f, 1.f));
		r.addModel(m2);

		// Updates the player and renders the scene
		@SuppressWarnings("serial")
		JPanel p = new JPanel(){
			float val = 0;

			public void paintComponent(Graphics g){
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;

				// Set Orientation
				Vec3 rot = playerMod.getOrientation();
				rot = new Vec3(rot.getX(), val,rot.getZ());
				playerMod.setOrientation(rot);

				// Set Position
				Vec3 pos = playerMod.getPosition();
				pos = new Vec3(pos.getX(), (float) (Math.sin(val)), pos.getZ());
				playerMod.setPosition(pos);

				val += 0.01;

				// Render to screen
				g2d.drawImage(r.render(), null, 0, 0);
			}
		};
		
		p.setSize(512, 512);
		p.setPreferredSize(new Dimension(1024, 768));

		frame.getContentPane().add(p, BorderLayout.CENTER);

		// Resizes render image when the frame resizes
		frame.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	r.resize(p.getWidth(), p.getHeight());
            }
        });
		//4. Size the frame.
		frame.pack();

		//5. Show it.
		frame.setVisible(true);

		// Repeatable draw the model
		while (true){
			frame.repaint();
			try {
			   Thread.sleep(1000/100);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}

	public static void main(String[] args) {
		new RendererTestWindow();
	}

}
