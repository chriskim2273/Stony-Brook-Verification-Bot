package commands;

import java.util.List;

import Data.Profile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class profileCMD extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User user = event.getAuthor();
		if(user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		String command = "!profile";
		String raw = event.getMessage().getContentRaw();
		String UserID = event.getAuthor().getId();
		
		if(raw.contains(command)){
			EmbedBuilder embed = new EmbedBuilder();
			Profile profile;
			List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
			if(mentionedMembers.size() == 0 ) {
				 profile = Profile.profileExists(UserID);
			} else if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				//Doesn't work
				profile = Profile.profileExists(mentionedMembers.get(0).getId());
				user = event.getJDA().getUserById(mentionedMembers.get(0).getId());
				if(user == null) {
					embed.setTitle("User has no profile or doesn't exist. idk...");
					event.getChannel().sendMessage(embed.build()).queue();
					return;
				}
			} else {
				profile = Profile.profileExists(UserID);
			}
			
			if(profile == null) {
				embed.setTitle("You have no profile. Please verify your email to create one.");
				event.getChannel().sendMessage(embed.build()).queue();
				return;
			}
			
			embed.setTitle(user.getName() + "'s Profile");
			embed.setImage(user.getAvatarUrl());
			embed.setThumbnail("https://www.shareicon.net/data/2015/09/12/100021_verified_512x512.png");
			embed.addField("Name",profile.returnName(),false);
			embed.addField("Email",profile.returnEmail(),false);
			//embed.addField("Birthday",profile.returnBirthday(),false);
			//Add age...
			embed.setFooter(user.getId());
			event.getChannel().sendMessage(embed.build()).queue();
		}
	}
}
