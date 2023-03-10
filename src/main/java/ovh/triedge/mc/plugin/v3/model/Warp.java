package ovh.triedge.mc.plugin.v3.model;

public class Warp {

	private String name, world;
	private int locationX, locationY, locationZ;
	private float pitch, yaw;
	
	public Warp() {
	}
	
	public Warp(String name) {
		setName(name);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	public int getLocationX() {
		return locationX;
	}
	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}
	public int getLocationY() {
		return locationY;
	}
	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
	public int getLocationZ() {
		return locationZ;
	}
	public void setLocationZ(int locationZ) {
		this.locationZ = locationZ;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
}
