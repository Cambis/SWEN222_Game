package game.logic.world;

import java.util.PriorityQueue;
import java.util.Stack;

import game.logic.Player;
import game.logic.items.Item;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import renderer.math.Vec3;

/**
 * @author Callum Gill 300316407 2015
 *
 */
public class Water extends BasicFloor {

	private R_Model model;

	/**
	 * Creates water which slows player and lowers them on z axis
	 * @param xPos xposition
	 * @param yPos yposition
	 * @param data model data to craete model from
	 * @param tileNum tile number
	 */
	public Water(double xPos, double yPos, R_ModelColorData data,
			int tileNum) {
		super(xPos, yPos, data, tileNum);
	}

	@Override
	public void onEnter(Player p) {
		p.multiplySpeed(0.5);
		if(p.getSide()==Team.GUARD){
			p.setZ(-0.09);
		}else{
			p.setZ(-0.2);
		}
	}

	@Override
	public void onExit(Player p) {
		p.multiplySpeed(2);
		p.setZ(0);
	}

}
