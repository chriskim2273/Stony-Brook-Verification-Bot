package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.annotation.Nonnull;

import Data.ServerSetting;
import Misc.NewServerOperations;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SettingsCommands extends ListenerAdapter{
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		
		//Require them to be admins.
		
		if(event.getAuthor().isBot() || event.isWebhookMessage())
			return;
		
		Message message = event.getMessage();
		String raw = message.getContentRaw();
		String[] Separated = raw.split("\\s+");

		if(raw.charAt(0) != '!')
			return;
		
		if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)){
			return;
		}
	
		Guild guild = event.getGuild();
		
		//Set onJoin
		if(Separated[0].equals("!toggleonjoin")) {
			ServerSetting ss = ServerSetting.getServer(guild.getId());
			TextChannel tc = event.getChannel();
			
			//Shouldn't be null ever.
			if(ss != null) {
				if(ss.isOnJoin()) {
					ss.setOnJoin(false);
					tc.sendMessage("On Join Setting Disabled").queue();
				}
				else {
					ss.setOnJoin(true);
					tc.sendMessage("On Join Setting Disabled").queue();
				}
			}
		}
		
		//Set Channel-Specific
		
		if(Separated[0].equals("!togglechannel")) {
			ServerSetting ss = ServerSetting.getServer(guild.getId());
			TextChannel tc = event.getChannel();
			if(ss != null) {
				if(ss.isChannelSpecific()) {
					ss.setChannelSpecific(false);
					tc.sendMessage("Channel Specific Setting Disabled").queue();
				}
				else {
					//System.out.println("joe");
					ss.setChannelSpecific(true);
					tc.sendMessage("Channel Specific Setting Enabled").queue();
					//System.out.println(ss.getChannels().size());
					if(ss.getChannels().size() == 0) {
						//They have to set channels.
						tc.sendMessage("You have yet to specify what channels the verify command can be used in. The feature will not be put into effect until they are specified").queue();
					}
				}
			}
		}
		
		if(Separated[0].equals("!setchannels")) {
			ServerSetting ss = ServerSetting.getServer(guild.getId());
			
			if(ss==null)
				return;
			
			TextChannel tc = event.getChannel();
			
			LinkedList<TextChannel> alltextchannels = new LinkedList<TextChannel>(message.getMentionedChannels());
			
			ArrayList<String> guildtextchannels = new ArrayList<String>();
			for(TextChannel channel: alltextchannels) {
				if(tc.getGuild().equals(guild)){
					guildtextchannels.add(tc.getId());
				}
			}
			//Kinda unneccessary
			alltextchannels = null;
			System.gc();
			
			if(guildtextchannels.size() == 0) {
				tc.sendMessage(event.getAuthor().getAsMention() + " , please specify proper textchannels in this guild.").queue();
				return;
			}
			
			ss.setChannels(guildtextchannels);
			tc.sendMessage("Channels set.").queue();
			//Set server settings to the channels.
			//Also cehck if the message has any actual chanenls mention that are in the guild.
					
		}
		
		if(Separated[0].equals("!help")) {
			event.getChannel().sendMessage(NewServerOperations.cmdEmbed()).queue();
		}
		
		if(Separated[0].equals("!settings")) {
			ServerSetting ss = ServerSetting.getServer(guild.getId());
			HashMap<Boolean,String> bool = new HashMap<Boolean,String>();
			bool.put(true, "✅");
			bool.put(false, "❌");
			
			String channels = "";
			for(String s: ss.getChannels()) {
				channels += event.getGuild().getTextChannelById(s).getAsMention() + "\n";
			}
			String roles = "";
			for(String s: ss.getRolesToGive()) {
				roles += event.getGuild().getRoleById(s).getAsMention() + " \n";
			}
			
			EmbedBuilder Settings = new EmbedBuilder();
			Settings.setTitle("Settings:")
			.setThumbnail("https://cdn1.iconfinder.com/data/icons/material-design-icons-light/24/settings-512.png")
			.addField("On Join", bool.get(ss.isOnJoin()), false)
			.addField("Is Channel Specific", bool.get(ss.isChannelSpecific()), false)
			.addField("Channels (For Channel-Specific Setting)", channels, false)
			.addField("Roles (To Give Users Upon Verification", roles, false)
			.setFooter("Created by Teapot#2273");
			
			event.getChannel().sendMessage(Settings.build()).queue();
		}
	}
}
