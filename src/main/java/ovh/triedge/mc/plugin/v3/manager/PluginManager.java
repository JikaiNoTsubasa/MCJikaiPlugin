package ovh.triedge.mc.plugin.v3.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import ovh.triedge.mc.plugin.v3.MCJikaiPlugin;
import ovh.triedge.mc.plugin.v3.model.User;
import ovh.triedge.mc.plugin.v3.model.UserList;
import ovh.triedge.mc.plugin.v3.model.Warp;
import ovh.triedge.mc.plugin.v3.model.WarpList;
import ovh.triedge.mc.plugin.v3.utils.Config;
import ovh.triedge.mc.plugin.v3.utils.Utils;

public class PluginManager implements Listener{

	private UserList users;
	private WarpList warps;
	private MCJikaiPlugin plugin;
	
	public void onWarpCommand(Player player, String[] args) {
		if (args.length > 0) {
			switch(args[0]) {
			case "create":
				actionCreateWarp(player,args);
				break;
			case "delete":
				actionDeleteWarp(player,args);
				break;
			case "list":
				actionListWarp(player,args);
				break;
			}
		}else {
			// Display help
			player.sendMessage(ChatColor.RED+"Il manque un paramètre!");
		}
	}
	
	private void actionCreateWarp(Player player, String[] args) {
		if (args.length < 2) {
			player.sendMessage(ChatColor.RED+"Il manque le nom du TP!");
			return;
		}
		String name = args[1];
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

		if (block.getType().equals(Material.DIAMOND_BLOCK)) {
			Vector vector = player.getLocation().toVector();
			String world = player.getWorld().getName();
			float pitch = player.getLocation().getPitch();
			float yaw = player.getLocation().getYaw();
			Warp warp = new Warp(name);
			warp.setWorld(world);
			warp.setLocationX(vector.getBlockX());
			warp.setLocationY(vector.getBlockY());
			warp.setLocationZ(vector.getBlockZ());
			warp.setPitch(pitch);
			warp.setYaw(yaw);
			warps.addWarp(warp);

			player.sendMessage(ChatColor.GREEN+"Cible de teleportation sauvegardé: "+name);
			getPlugin().getLogger().info("# REGISTER TP: "+name+"["+vector.getBlockX()+"/"+vector.getBlockY()+"/"+vector.getBlockZ()+"]");
		}else {
			player.sendMessage("Vous devez etre sur un block de diamant pour cette commande.");
		}

	}
	
	private void actionDeleteWarp(Player player, String[] args) {
		if (args.length < 2) {
			player.sendMessage(ChatColor.RED+"Il manque le nom du TP!");
			return;
		}
		String name = args[1];
		Warp warp = warps.getWarp(name);
		if (warp == null) {
			player.sendMessage(ChatColor.RED+"TP non trouvé");
			return;
		}

		warps.getWarps().remove(warp);
		player.sendMessage(ChatColor.GREEN+"TP "+name+" supprimé");
	}
	
	private void actionListWarp(Player player, String[] args) {
		if (warps.getWarps().isEmpty()) {
			player.sendMessage("Liste vide!");
			return;
		}
		StringBuilder tmp = new StringBuilder();
		for (Warp warp : warps.getWarps()) {
			String name = warp.getName();
			if (!name.startsWith("h_")) {
				tmp.append(name);
				tmp.append(", ");
			}
		}
		player.sendMessage(tmp.toString());
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event == null)
			return;
		
		// Executes for main hand
				if (event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND) && event.getPlayer().getInventory().getItemInMainHand() != null) {
					if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null)
						onPlayerUseCustomItem(event, event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName());
				}

		// Right click block
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			// If WALL Sign
			Block block = event.getClickedBlock();
			if (block != null && (block.getType() == Material.ACACIA_WALL_SIGN || block.getType() == Material.BIRCH_WALL_SIGN || block.getType() == Material.DARK_OAK_WALL_SIGN || block.getType() == Material.JUNGLE_WALL_SIGN || block.getType() == Material.OAK_WALL_SIGN || block.getType() == Material.SPRUCE_WALL_SIGN))
			{
				Player player = event.getPlayer();
				String[] lines = readSign(block);
				if (lines != null) {
					parseSign(lines[0], player);
				}
			}

		}
	}
	
	private void onPlayerUseCustomItem(PlayerInteractEvent event, String name) {
		// Grenade
		if (name.equals(Custom.GRENADE) && event.getAction() == Action.RIGHT_CLICK_AIR) {
			launchGrenade(event.getPlayer(), 2.0F, Utils.secondsToTicks(3)); // 3s
			event.setCancelled(true);
		// Nuke
		}else if (name.equals(Custom.NUKE) && event.getAction() == Action.RIGHT_CLICK_AIR) {
			launchGrenade(event.getPlayer(), 10.0F, Utils.secondsToTicks(5)); // 5s
			event.setCancelled(true);
		// Ultimate Water
		}else if (name.equals(Custom.ULT_WATER) && event.getAction() == Action.LEFT_CLICK_BLOCK) {
			event.getClickedBlock().setType(Material.WATER);
			event.getPlayer().getWorld().spawnParticle(Particle.WATER_BUBBLE, event.getClickedBlock().getLocation(), 10);
		// Ultimate Water Cancel
		}else if (name.equals(Custom.ULT_WATER) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			event.setCancelled(true);
		}

	}
	
	private void launchGrenade(Player player, float power, long ticks ) {
		// must be player's inventory item
		ItemStack stack = player.getInventory().getItemInMainHand();
		ItemStack stack2 = new ItemStack(stack);
		stack2.setAmount(1); // 20200706.0
		final Item item = player.getWorld().dropItem(player.getEyeLocation(), stack2);
		item.setVelocity(player.getEyeLocation().getDirection());
		//Utils.decreaseItemFromInventory(item.getItemStack().getItemMeta().getDisplayName(), player);
		Utils.decreaseItemFromInventory(stack, player);
		//player.getInventory().removeItem(item.getItemStack());
		final float pow = power;
		// After 3sec
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
			@Override
			public void run() {
				item.getWorld().playEffect(item.getLocation(), Effect.SMOKE, 5);
				item.getWorld().createExplosion(item.getLocation(), pow, false, true); // 20200706.1
				item.remove();
				item.playEffect(EntityEffect.WOLF_SMOKE);
			}
		}, ticks);
	}
	
	@EventHandler
	public void onBlockDamageEvent(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand() != null &&
				player.getInventory().getItemInMainHand().getType() == Material.STICK &&
				player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Custom.ULT_STICK)) {
			Block block = event.getBlock();
			if (block != null) {
				block.breakNaturally();
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item == null)
			return;
		if (item.getItemMeta() == null)
			return;
		String name = item.getItemMeta().getDisplayName();
		if (name.equals(Custom.IMP_GOLD_PICKAXE) ||
				name.equals(Custom.IMP_DIAMOND_PICKAXE) ||
				name.equals(Custom.IMP_GOLD_SHOVEL) ||
				name.equals(Custom.IMP_GOLD_AXE) ||
				name.equals(Custom.IMP_NETHERITE_PICKAXE)) {
			// Manage durability
			int durability = Integer.valueOf(item.getItemMeta().getLore().get(0));
			durability--;
			if (durability <= 0) {
				player.getInventory().remove(item);
				player.sendMessage(ChatColor.RED+item.getItemMeta().getDisplayName()+" est cassé");
			}else {
				Custom.decreaseDurability(item);
			}

			// Manage custom drop for custom pickaxe
			Block block = event.getBlock();
			if (block == null)
				return;
			Material type = block.getType();
			if (type == Material.DIAMOND_ORE ||
					type == Material.DEEPSLATE_DIAMOND_ORE ||
					type == Material.DEEPSLATE_GOLD_ORE ||
					type == Material.DEEPSLATE_EMERALD_ORE ||
					type == Material.DEEPSLATE_IRON_ORE ||
					type == Material.DEEPSLATE_COAL_ORE ||
					type == Material.GOLD_ORE ||
					type == Material.EMERALD_ORE ||
					type == Material.IRON_ORE ||
					type == Material.NETHER_GOLD_ORE ||
					type == Material.NETHER_QUARTZ_ORE ||
					type == Material.NETHERITE_BLOCK ||
					type == Material.COAL_ORE ||
					type == Material.ANCIENT_DEBRIS ||
					type == Material.GLOWSTONE) {
				if (!block.getDrops().isEmpty()) {
					if (name.equals(Custom.IMP_NETHERITE_PICKAXE)) {
						ItemStack stack = new ItemStack(block.getDrops().iterator().next().getType(), 5);
						block.getLocation().getWorld().dropItemNaturally(block.getLocation(), stack);
					}else if (name.equals(Custom.IMP_DIAMOND_PICKAXE)){
						ItemStack stack = new ItemStack(block.getDrops().iterator().next().getType(), 3);
						block.getLocation().getWorld().dropItemNaturally(block.getLocation(), stack);
					}else {
						ItemStack stack = new ItemStack(block.getDrops().iterator().next().getType(), 2);
						block.getLocation().getWorld().dropItemNaturally(block.getLocation(), stack);
					}

				}
			}
		}
	}
	
	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		// Slime appearance
		if (event.getEntityType() == EntityType.SLIME) {
			Slime slime = (Slime) event.getEntity();
			Bukkit.broadcastMessage(ChatColor.ITALIC+""+ChatColor.DARK_PURPLE+"Un Slime est apparu: x:"+slime.getLocation().getX()+" y:"+slime.getLocation().getY()+" z:"+slime.getLocation().getZ());
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event == null)
			return;
		// Custom loot on creepers
		if (event.getEntityType() == EntityType.CREEPER) {
			event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.EMERALD));
		}
	}

	private String[] readSign(Block block) {
		Sign sign = (Sign) block.getState();
		String[] lines = sign.getLines();
		if (lines.length != 0)
			return lines;
		return null;
	}

	private void parseSign(String line, Player player) {
		String[] sp = line.split(":");
		if (sp[0].equalsIgnoreCase("TP")) {
			actionTP(sp[1],player);
		}
	}
	
	private void actionTP(String name, Player player) {
		if (name == null || name == "") {
			return;
		}
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		getPlugin().getLogger().info("### Action TP ###");
		getPlugin().getLogger().info("# "+player.getName());

		if (block.getType().equals(Material.DIAMOND_BLOCK)) {
			warpTo(player, name);
		}else {
			player.sendMessage("Vous devez etre sur un block de diamant pour cette commande.");
		}
	}

	public void warpTo(Player player, String target) {
		if (doesWarpExist(target)) {
			Warp warp = warps.getWarp(target);
			// Get teleport target
			String world_str = warp.getWorld();
			//String group = warp.group;

			float pitch = warp.getPitch();
			float yaw = warp.getYaw();

			World world = Bukkit.getWorld(world_str);
			world.playEffect(player.getLocation(), Effect.SMOKE, 10);
			Location target_loc = new Location(world, warp.getLocationX(),warp.getLocationY(),warp.getLocationZ());
			target_loc.setPitch(pitch);
			target_loc.setYaw(yaw);

			Block block = target_loc.getBlock().getRelative(BlockFace.DOWN);
			if (block != null && block.getType() == Material.DIAMOND_BLOCK) {
				player.teleport(target_loc);
				world.playEffect(player.getLocation(), Effect.SMOKE, 10);
				getPlugin().getLogger().info("# "+player.getName()+" warped to "+target);
			}else {
				player.sendMessage(ChatColor.RED+"La destination n'est pas un block de diamant");
			}

		}else {
			player.sendMessage(ChatColor.RED+"La destination n'existe pas!");
		}
	}

	private boolean doesWarpExist(String name) {
		Warp warp = warps.getWarp(name);
		return warp!=null;
	}
	
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
