package ovh.triedge.mc.plugin.v3.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Utils {
	
	public static float getRequiredXp(int level) {
		return (level * 0.1f) + 40;
	}
	
	public static float getDamage(int level) {
		return (level * 0.15f) + 1;
	}

	public static void displayTitle(Player p, String title, String subtitle) {
		p.sendTitle(title, subtitle, 10, 70, 20);
	}
	
	public static void playSound(Player player, Sound sound) {
		player.getWorld().playSound(player.getLocation(), sound, 10, 1);
	}
	
	public static Player getPlayer(String name) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equals(name))
				return p;
		}
		return null;
	}
	
	public static boolean isDetectable(Material type) {
		return type == Material.DEEPSLATE_DIAMOND_ORE ||
				type == Material.DEEPSLATE_IRON_ORE ||
				type == Material.DEEPSLATE_GOLD_ORE ||
				type == Material.DIAMOND_ORE ||
				type == Material.IRON_ORE ||
				type == Material.GOLD_ORE ||
				type == Material.EMERALD_ORE ||
				type == Material.ANCIENT_DEBRIS ||
				type == Material.NETHERITE_BLOCK ||
				type == Material.LAPIS_ORE ||
				type == Material.ANCIENT_DEBRIS ||
				type == Material.NETHER_GOLD_ORE;
	}
	
	public static void decreaseItemFromInventory(String name, Player player) {
		PlayerInventory inv = player.getInventory();
		for (ItemStack stack : inv.getContents()) {
			if (stack == null)
				continue;
			if (stack.getItemMeta().getDisplayName().equals(name)) {
				int count = stack.getAmount();
				if (count <= 1) {
					inv.remove(stack);
				}else {
					stack.setAmount(count - 1);
				}
				break;
			}
		}
	}
	
	public static void decreaseItemFromInventory(ItemStack stack, Player player) {
		if (stack == null || player == null)
			return;
		PlayerInventory inv = player.getInventory();
		int count = stack.getAmount();
		if (count <= 1) {
			inv.remove(stack);
		}else {
			stack.setAmount(count - 1);
		}
	}
	
	public static long secondsToTicks(int seconds) {
		return (long) seconds * 20L;
	}
}
