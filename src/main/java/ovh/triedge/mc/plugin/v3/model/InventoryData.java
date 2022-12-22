package ovh.triedge.mc.plugin.v3.model;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class InventoryData {

	private String name;
	private ArrayList<ItemStack> stacks = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ItemStack> getStacks() {
		return stacks;
	}

	public void setStacks(ArrayList<ItemStack> stacks) {
		this.stacks = stacks;
	}
}
