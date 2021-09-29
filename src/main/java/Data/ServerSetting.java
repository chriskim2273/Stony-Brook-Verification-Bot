package Data;

import java.util.ArrayList;
import java.util.LinkedList;

public class ServerSetting {
	
	public static LinkedList<ServerSetting> allServerSettings = new LinkedList<ServerSetting>();
	
	private String serverID;
	private boolean onJoin;
	private boolean channelSpecific;
	private ArrayList<String> channels;
	private ArrayList<String> rolesToGive;
	
	public String getServerID() {
		return serverID;
	}
	public void setServerID(String serverID) {
		this.serverID = serverID;
		MongoDB.saveSettings(this);
	}
	public boolean isOnJoin() {
		return onJoin;
	}
	public void setOnJoin(boolean onJoin) {
		this.onJoin = onJoin;
		MongoDB.saveSettings(this);
	}
	public boolean isChannelSpecific() {
		return channelSpecific;
	}
	public void setChannelSpecific(boolean channelSpecific) {
		this.channelSpecific = channelSpecific;
		MongoDB.saveSettings(this);
	}
	public ArrayList<String> getChannels() {
		return channels;
	}
	public void setChannels(ArrayList<String> channels) {
		this.channels = channels;
		MongoDB.saveSettings(this);
	}
	public ArrayList<String> getRolesToGive() {
		return rolesToGive;
	}
	public void setRolesToGive(ArrayList<String> rolesToGive) {
		this.rolesToGive = rolesToGive;
		MongoDB.saveSettings(this);
	}
	
	public static ServerSetting getServer(String targetID) {
		for(ServerSetting ss: allServerSettings) {
			String id = ss.getServerID();
			if(id.equals(targetID)) {
				return ss;
			}
		}
		return null;
	}
	
	public static boolean onJoinEnabled(String targetID) {
		ServerSetting ss = getServer(targetID);
		if(ss.isOnJoin())
			return true;
		return false;
	}
	
	public ServerSetting(String serverID, boolean onJoin, boolean channelSpecific, ArrayList<String> arrayList, ArrayList<String> arrayList2) {
		this.serverID = serverID;
		this.onJoin = onJoin;
		this.channelSpecific = channelSpecific;
		this.channels = arrayList;
		this.rolesToGive = arrayList2;
		//System.out.println(this.toString());
		allServerSettings.add(this);
		//MongoDB.saveSettings(this);
		//MongoDB
	}
	// When joining a new server, give all the users verified the role. When a new person joins, give them a role if they are verified.
	
	
	// Enable channel-specific
	// Channels to allow.
	
	
	// On join
	// Change Profile command
	// Role to give (allow for multiple)
	// 
}
