package game.logic;

import renderer.R_Player.Team;

public class SpawnPoint {

	public final double x;
	public final double y;
	public final Room room;
	public final Team team;

	public SpawnPoint(Room r, int x, int y, Team team){
		this.room = r;
		this.x = x*0.1;
		this.y = y*0.1;
		this.team = team;
	}

}
