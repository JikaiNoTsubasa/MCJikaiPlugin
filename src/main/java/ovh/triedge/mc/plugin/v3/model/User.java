package ovh.triedge.mc.plugin.v3.model;

public class User {

	private String name;
	private MagicData magicData = new MagicData();
	//private ArrayList<InventoryData> inventories = new ArrayList<>();
	
	public User(String name) {
		setName(name);
	}
	/*
	public InventoryData getInventory(String name) {
		return inventories.stream().filter(i -> i.getName().equals(name)).findFirst().orElse(null);
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MagicData getMagicData() {
		return magicData;
	}

	public void setMagicData(MagicData magicData) {
		this.magicData = magicData;
	}

	/*
	public ArrayList<InventoryData> getInventories() {
		return inventories;
	}

	public void setInventories(ArrayList<InventoryData> inventories) {
		this.inventories = inventories;
	}
	*/
}
