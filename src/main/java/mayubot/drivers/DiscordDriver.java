package mayubot.drivers;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;

public class DiscordDriver{
  public DiscordDriver(){
  var token = System.getenv("TOKEN");
    if(token == null){
      System.out.println("invalid token, exiting");
    }
    else{
      JDABuilder.createLight(token, EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                .addEventListeners(new MayuListener(MayuListener.Mode.Standard))
                .build();
    }
  }
}
