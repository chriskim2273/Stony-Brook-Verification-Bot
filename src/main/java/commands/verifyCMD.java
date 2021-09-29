package commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import SBUBot.Bot;
import emailVerification.privateMessageResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import Data.Profile;
import Data.ServerSetting;

public class verifyCMD extends ListenerAdapter{
	
	public String formattedDate() {
		LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    return myDateObj.format(myFormatObj);
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User user = event.getAuthor();
		if(user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		String command = "!verify";
		String raw = event.getMessage().getContentRaw();
		
		//System.out.println("WTF");
		
		if(raw.equalsIgnoreCase(command)){
			
			
			//Add checking to see if channel-specific is enabled and if so check if the channel is one of them.
			String serverID = event.getGuild().getId();
			ServerSetting ss = ServerSetting.getServer(serverID);
			
			if(ss == null)
				return;
			
			if(ss.getRolesToGive().size() == 0) {
				// Tell them you must specify which roles to give!!
				event.getChannel().sendMessage("Server admins must specify which roles to give before this command can be used! {!setroles (mention roles)}").queue();
				return;
			}
			
			//Check if the message is sent in one of the designated channels and also if such channels are even designated
			//in the first place
			if(ss.isChannelSpecific()){
				if(ss.getChannels().size() == 0)
					return;
				if(!ss.getChannels().contains(event.getChannel().getId()))
					return;
			}
			
			if(Profile.profileExists(event.getAuthor().getId()) != null) {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("You are already verified! If there is any issue, please send a message to Teapot#2273 or send an email to sbudiscord@gmail.com.");
				event.getChannel().sendMessage(embed.build()).queue();
				return;
			}
			
			/*
			//Check if it is the verify channel
			if(!event.getChannel().getId().equals("863586772650950686"))
				return;
			*/
			
			if(privateMessageResponse.userExists(user.getId()) != null) {
				privateMessageResponse pmr = privateMessageResponse.userExists(user.getId());
				privateMessageResponse.allObjects.remove(pmr);
 				System.gc();
				Bot.jda.removeEventListener(pmr);
			}
			
			TextChannel channel = event.getChannel();
			
			// Generate the pin randomly.
			int Pin = (int)(Math.random() * (999999 - 99999 + 1) + 99999);
			
			System.out.println("Pin: " + Pin);
			
			EmbedBuilder embed = new EmbedBuilder();
			
			embed.setTitle("Please Send Your SBU Email Address");
			embed.setDescription("You can access your SBU email by going to the link: stonybrook.edu/mycloud");
			try {
				event.getJDA().openPrivateChannelById(user.getId()).complete().sendMessage(embed.build()).queue();
			}
			catch(ErrorResponseException e) {
				embed.setTitle("Unable to send user a private message.");
				channel.sendMessage(embed.build()).queue();
				return;
			}
			embed.clear();
			embed.setTitle("Please Check your DMs for next steps. Please be aware that the whole operation will expire in 5 minutes.");
			//embed.setDescription("You can access your SBU email by going to the link: stonybrook.edu/mycloud");
			channel.sendMessage(embed.build()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
			//Create class object that waits for a response.
			//Timer for five minutes
			privateMessageResponse pmr = new privateMessageResponse(user.getId(), Pin);
			Bot.jda.addEventListener(pmr);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					if(privateMessageResponse.userExists(user.getId()) != null) {
						//message.delete().queue();
						//event.getChannel().sendMessage(event.getMember().getAsMention() + ", you did not respond within 15 seconds. Cancelling command.").queue();
						Bot.jda.removeEventListener(pmr);
						embed.clear();
						embed.setTitle("The operation has expired as 5 minutes has passed. Please re-run the command");
						event.getJDA().openPrivateChannelById(user.getId()).complete().sendMessage(embed.build()).queue();
					}
					else
						return;
						
				}
				
			}, 300000); // 5 minutes = 300000 ms
		}
		
	}
}