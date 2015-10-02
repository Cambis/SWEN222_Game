package game.logic.world;
import game.logic.Player;
import game.logic.Room;
import renderer.R_Model;
import renderer.R_ModelColorData;
import renderer.math.Vec3;

public class Door implements Tile{

	private R_Model model;
	private Room targetRoom;
	private double targetX;
	private double targetY;
	private double direction;

	public Door(){

	}

	@Override
	public boolean canEnter(Player player) {
		return true;
	}

	@Override
	public R_Model getModel() {
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
	public double getY(){
		return targetY;
	}
	public double getDirection(){
		return direction;
	}

}
