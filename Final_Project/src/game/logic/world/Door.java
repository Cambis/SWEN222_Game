package game.logic.world;

import java.awt.Color;
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
	public final int doorID, tileNum;
	private boolean locked = false;
	private int keyID = -1;

	private double x, y;

	public Door(double xPos, double yPos, int tileNum, int ID) {
		this.x = xPos;
		this.y = yPos;
		this.doorID = ID;
		this.tileNum = tileNum;
	}

	public void setLocked(boolean val) {
		locked = val;
	}

	public void setKey(int keyID) {
		this.keyID = keyID;
	}

	@Override
	public boolean canEnter(Player p) {
		if (!locked) {
			return true;
		}
		if (p.getSide() == Team.GUARD) {
			return true;
		} else {
			return p.getInventory().contains(new Key(keyID));
		}
	}

	@Override
	public R_AbstractModelData getModelData() {
		return new R_ModelColorData("Door", "res/models/BasicFloor.obj",
				Color.ORANGE);
	}

	@Override
	public R_AbstractModel getModel() {
		return new R_Model("Door " + tileNum, getModelData(),
				new Vec3(x, 0, y), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
	}

	public Room getTargetRoom() {
		return targetRoom;
	}

	public void setTargetRoom(Room targetRoom) {
		this.targetRoom = targetRoom;
	}

	public void setTargetPos(int x, int y) {
		targetX = x * 0.2;
		targetY = y * 0.2;
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
		p.setRoomLoaded(false);
	}

	@Override
	public void onEnter(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit(Player p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final int getID() {
		return this.tileNum;
	}
}
