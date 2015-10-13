package game.test;

import static org.junit.Assert.*;
import game.logic.*;
import game.logic.world.*;

import org.junit.*;

import renderer.R_AbstractModel;
import renderer.R_Model;

public class LogicTilesTests {

	@Test
	public void basic_test_floor(){
		Player p = new Player("name", 0, 0, 0);
		Tile floor = new BasicFloor(0, 0, null, 0);
		assertTrue(floor.canEnter(p));
		assertTrue(!floor.blockLight());
	}

	@Test
	public void basic_test_wall(){
		Player p = new Player("name", 0, 0, 0);
		Tile wall = new Wall(0, 0, null, 0);
		assertTrue(!wall.canEnter(p));
		assertTrue(wall.blockLight());
	}

	@Test
	public void basic_test_water(){
		Player p = new Player("name", 0, 0, 0);
		Tile water = new Water(0, 0, null, 0);
		assertTrue(water.canEnter(p));
		assertTrue(!water.blockLight());
	}

	@Test
	public void basic_test_door(){
		Player p = new Player("name", 0, 0, 0);
		Door door = new Door(0, 0, 0, 5);
		assertTrue(door.canEnter(p));
		assertFalse(door.blockLight());
		assertTrue(door.getID()==5);
		R_AbstractModel m1 = door.getModel();
		door.setLocked(true);
		R_AbstractModel m2 = door.getModel();
		assertTrue(m1!=m2);
	}

}
