package Misc;

import javax.annotation.Nonnull;

import Data.Profile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class antiNickChange extends ListenerAdapter{
	public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {
		User user = event.getUser();
		
		if(user.isBot())
			return;
		
		Member member = event.getMember();
		Guild SBUGuild = event.getGuild();
		Profile profile = Profile.profileExists(user.getId());
		if(profile != null) {
			SBUGuild.modifyNickname(member, profile.returnName());
		}
	}
}
