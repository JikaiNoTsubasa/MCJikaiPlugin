package ovh.triedge.mc.plugin.v3.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ovh.triedge.mc.plugin.v3.MCJikaiPlugin;
import ovh.triedge.mc.plugin.v3.model.User;
import ovh.triedge.mc.plugin.v3.model.UserList;
import ovh.triedge.mc.plugin.v3.model.WarpList;
import ovh.triedge.mc.plugin.v3.utils.Config;
import ovh.triedge.mc.plugin.v3.utils.Utils;

public class PluginManager implements Listener{

	private UserList users;
	private WarpList warps;
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
	
	public void onDetectorCommand(Player player) {
		ArrayList<Block> blocks = new ArrayList<>();
		int maxDist = 20;
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		Location loc = block.getLocation();
		player.sendMessage(ChatColor.GOLD+"Recherche avec le detecteur:");
		int startX = block.getX() -(maxDist/2);
		int startY = block.getY() -2;
		int startZ = block.getZ() -(maxDist/2);

		int endX = block.getX() +(maxDist/2);
		int endY = block.getY() +2;
		int endZ = block.getZ() +(maxDist/2);

		// 20200706.4
		for (int x = startX; x <= endX; ++x) {
			for (int y = startY; y <= endY; ++y) {
				for (int z = startZ; z <= endZ; ++z) {
					loc.setX(x);
					loc.setY(y);
					loc.setZ(z);
					Material type = loc.getBlock().getType();
					if (Utils.isDetectable(type)) {
						blocks.add(loc.getBlock());
					}
				}
			}
		}
		if (blocks.isEmpty())
			player.sendMessage(ChatColor.DARK_PURPLE+"Rien trouvé");
		else {
			for (Block b : blocks) {
				String name = b.getType().toString();
				if (b.getType() == Material.DIAMOND_ORE || b.getType() == Material.DEEPSLATE_DIAMOND_ORE)
					player.sendMessage(ChatColor.AQUA+name+" -> X:"+b.getX()+" Y:"+b.getY()+" Z:"+b.getZ());
				else
					player.sendMessage(ChatColor.GREEN+name+" -> X:"+b.getX()+" Y:"+b.getY()+" Z:"+b.getZ());
					
			}
		}
	}
	
	public void onClearSnowballs() {
		List<World> worlds = getPlugin().getServer().getWorlds();
		for (World world : worlds) {
			List<Entity> entList = world.getEntities();//get all entities in the world
			 for(Entity current : entList){//loop through the list
		            if (current instanceof Snowball){//make sure we aren't deleting mobs/players
		            	current.remove();//remove it
		            }
			 }
		}
	}
	
	/*
	
	public void onInvCommand(Player p, String[] args) {
		if (args.length>0) {
			String cmd = args[0];
			switch (cmd) {
				case "list":
					listInventory(p);
					break;
				default:
					openInventory(p, cmd);
					break;
			}
		}
	}
	
	private void openInventory(Player p, String name) {		
		Inventory inv = Bukkit.createInventory(p, InventoryType.CHEST);
		User u = users.getUser(p.getName());
		InventoryData i = u.getInventory(name);
		if (i == null) {
			p.sendMessage(ChatColor.RED+"Pas d'inventaire à ce nom. Création d'un nouveau.");
			ItemStack[] stacks = new ItemStack[0];
			inv.setContents(stacks);
		}else {
			ItemStack[] stacks = new ItemStack[i.getStacks().size()];
			int idx = 0;
			for (ItemStack stack : stacks) {
				stacks[idx] = stack;
				idx++;
			}
			inv.setContents(stacks);
		}
		Utils.playSound(p, Sound.BLOCK_CHEST_OPEN);
	}


	private void listInventory(Player p) {
		User u = users.getUser(p.getName());
		p.sendMessage(ChatColor.GREEN+"Inventories:");
		String invs = "";
		for (InventoryData i : u.getInventories()) {
			invs+=i.getName()+", ";
		}
		p.sendMessage(ChatColor.GREEN+invs);
	}
	*/
	

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

	public WarpList getWarps() {
		return warps;
	}

	public void setWarps(WarpList warps) {
		this.warps = warps;
	}
	
	
}
