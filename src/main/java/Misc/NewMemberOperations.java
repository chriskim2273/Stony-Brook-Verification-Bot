package Misc;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;

import Data.Profile;
import Data.ServerSetting;
import SBUBot.Bot;
import emailVerification.privateMessageResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NewMemberOperations extends ListenerAdapter{
	
	public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
		
		String serverID = event.getGuild().getId();
		ServerSetting ss = ServerSetting.getServer(serverID);
		
		if(ss == null)
			return;
		
		if(ss.isOnJoin()==false)
			return;
		
		if(ss.getRolesToGive().size() == 0) {
			// Tell them you must specify which roles to give!!
			return;
		}
		
		Member member = event.getMember();
		boolean profileExists = (Profile.profileExists(member.getId()) != null);
		
		if(profileExists)
			return;
		
		if(privateMessageResponse.userExists(member.getId()) != null) {
			privateMessageResponse pmr = privateMessageResponse.userExists(member.getId());
			privateMessageResponse.allObjects.remove(pmr);
			System.gc();
			Bot.jda.removeEventListener(pmr);
		}
		
		int Pin = (int)(Math.random() * (999999 - 99999 + 1) + 99999);
		
		System.out.println("Pin: " + Pin);
		
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setTitle("Please Send Your SBU Email Address");
		embed.setDescription("You can access your SBU email by going to the link: stonybrook.edu/mycloud");
		try {
			event.getJDA().openPrivateChannelById(member.getId()).complete().sendMessage(embed.build()).queue();
		}
		catch(ErrorResponseException e) {
			embed.setTitle("Unable to send user a private message.");
			//channel.sendMessage(embed.build()).queue();
			return;
		}
		embed.clear();
		embed.setTitle("Please Check your DMs for next steps. Please be aware that the whole operation will expire in 5 minutes.");
		//embed.setDescription("You can access your SBU email by going to the link: stonybrook.edu/mycloud");
		//channel.sendMessage(embed.build()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
		//Create class object that waits for a response.
		//Timer for five minutes
		privateMessageResponse pmr = new privateMessageResponse(member.getId(), Pin);
		Bot.jda.addEventListener(pmr);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(privateMessageResponse.userExists(member.getId()) != null) {
					//message.delete().queue();
					//event.getChannel().sendMessage(event.getMember().getAsMention() + ", you did not respond within 15 seconds. Cancelling command.").queue();
					Bot.jda.removeEventListener(pmr);
					embed.clear();
					embed.setTitle("The operation has expired as 5 minutes has passed. Please re-run the command");
					event.getJDA().openPrivateChannelById(member.getId()).complete().sendMessage(embed.build()).queue();
				}
				else
					return;
					
			}
			
		}, 300000); // 5 minutes = 300000 ms
	}
}
