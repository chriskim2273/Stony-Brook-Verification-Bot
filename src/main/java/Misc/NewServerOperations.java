package Misc;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import Data.MongoDB;
import Data.ServerSetting;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NewServerOperations extends ListenerAdapter{
	public static MessageEmbed cmdEmbed() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("All Setup Commands:")
		.setThumbnail("https://cdn0.iconfinder.com/data/icons/cosmo-symbols/40/help_1-512.png")
		.setDescription("To make sure that the bot works how you want it to, please make sure you check out some of the setting commands!")
		.addField("!toggleonjoin","(OFF BY DEFAULT) This setting determines if a new user gets sent a verification private message if they join the server.",false)
		.addField("!setRoles", "(MANDATORY) Set the roles the discord bot will give to those that are verified.",false)
		.addField("!togglechannel", "Toggle whether the bot only accepts the command in one text channel.",false)
		.addField("!setchannels", "If toggle channels is enabled, then you must specify which channels using this command.",false)
		.setFooter("Created by Teapot#2273");
		return embed.build();
	}
	public void onGuildJoin(@Nonnull GuildJoinEvent event) {
		String serverID = event.getGuild().getId();
		ServerSetting ss = ServerSetting.getServer(serverID);
		
		if(ss == null) {
			ss = new ServerSetting(serverID, false, false, new ArrayList<String>(), new ArrayList<String>());
			MongoDB.saveSettings(ss);
			
			TextChannel setupChannel = event.getGuild().createTextChannel("SBU-Verification-Setup").complete();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("This text-channel will automatically delete itself once you do the mandatory setup. (DO NOT RENAME)");
			embed.setDescription("*ONLY ADMINS CAN DO SETUP/SETTING COMMANDS* \nYou must set the roles that the bot will assign upon verification! Use the command !setRoles (mention roles) .");
			setupChannel.sendMessage(embed.build()).queue();
			setupChannel.sendMessage(cmdEmbed()).queue();
		}
		
		//send embed to server asking to set settings.
		//
		
	}
}
