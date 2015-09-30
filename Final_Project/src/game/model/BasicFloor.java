package game.model;

public class BasicFloor implements Tile{

	@Override
	public boolean canEnter(Player player) {
		return true;
	}

}
