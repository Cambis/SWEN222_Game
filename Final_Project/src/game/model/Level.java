package game.model;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Level {

	private List<Room> rooms = new ArrayList<Room>();
	private List<Player> players = new ArrayList<Player>();

	public Level() {

	}

	public Level(String filename){
		loadRooms(filename);
	}

	public void loadRooms(String filename){

		rooms.clear();

		try {
			Scanner s = new Scanner(new File(filename));
			int roomNum = 0;
			while (s.hasNext()) {
				String roomFile = s.nextLine();
				rooms.add(new Room(roomFile));
			}
		} catch (IOException e) {
			System.out.println("Error loading file - IOException : " + e.getMessage());
		}
	}

	/**
	 * Gobbles string in scanner, returns if string found or not and moves scanner along if it does
	 * @param pat
	 * @param s
	 * @return
	 */
	private boolean gobble(String pat, Scanner s){
		if(s.hasNext(pat)){
			s.next();
			return true;
		}
		return false;
	}

	public void addPlayer(Player p){
		players.add(p);
	}

	public boolean removePlayer(Player p) {
		return players.remove(p);
	}

	public void movePlayer(String username, double x, double z, double rot) {
		Player p = getPlayer(username);
		p.setX(x);
		p.setY(z);
	}

	private Player getPlayer(String username) {
		for (Player p : players)
			if (p.getUsername().equals(username))
				return p;

		return null;
	}
}
