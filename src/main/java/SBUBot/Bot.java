package SBUBot;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import Data.MongoDB;
import Data.ServerSetting;
import Misc.NewMemberOperations;
import Misc.NewServerOperations;
import Misc.giveRoleonJoin;
import commands.RoleCommand;
import commands.SettingsCommands;
import commands.profileCMD;
import commands.verifyCMD;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {

	public static JDA jda;
	//public static Guild SBUGuild;
	
	@SuppressWarnings("deprecation")
	private Bot() throws LoginException {
		jda = JDABuilder.createDefault("ODYzMzMxODYxODA3MTA0MDEw.YOlWcw.s-KUF0h1r_yYms3Uu1aG--WGKOQ")
		//.setToken(Config.get("TOKEN"))
		//.setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
        .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
        .enableIntents(EnumSet.allOf(GatewayIntent.class))
		.build();
		
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.watching("!verify"));
        //
        jda.addEventListener(new Listener());
        jda.addEventListener(new verifyCMD());
        jda.addEventListener(new profileCMD());
        //jda.addEventListener(new verifyChannel());
        jda.addEventListener(new giveRoleonJoin());
        
        jda.addEventListener(new RoleCommand());
        jda.addEventListener(new SettingsCommands());
        jda.addEventListener(new NewServerOperations());
        jda.addEventListener(new NewMemberOperations());
	}
	public static void main(String[] args) throws LoginException {
		MongoDB.retrieveProfiles();
		MongoDB.retrieveSettings();
		new Bot();
		//SBUGuild = jda.getGuildById("863334781155278880");
		
		//userCount.updateCount();
	}
}
