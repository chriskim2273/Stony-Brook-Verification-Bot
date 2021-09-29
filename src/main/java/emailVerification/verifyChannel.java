package emailVerification;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class verifyChannel extends ListenerAdapter{
	
	public String channelID = "863586772650950686";
	public String whitelist = "!verify";
	
	
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		
		User user = event.getAuthor();
		if(user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		//Guild SBUGuild = event.getGuild();
		TextChannel channel = event.getChannel();
		
		if(!channel.getId().equals(channelID))
			return;
		
		if(event.getMessage().getContentRaw().equals(whitelist))
			channel.deleteMessageById(event.getMessageId()).queueAfter(5, TimeUnit.SECONDS);
		else
			channel.deleteMessageById(event.getMessageId()).queue();
		
		
	}
}
