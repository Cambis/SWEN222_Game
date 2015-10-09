package game.logic.world;

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

	// ID of the key that opens this door, -1 means that there is no key
	public int keyID;

	public Door() {
		this(-1);
	}

	public Door(int keyID) {
		this.keyID = keyID;
	}

	@Override
	public boolean canEnter(Player player) {
		return true;
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

		if (keyID == -1)
			return true;

		for (Item item : player.getInventory())
			if (item instanceof Key)
				if (keyID == item.getID())
					return true;

		return false;
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
