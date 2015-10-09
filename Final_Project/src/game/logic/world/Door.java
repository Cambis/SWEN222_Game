package game.logic.world;

import java.util.PriorityQueue;
import java.util.Stack;

import game.logic.Player;
import game.logic.Room;
import game.logic.items.Item;
import game.logic.items.Key;
import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import renderer.math.Vec3;

public class Door implements Tile {

	private R_Model model;
	private Room targetRoom;
	private double targetX;
	private double targetY;
	private double direction;
	public final int doorID;
	private boolean locked = false;
	private int keyID = -1;


	public Door(double xPos, double yPos, R_ModelColorData data,
			int tileNum, int ID) {
		model = new R_Model("Door" + tileNum, data, new Vec3(xPos, 0,
				yPos), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
		doorID = ID;
	}

	public void setLocked(boolean val){
		locked =val;
	}

	public void setKey(int keyID){
		this.keyID = keyID;
	}

	@Override
	public boolean canEnter(Player p) {
		if(!locked){
			return true;
		}
		if(p.getSide()==Team.GUARD){
			return true;
		}else{
			return p.getInventory().contains(new Key(keyID));
		}
	}

	@Override
	public R_AbstractModelData getModelData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R_AbstractModel getModel() {
		return model;
	}

	public Room getTargetRoom() {
		return targetRoom;
	}

	public void setTargetRoom(Room targetRoom) {
		this.targetRoom = targetRoom;
	}

	public void setTargetPos(int x, int y) {
		targetX = x;
		targetY = y;
	}


	public double getX() {
		return targetX;
	}

	public double getY() {
		return targetY;
	}

	public double getDirection() {
		return direction;
	}

	@Override
	public boolean canInteract(Player player) {
		return true;
	}

	@Override
	public void onInteract(Player p) {
		p.setRoom(targetRoom);
		p.setX(targetX);
		p.setY(targetY);
	}

	@Override
	public void onEnter(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit(Player p) {
		// TODO Auto-generated method stub

	}
}
