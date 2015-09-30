package game.model;

public class Wall implements Tile{

	@Override
	public boolean canEnter(Player player) {
		return false;
	}

}
