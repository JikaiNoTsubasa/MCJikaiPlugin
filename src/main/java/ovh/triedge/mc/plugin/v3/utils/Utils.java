package ovh.triedge.mc.plugin.v3.utils;

import org.bukkit.entity.Player;

public class Utils {

	public static void displayTitle(Player p, String title, String subtitle) {
		p.sendTitle(title, subtitle, 10, 70, 20);
	}
}
