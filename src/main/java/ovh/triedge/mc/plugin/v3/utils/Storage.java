package ovh.triedge.mc.plugin.v3.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ovh.triedge.mc.plugin.v3.model.UserList;
import ovh.triedge.mc.plugin.v3.model.WarpList;

public class Storage {

	public static void storeUsers(UserList list, String path) throws IOException {
		checkPath(path);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter w = new FileWriter(path);
		gson.toJson(list, w);
		w.flush();
		w.close();
	}
	
	public static void storeUsers(UserList list) throws IOException {
		storeUsers(list, Config.USER_FILE);
	}
	
	public static UserList loadUsers(String path) throws IOException {
		File f = new File(path);
		if (!f.isFile()) {
			storeUsers(new UserList(), path);
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileReader r = new FileReader(path);
		UserList list = gson.fromJson(r, UserList.class);
		return list;
	}
	
	public static void storeWarps(WarpList list) throws IOException {
		storeWarps(list, Config.WARP_FILE);
	}
	
	public static void storeWarps(WarpList list, String path) throws IOException {
		checkPath(path);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter w = new FileWriter(path);
		gson.toJson(list, w);
		w.flush();
		w.close();
	}
	
	public static WarpList loadWarps(String path) throws IOException {
		File f = new File(path);
		if (!f.isFile()) {
			storeWarps(new WarpList(), path);
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileReader r = new FileReader(path);
		WarpList list = gson.fromJson(r, WarpList.class);
		return list;
	}
	
	public static WarpList loadWarps() throws IOException {
		return loadWarps(Config.WARP_FILE);
	}
	
	public static UserList loadUsers() throws IOException {
		return loadUsers(Config.USER_FILE);
	}
	
	
	public static void checkPath(String path) {
		File f = new File(path);
		f.getParentFile().mkdirs();
	}
}
