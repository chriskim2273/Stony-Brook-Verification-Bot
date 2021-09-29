package Misc;

import javax.annotation.Nonnull;

import SBUBot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class userCount extends ListenerAdapter{
	
	public static Guild SBUGuild = Bot.jda.getGuildById("863334781155278880");
	public static VoiceChannel verified;
	public static VoiceChannel total;
	
	public static void updateCount() {
		Role verifiedRole = Bot.jda.getRoleById("863340993567129621");
		int verifiedCount = SBUGuild.getMembersWithRoles(verifiedRole).size();
		int totalCount = SBUGuild.getMemberCount();
		
		verified = SBUGuild.getVoiceChannelById("863598199402004540");
		total = SBUGuild.getVoiceChannelById("863598305187463188");
		
		verified.getManager().setName("Verified Students: " + verifiedCount).queue();
		
		total.getManager().setName("Total Users: " + totalCount).queue();
		
	}
	
	public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
		updateCount();
	}
	
	public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
		updateCount();
	}
	
}
