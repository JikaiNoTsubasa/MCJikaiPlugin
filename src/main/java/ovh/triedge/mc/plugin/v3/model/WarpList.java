package ovh.triedge.mc.plugin.v3.model;

import java.util.ArrayList;


public class WarpList {

	private ArrayList<Warp> warps = new ArrayList<>();

	public ArrayList<Warp> getWarps() {
		return warps;
	}

	public void setWarps(ArrayList<Warp> warps) {
		this.warps = warps;
	}
	
	public Warp getWarp(String name) {
		for (Warp w : getWarps())
			if (w.getName().equals(name))
				return w;
		return null;
	}
	
	public void addWarp(Warp warp) {
		// Verify if warp exist, remove it
		Warp tmp = getWarp(warp.getName());
		if (tmp != null) {
			getWarps().remove(tmp);
		}
		getWarps().add(warp);
	}
}
