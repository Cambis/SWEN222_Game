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

	private Room targetRoom;
	private double targetX;
	private double targetY;
	private double direction;
	public final int doorID, tileNum;
	private boolean locked = false;
	private int keyID = -1;
	private R_AbstractModelData lockedModelData = new R_ModelColorData("DoorLocked", "res/models/BasicFloor.obj",
			Color.RED);
	private R_AbstractModelData unlockedModelData = new R_ModelColorData("DoorUnlocked", "res/models/BasicFloor.obj",
			Color.YELLOW);
	private R_AbstractModel lockedModel;
	private R_AbstractModel unlockedModel;

	private double x, y;

	public Door(double xPos, double yPos, int tileNum, int ID) {
		this.x = xPos;
		this.y = yPos;
		this.doorID = ID;
		this.tileNum = tileNum;
		lockedModel = new R_Model("Door " + tileNum, lockedModelData,
				new Vec3(x, 0, y), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
		unlockedModel = new R_Model("Door " + tileNum, unlockedModelData,
				new Vec3(x, 0, y), Vec3.Zero(), new Vec3(0.1f, 0.1f, 0.1f));
	}

	public void setLocked(boolean val) {
		locked = val;
	}

	public void setKey(int keyID) {
		this.keyID = keyID;
	}

	@Override
	public boolean canEnter(Player p) {
		return true;
	}

	@Override
	public R_AbstractModel getModel() {
		if(locked){
			return lockedModel;
		}else{
			return unlockedModel;
		}
	}

	public void setTargetRoom(Room targetRoom) {
		this.targetRoom = targetRoom;
	}

	public void setTargetPos(int x, int y) {
		targetX = x * 0.2;
		targetY = y * 0.2;
	}

	@Override
	public void onInteract(Player p) {
		if(locked && p.getInventory().contains(new Key(keyID))){
			locked=false;
		}

		if(!locked || p.getSide()==Team.GUARD){
			p.setRoom(targetRoom);
			p.setX(targetX);
			p.setY(targetY);
			p.setRoomLoaded(false);
		}

	}

	@Override
	public void onEnter(Player p) {
		//Do nothing
	}

	@Override
	public void onExit(Player p) {
		// Do Nothing
	}

	@Override
	public final int getID() {
		return this.tileNum;
	}

	@Override
	public boolean blockLight() {
		return false;
	}
}
