package ovh.triedge.mc.plugin.v3;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import ovh.triedge.mc.plugin.v3.manager.PluginManager;
import ovh.triedge.mc.plugin.v3.manager.ScheduledTask;
import ovh.triedge.mc.plugin.v3.model.UserList;
import ovh.triedge.mc.plugin.v3.model.WarpList;
import ovh.triedge.mc.plugin.v3.utils.Storage;

public class MCJikaiPlugin extends JavaPlugin{

	private PluginManager manager;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean res = false;
		if (sender != null && sender instanceof Player) {
			Player player = (Player) sender;
			String cmd = command.getName();
			switch (cmd) {
			case "detector":
				getManager().onDetectorCommand(player);
				break;
			case "clearsnowball":
				getManager().onClearSnowballs();
				return true;
			/*
			case "inv":
				getManager().onInvCommand(player, args);
				res = true;
				break;
				*/
			}
		}
		return res;
	}
	
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
			WarpList warps = Storage.loadWarps();
			getManager().setWarps(warps);
			getLogger().log(Level.INFO, "Loaded "+warps.getWarps().size()+" warps");
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
