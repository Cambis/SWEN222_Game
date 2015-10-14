package game.test;

import static org.junit.Assert.*;
import game.logic.*;
import game.logic.items.Key;
import game.logic.world.*;

import org.junit.*;

import renderer.R_AbstractModel;
import renderer.R_Model;
import renderer.R_Player.Team;

public class LogicTilesTests {

	/**
	 * Tests blocking of light and if player can enter
	 */
	@Test
	public void basic_tests_floor(){
		Player p = new Player("name", 0, 0, 0);
		Tile floor = new BasicFloor(0, 0, null, 0);
		assertTrue(floor.canEnter(p));
		assertTrue(!floor.blockLight());
	}

	/**
	 * Tests blocking of light and if player can enter
	 */
	@Test
	public void basic_tests_wall(){
		Player p = new Player("name", 0, 0, 0);
		Tile wall = new Wall(0, 0, null, 5);
		assertTrue(!wall.canEnter(p));
		assertTrue(wall.blockLight());
		assertTrue(wall.getModel()!=null);
		assertTrue(wall.getID()==5);
	}

	/**
	 * Tests blocking of light and if player can enter
	 */
	@Test
	public void basic_tests_blanktile(){
		Player p = new Player("name", 0, 0, 0);
		Tile blank = new BlankTile();
		assertTrue(!blank.canEnter(p));
		assertTrue(!blank.blockLight());
		assertTrue(blank.getModel()==null);
		assertTrue(blank.getID()==0);
	}

	/**
	 * Tests blocking of light and if player can enter
	 */
	@Test
	public void basic_tests_water(){
		Player p = new Player("name", 0, 0, 0);
		Tile water = new Water(0, 0, null, 0);
		assertTrue(water.canEnter(p));
		assertTrue(!water.blockLight());
	}

	/**
	 * Test water interaction with spy
	 */
	@Test
	public void water_interaction_spy_test(){
		Player spy = new Player("s", 0, 0, 0);
		spy.setSide(Team.SPY);
		//Test spy water interaction
		waterInteractionTest(spy);
	}

	/**
	 * Test water interaction with player
	 */
	@Test
	public void water_interaction_guar_test(){
		Player guard = new Player("g", 0, 0, 0);
		guard.setSide(Team.GUARD);
		//Test guard water interaction
		waterInteractionTest(guard);
	}

	private void waterInteractionTest(Player p){
		double z1 = p.getZ();
		Tile water = new Water(0, 0, null, 0);
		water.onEnter(p);
		double z2 = p.getZ();
		assertTrue(z1>z2);
		water.onExit(p);
		z2 = p.getZ();
		assertTrue(z1==z2);
	}

	@Test
	public void basic_tests_door(){
		Player p = new Player("name", 0, 0, 0);
		Door door = new Door(0, 0, 5, 0);
		//Checks if player can enter
		assertTrue(door.canEnter(p));
		//Checks if blocks light
		assertFalse(door.blockLight());
		//Checks door id
		assertTrue(door.getID()==5);
		//Checks if model changes when locked vs unlocked
		R_AbstractModel m1 = door.getModel();
		door.setLocked(true);
		R_AbstractModel m2 = door.getModel();
		assertTrue(m1!=m2);
	}

	@Test
	public void door_destination_tests(){
		Player p = new Player("name", 0, 0, 0);
		Door door = new Door(0, 0, 5, 0);
		Room r1 = new Room();
		Room r2 = new Room();
		p.setRoom(r1);
		createDoorDest(door, r2, 10, 5);
		assertTrue(p.getRoom()==r1);
		door.onInteract(p);
		assertTrue(p.getRoom()==r2);
		assertTrue(p.getX()==10*0.2);

		assertTrue(p.getY()==5*0.2);
	}
	@Test
	public void door_locked_interaction_test(){
		Player spy = new Player("spy", 0, 0, 0);
		Player spy2 = new Player("spy", 0, 0, 0);
		spy.setSide(Team.SPY);
		spy2.setSide(Team.SPY);
		Player guard = new Player("guard", 0, 0, 0);
		guard.setSide(Team.GUARD);
		Door door = new Door(0, 0, 5, 0);
		Room r1 = new Room();
		Room r2 = new Room();
		spy.setRoom(r1);
		guard.setRoom(r1);
		createDoorDest(door, r2, 10, 5);
		door.setKey(1);
		door.setLocked(true);
		door.onInteract(spy);
		door.onInteract(guard);
		//Check spy can not use locked door
		assertTrue(spy.getRoom()==r1);
		//Check guard can use locked door
		assertTrue(guard.getRoom()==r2);
		spy.addItem(new Key(1));
		door.onInteract(spy);
		door.onInteract(spy2);
		//Check spy with correct key can use locked door
		assertTrue(spy.getRoom()==r2);
		//Check door unlocked after spy with key used it
		assertTrue(spy2.getRoom()==r2);
	}

	private void createDoorDest(Door d, Room dest, int x, int y){
		d.setTargetRoom(dest);
		d.setTargetPos(x, y);
	}

}
