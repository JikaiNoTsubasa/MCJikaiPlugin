package ovh.triedge.mc.plugin.v3.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Utils {

	public static void displayTitle(Player p, String title, String subtitle) {
		p.sendTitle(title, subtitle, 10, 70, 20);
	}
	
	public static void playSound(Player player, Sound sound) {
		player.getWorld().playSound(player.getLocation(), sound, 10, 1);
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
}
