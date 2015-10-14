package game.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import renderer.R_AbstractModel;
import renderer.R_AbstractModelData;
import renderer.R_ModelColorData;
import renderer.R_Player.Team;
import game.logic.Player;
import game.logic.items.*;
import static org.junit.Assert.*;

public class ItemTests {

	//Key Tests
	/**
	 * Checks if ID correct and model/data not null
	 */
	@Test
	public void key_test_1(){
		Key k = new Key(1, 2, 3);
		assertTrue(k.getID()==1);
		assertTrue(k.getModel()!=null);
		assertTrue(k.getModelData()!=null);
	}
	/**
	 * Test can be picked up by spy
	 */
	@Test
	public void key_test_2(){
		Key k = new Key(1, 2, 3);
		Player p = new Player("spy", 0, 0, 0);
		p.setSide(Team.SPY);
		assertTrue(k.canPickUp(p));
		k.interact(p);
		assertFalse(p.getInventory().contains(k));
	}
	/**
	 * Test can be picked up by guard
	 */
	@Test
	public void key_test_3(){
		Key k = new Key(1, 2, 3);
		Player p = new Player("g", 0, 0, 0);
		p.setSide(Team.GUARD);
		assertFalse(k.canPickUp(p));
		k.interact(p);
		assertFalse(p.getInventory().contains(k));
	}

	/**
	 * Test equals method
	 */
	@Test
	public void key_test_4(){
		Key k1 = new Key(1);
		Key k2 = new Key(1);
		Key k3 = new Key(2);
		assertTrue(k1.equals(k2));
		assertFalse(k1.equals(k3));
		assertFalse(k1.equals(null));
	}

	//Chest tests

	/**
	 * Checks if player can pickup
	 */
	@Test
	public void chest_test_1(){
		Chest c = new Chest(new Key(1), 0, 0);
		Player p = new Player("spy", 0, 0, 0);
		assertFalse(c.canPickUp(p));
		assertTrue(c.getID()==0);
	}

	/**
	 * Checks if chest changes model when opened
	 */
	@Test
	public void chest_test_2(){
		Chest c = new Chest(new Key(1), 0, 0);
		Player p = new Player("spy", 0, 0, 0);
		R_AbstractModel m1 = c.getModel();
		R_AbstractModelData md1 = c.getModelData();
		R_AbstractModel m2 = c.getModel();
		R_AbstractModelData md2 = c.getModelData();
		//Check data and model the same
		assertTrue(m1==m2);
		assertTrue(md1==md2);
		c.interact(p);
		m2 = c.getModel();
		md2 = c.getModelData();
		//Check both model and data updated
		assertTrue(m1!=m2);
		assertTrue(md1!=md2);
	}

	/**
	 * Checks if player gets item from chest and chest does not keep item
	 */
	@Test
	public void chest_test_3(){
		Chest c = new Chest(new Key(1), 0, 0);
		Key key = new Key(1);
		Player p = new Player("spy", 0, 0, 0);
		Player p2 = new Player("spy", 0, 0, 0);
		//Check player doesn't already have key
		assertFalse(p.getInventory().contains(key));
		assertFalse(p2.getInventory().contains(key));
		c.interact(p);
		c.interact(p2);
		//Check player obtained key from chest
		assertTrue(p.getInventory().contains(key));
		//Check player 2 did not obtain key after
		assertFalse(p2.getInventory().contains(key));
	}

	/**
	 * Checks to make sure guard can't open chest
	 * And checks item stays in chest afterwards
	 */
	@Test
	public void chest_test_4(){
		Chest c = new Chest(new Key(1), 0, 0);
		Key key = new Key(1);
		Player p = new Player("g", 0, 0, 0);
		Player p2 = new Player("spy", 0, 0, 0);
		p.setSide(Team.GUARD);
		p2.setSide(Team.SPY);
		//Check player doesn't already have key
		assertFalse(p.getInventory().contains(key));
		c.interact(p);
		c.interact(p2);
		//Check player obtained key from chest
		assertFalse(p.getInventory().contains(key));
		assertTrue(p2.getInventory().contains(key));
	}

	/**
	 * Checks if can create chest from list of items
	 */
	@Test
	public void chest_test_5(){
		List<Item> items = new ArrayList<Item>();
		items.add(new Key(1));
		items.add(new Key(2));
		Chest c = new Chest(items, 0, 0);
		Key key = new Key(1);
		Player p = new Player("spy", 0, 0, 0);
		Player p2 = new Player("spy", 0, 0, 0);
		p.setSide(Team.SPY);
		//Check player doesn't already have key
		assertTrue(p.getInventory().isEmpty());
		assertTrue(p2.getInventory().isEmpty());
		c.interact(p);
		c.interact(p2);
		//Check player obtained key from chest
		assertTrue(p.getInventory().size()==2);
		assertTrue(p2.getInventory().isEmpty());

	}

	//Terminal Tests

	/**
	 * Checks if Terminal can be picked up
	 */
	@Test
	public void terminal_test_1(){
		Terminal t = new Terminal(0, 0);
		Player p = new Player("spy", 0, 0, 0);
		assertFalse(t.canPickUp(p));
		assertTrue(t.getID()==0);
	}

	/**
	 * Checks if Terminal capture logic and point allocation system
	 */
	@Test
	public void terminal_test_2(){
		Terminal t = new Terminal(0, 0);
		Player p = new Player("g", 0, 0, 0);
		Player p2 = new Player("spy", 0, 0, 0);
		p.setSide(Team.GUARD);
		p2.setSide(Team.SPY);
		t.interact(p2);
		//Check captured and given points to spy
		assertTrue(p2.getPoints()==100);
		t.interact(p);
		//Check recaptured and given points to guard
		assertTrue(p.getPoints()==100);
		t.interact(p);
		//Check if attempted recapture no points given
		assertTrue(p.getPoints()==100);
	}

	/**
	 * Test if model changes on capture
	 */
	@Test
	public void terminal_test_3(){
		Terminal t = new Terminal(0, 0);
		Player p = new Player("g", 0, 0, 0);
		Player p2 = new Player("spy", 0, 0, 0);
		p.setSide(Team.GUARD);
		p2.setSide(Team.SPY);
		R_AbstractModel m1 = t.getModel();
		R_AbstractModelData md1 = t.getModelData();
		R_AbstractModel m2 = t.getModel();
		R_AbstractModelData md2 = t.getModelData();
		//Check data and model the same
		assertTrue(m1==m2);
		assertTrue(md1==md2);
		t.interact(p2);
		m2 = t.getModel();
		md2 = t.getModelData();
		//Check both model and data updated
		assertTrue(m1!=m2);
		assertTrue(md1!=md2);
		//Check changes back to original (guard side model)
		t.interact(p);
		m2 = t.getModel();
		md2 = t.getModelData();
		assertTrue(m1==m2);
		assertTrue(md1==md2);
	}

}
