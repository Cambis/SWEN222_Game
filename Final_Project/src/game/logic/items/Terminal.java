package game.logic.items;

import java.awt.Color;

import game.logic.Player;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import renderer.math.Vec3;

public class Terminal implements Item {

	private Team ownedBy = Team.GUARD;
	private final int SCORE_ON_CAPTURE = 100;
	private int ID;
	private double x, z;
	private R_AbstractModel guardModel;
	private R_AbstractModel spyModel;
	private R_ModelColorData spyModelData = new R_ModelColorData("Terminal", "res/models/terminal.obj", Color.ORANGE.darker());
	private R_ModelColorData guardModelData = new R_ModelColorData("Terminal", "res/models/terminal.obj", Color.BLUE);

	public Terminal(double x, double y){
		setPosition(x,y);
		ID = (int)(x*7*y*3);
		spyModel = new R_Model("Terminal"+x+y, spyModelData,
				new Vec3(x, 0, y), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
		guardModel = new R_Model("Terminal"+x+y, guardModelData,
				new Vec3(x, 0, y), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));

	}

	@Override
	public void setPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	@Override
	/**
	 * Adds 100 points to player if not already captured by team
	 */
	public void interact(Player p) {
		if(p.getSide()!=ownedBy){
			p.addPoints(SCORE_ON_CAPTURE);
			ownedBy = p.getSide();
			System.out.println("YOU HAVE : " + p.getPoints() + " POINTS!!!");
		}
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public R_AbstractModelData getModelData() {
		if(ownedBy==Team.SPY){
			return spyModelData;
		}
		return guardModelData;
	}

	@Override
	public R_AbstractModel getModel() {
		if(ownedBy==Team.SPY){
			return spyModel;
		}
		return guardModel;
	}

	@Override
	public boolean canPickUp(Player player) {
		return false;
	}

}
