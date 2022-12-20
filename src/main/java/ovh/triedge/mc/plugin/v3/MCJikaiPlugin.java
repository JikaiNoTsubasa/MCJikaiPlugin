package ovh.triedge.mc.plugin.v3;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import ovh.triedge.mc.plugin.v3.manager.PluginManager;
import ovh.triedge.mc.plugin.v3.manager.ScheduledTask;
import ovh.triedge.mc.plugin.v3.model.UserList;
import ovh.triedge.mc.plugin.v3.utils.Storage;

public class MCJikaiPlugin extends JavaPlugin{

	private PluginManager manager;
	
	@Override
	public void onDisable() {
		super.onDisable();
		getServer().getScheduler().cancelTasks(this);
	}
	
	@Override
	public void onEnable() {
		getLogger().log(Level.INFO,"Enable MCJikaiPlugin");
		super.onEnable();
		
		// Init and load users info
		initPluginManager();
		
		getServer().getPluginManager().registerEvents(getManager(), this);
		getLogger().log(Level.INFO,"Manager registered");
		
		// Scheduled tasks
		BukkitScheduler scheduler = getServer().getScheduler();
		int res = scheduler.scheduleSyncRepeatingTask(this, new ScheduledTask(this), 1000L, 6000L);
		if (res == -1)
			getLogger().log(Level.SEVERE, "Cannot schedule SaveTask");
	}
	
	
	public void initPluginManager() {
		getLogger().log(Level.INFO, "Initialize plugin manager...");
		setManager(new PluginManager(this));
		getLogger().log(Level.INFO, "Loading users into memory...");
		try {
			UserList list = Storage.loadUsers();
			getManager().setUsers(list);
			getLogger().log(Level.INFO, "Loaded "+list.getUsers().size()+" users");
		} catch (IOException e) {
			e.printStackTrace();
		}
		getLogger().log(Level.INFO, "Plugin manager initialized");
	}

	public PluginManager getManager() {
		return manager;
	}

	public void setManager(PluginManager manager) {
		this.manager = manager;
	}
}
