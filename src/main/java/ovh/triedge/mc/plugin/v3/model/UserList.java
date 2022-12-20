package ovh.triedge.mc.plugin.v3.model;

import java.util.ArrayList;

public class UserList {

	private ArrayList<User> users = new ArrayList<>();
	
	public boolean contains(String name) {
		for(User u : getUsers()) {
			if (u.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public User getUser(String name) {
		return getUsers().stream().filter(u -> u.getName().equals(name)).findFirst().orElse(null);
	}
	
	public void addUser(User u) {
		getUsers().add(u);
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
}
