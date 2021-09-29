package Misc;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class giveRoleonJoin extends ListenerAdapter{
	public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
		System.out.println("bruh");
		Guild SBUGuild = event.getGuild();
		Role role = SBUGuild.getRoleById("863586957754236939");
		Member member = event.getMember();
		SBUGuild.addRoleToMember(member, role).queue();
	}
}
