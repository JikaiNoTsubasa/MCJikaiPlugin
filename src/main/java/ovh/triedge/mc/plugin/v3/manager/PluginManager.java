package ovh.triedge.mc.plugin.v3.manager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ovh.triedge.mc.plugin.v3.MCJikaiPlugin;
import ovh.triedge.mc.plugin.v3.model.User;
import ovh.triedge.mc.plugin.v3.model.UserList;
import ovh.triedge.mc.plugin.v3.utils.Config;
import ovh.triedge.mc.plugin.v3.utils.Utils;

public class PluginManager implements Listener{

	private UserList users;
	private MCJikaiPlugin plugin;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		p.sendMessage(ChatColor.GREEN + "MCJikaiPlugin 1.19");
		p.sendMessage(ChatColor.GREEN + "Les info sur triedge.ovh");
		Utils.displayTitle(p, ChatColor.AQUA+"Triedge", Config.VERSION_SUB);
		
		if (users != null) {
			if (!users.contains(p.getName())) {
				User u = new User(p.getName());
				users.addUser(u);
			}
		}
	}
	

	public PluginManager(MCJikaiPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	public MCJikaiPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(MCJikaiPlugin plugin) {
		this.plugin = plugin;
	}


	public UserList getUsers() {
		return users;
	}


	public void setUsers(UserList users) {
		this.users = users;
	}
	
	
}
