package Data;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
	private static String password = "";
	
	public static String uri = "mongodb+srv://eucliduser:" + password + "@euclidbot.qvjvz.mongodb.net/<dbname>?retryWrites=true&w=majority";
	public static MongoClientURI clientURI = new MongoClientURI(uri);
	public static MongoClient mongoClient = new MongoClient(clientURI);
	public static MongoDatabase sbuDatabase = mongoClient.getDatabase("SBU");
	public static MongoCollection<Document> ProfileData = sbuDatabase.getCollection("Profiles");
	public static MongoCollection<Document> SettingsData = sbuDatabase.getCollection("Server-Settings");
	//public static DBCollection Settings;
	
	//Create new database for each server.
	
	public MongoDB() throws UnknownHostException {
		clientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(clientURI);
		sbuDatabase = mongoClient.getDatabase("SBU");
		ProfileData = sbuDatabase.getCollection("Profiles"); //Will not need due to collections for each server.
		SettingsData = sbuDatabase.getCollection("Server-Settings");
		//Settings = customDatabase.collect
	}
	

	@SuppressWarnings("unchecked")
	public static void retrieveSettings(){
		FindIterable<Document> ServerSettings = SettingsData.find();
		MongoCursor<Document> cursor = ServerSettings.cursor();
		while(cursor.hasNext()) {
			Document next = (Document) cursor.next();
			System.out.println(next);
			// new
			try {
				new ServerSetting(next.get("ServerID").toString(), (boolean) next.get("onJoin"), (boolean) next.get("channelSpecific"), (ArrayList<String>)next.get("channels"), (ArrayList<String>)next.get("rolesToGive"));
			}catch(NullPointerException NPE) {}
		}
		System.out.println(ServerSetting.allServerSettings.toString());
	}
	
	public static void saveSettings(ServerSetting server) {
		Document oldServerSettings = (Document) SettingsData.find(new Document("ServerID",server.getServerID())).first();
		//System.out.println(oldServerSettings.toString());
		Document newServerSettings = convertServerSetting(server);

		update(SettingsData, oldServerSettings, newServerSettings);
	}
	
	public static void retrieveProfiles() {
		FindIterable<Document> Profiles = ProfileData.find();
		MongoCursor<Document> cursor = Profiles.cursor();
		while(cursor.hasNext()) {
			Document next = (Document) cursor.next();
			System.out.println(next);
			new Profile(next.get("UserID").toString(),next.get("Name").toString(),next.get("Email").toString());
		}
		System.out.println(Profile.allProfiles.toString());
	}
	
	public static void update(MongoCollection<Document> Data, Document oldDocument, Document newDocument) {
		Bson updateOperation = new Document("$set", newDocument);
		
		if(oldDocument == null) {
			Data.insertOne(newDocument);
		}else {
			Data.updateOne(oldDocument, updateOperation);
		}
	}
	
	public static void saveProfile(Profile player) {
		Document oldProfile = (Document) ProfileData.find(new Document("UserID",player.returnUserID())).first();//new BasicDBObject("userID",player.getUserID());
		Document newProfile = convertProfile(player);

		update(ProfileData, oldProfile, newProfile);
	}
	
	public static Document convertProfile(Profile profile) {
		return new Document("UserID", profile.returnUserID())
				.append("Name", profile.returnName())
				//.append("Birthday", profile.returnBirthday())
				.append("Email", profile.returnEmail());
	}
	
	public static Document convertServerSetting(ServerSetting server) {
		return new Document("ServerID", server.getServerID())
				.append("onJoin", server.isOnJoin())
				.append("channelSpecific", server.isChannelSpecific())
				.append("channels", server.getChannels())
				.append("rolesToGive", server.getRolesToGive());
	}
}
