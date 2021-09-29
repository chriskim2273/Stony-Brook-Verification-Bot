package commands;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.annotation.Nonnull;

import Data.ServerSetting;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleCommand extends ListenerAdapter{
	@SuppressWarnings("unused")
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		
		if(event.getAuthor().isBot() || event.isWebhookMessage())
			return;
		
		if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)){
			return;
		}
		
		Message message = event.getMessage();
		String command = "!setroles";
		String raw = message.getContentRaw();
		String[] Separated = raw.split("\\s+");
		String[] args = raw.replaceFirst(command + " ", "").split("\\s+");
		
		
		if(Separated[0].equals(command)) {
			
			LinkedList<Role> roles = new LinkedList<Role>(message.getMentionedRoles());
			TextChannel channel = event.getChannel();
			Guild guild = event.getGuild();
			if(roles.isEmpty()) {
				channel.sendMessage("Please mention the roles you wish the bot to assign to new members.").queue();
				return;
			}
			else {
				ServerSetting ss = ServerSetting.getServer(guild.getId());
				
				if(ss != null) {
					ArrayList<String> roleIDs = new ArrayList<String>();
					for(Role r: roles) {
						roleIDs.add(r.getId());
					}
					ss.setRolesToGive(roleIDs);
					System.out.println(ss.getRolesToGive().toString());
					
					//Remove the setup text channel
					try {
						event.getGuild().getTextChannelsByName("sbu-verification-setup", false).get(0).delete().queue();
						
					}catch(IndexOutOfBoundsException e) {
						System.out.println("No Channel By That Name.");
					}
					channel.sendMessage("Roles Set.").queue();
				}
			}
		}
		
	}
}
