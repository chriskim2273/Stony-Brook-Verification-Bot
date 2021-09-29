package SBUBot;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter{
	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		System.out.printf("%#s is ready.", event.getJDA().getSelfUser());
	}
}
