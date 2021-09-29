package emailVerification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import Data.MongoDB;
import Data.Profile;
import Data.ServerSetting;
import SBUBot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class privateMessageResponse extends ListenerAdapter{
	
	public static ArrayList<privateMessageResponse> allObjects = new ArrayList<>();
	
	public static privateMessageResponse userExists(String userId) {
		for(privateMessageResponse pmr: allObjects) {
			if(pmr.userID.contentEquals(userId))
				return pmr;
		}
		return null;
	}
	
	public static boolean isNumeric(String str){
	    for (char c : str.toCharArray()){
	        if (!Character.isDigit(c))
	        	return false;
	    }
	    return true;
	}
	
	public static boolean isPin(String message) {
		try {
			@SuppressWarnings("unused")
			int Pin = Integer.parseInt(message);
		}catch(NumberFormatException e){
			return false;
		}
		if(message.length()!=6)
			return false;
		return true;
	}
	
	public boolean checkEmail(String email) {
		if(!email.contains("@stonybrook.edu"))
			return false;
		if(email.contains("+"))
			return false;
	
		Pattern rfc2822 = Pattern.compile(
		        "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
		);

		if (!rfc2822.matcher(email).matches()) {
		    return false;
		}
		
		return true;
	}
	
	public String retrieveNamefromEmail(String Email) {
		String[] Parts = Email.split("\\.|@");
		String Name = capitalizeFirstLetter(Parts[0]) + " " + capitalizeFirstLetter(Parts[1]);
		return Name;
	}
	
	public String capitalizeFirstLetter(String string) {
		return (string.substring(0,1).toUpperCase() + string.substring(1));
	}
	
	public String getServerID() {
		return serverID;
	}

	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	//Make function that makes name upper case
	
	private String userID;
	private int Pin;
	private String Email;
	private boolean emailSent = false;
	private String serverID;
	
	
	
	public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
		User Sender = event.getAuthor();
		String SenderId = Sender.getId();
		
		if(!SenderId.equals(userID))
			return;
		
		String Message = event.getMessage().getContentRaw();
		
		if(emailSent == false) {				
			
			if(checkEmail(Message) == false) {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("Please send a valid Stony Brook Email Address. (remove +'s)");
				event.getChannel().sendMessage(embed.build()).queue();
				return;
			}
			else if(Profile.emailExists(Message)) {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("There is a user that already has this email! If this is an error, please send a message to Teapot#2273 or send an email to sbudiscord@gmail.com.");
				event.getChannel().sendMessage(embed.build()).queue();
				return;
			}
			//Check if email exists already.
			else {
				emailSent = true;
				Email = Message;
				
				// send Email
				mailAPI.sendEmail(Email, Pin);
				
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("A pin has been sent to your SBU email. Please paste it in this chat to verify. Didn't receive an email? Please check your spam or re-use the command in the discord server.");
				event.getChannel().sendMessage(embed.build()).queue();
			}
			
		}
		else if(emailSent == true){
			
			if(isPin(Message) == false) {
				event.getChannel().sendMessage("Invalid Pin").queue();
			}
			else {
				
				if(Message.equals(Integer.toString(Pin))) {
					// Verified
					System.out.println("Correct! Verified.");
					
					EmbedBuilder embed = new EmbedBuilder();
					embed.setTitle("Correct! Verified.");
					event.getChannel().sendMessage(embed.build()).queue();
					
					// Save Email to UserID
					if(Profile.profileExists(userID) == null)
						new Profile(userID,retrieveNamefromEmail(Email),Email);
					else
						MongoDB.saveProfile(Profile.profileExists(userID));
				
					
					//Give Role
					
					Guild Guild = event.getJDA().getGuildById(serverID); 
					ServerSetting ss = ServerSetting.getServer(serverID);
					ArrayList<String> rolesToGive = ss.getRolesToGive();
					for(String role: rolesToGive) {
						Guild.addRoleToMember(userID, Guild.getRoleById(role)).queue();
					}
					//Guild.removeRoleFromMember(userID, Guild.getRoleById("863586957754236939")).queue();
					//userCount.updateCount();
					
					//Set Nickname to name
					
					//SBUGuild.getMember(event.getAuthor()).modifyNickname(retrieveNamefromEmail(Email)).queue();
					//Make it so that they can't change
					
					allObjects.remove(this);
					Bot.jda.removeEventListener(this);
					
				}
				
				//else {}
				
			}
			
		}
	}
	
	public privateMessageResponse(String userID, int Pin) {
		this.userID = userID;
		this.Pin = Pin;
		allObjects.add(this);
	}


}
