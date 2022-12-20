package ovh.triedge.mc.plugin.v3.model;

public class User {

	private String name;
	private MagicData magicData = new MagicData();
	
	public User(String name) {
		setName(name);
	}

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
}
