package ovh.triedge.mc.plugin.v3.manager;

import java.io.IOException;
import java.util.logging.Level;

import ovh.triedge.mc.plugin.v3.MCJikaiPlugin;
import ovh.triedge.mc.plugin.v3.model.UserList;
import ovh.triedge.mc.plugin.v3.model.WarpList;
import ovh.triedge.mc.plugin.v3.utils.Config;
import ovh.triedge.mc.plugin.v3.utils.Storage;

public class ScheduledTask implements Runnable{

	private MCJikaiPlugin plugin;
	
	public ScheduledTask(MCJikaiPlugin plugin) {
		setPlugin(plugin);
	}
	
	@Override
	public void run() {
		UserList list = plugin.getManager().getUsers();
		WarpList warps = plugin.getManager().getWarps();
		try {
			Storage.storeUsers(list);
			plugin.getLogger().log(Level.INFO, "[SHEDULED_TASK] Saved "+list.getUsers().size()+" users to file "+Config.USER_FILE);
			Storage.storeWarps(warps);
			plugin.getLogger().log(Level.INFO, "[SHEDULED_TASK] Saved "+warps.getWarps().size()+" warps to file "+Config.WARP_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public MCJikaiPlugin getPlugin() {
		return plugin;
	}



	public void setPlugin(MCJikaiPlugin plugin) {
		this.plugin = plugin;
	}

}
