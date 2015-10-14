package game.logic;

import renderer.R_Player.Team;

/**
 * @author Callum Gill 300316407 2015
 *
 */
public class SpawnPoint {

	public final double x;
	public final double y;
	public final Room room;
	public final Team team;

	/**
	 * Create spawn point
	 * @param r
	 * 	-	room of spawn point
	 * @param x
	 * 	-	x-position in room (TILE CO-ORDINATE)
	 * @param y
	 * -	y-position in room (TILE CO-ORDINATE)
	 * @param team
	 * 	-	what team spawn belongs to
	 */
	public SpawnPoint(Room r, int x, int y, Team team){
		this.room = r;
		this.x = x*0.2;
		this.y = y*0.2;
		this.team = team;
	}

}
