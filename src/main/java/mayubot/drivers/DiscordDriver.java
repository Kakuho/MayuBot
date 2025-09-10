package mayubot.drivers;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

public class DiscordDriver extends Driver{
  private MayuListener mayuListener;
  public DiscordDriver(){
    super();
    this.mayuListener = new MayuListener();
    this.mayuListener.SetDriver(this);
  }

  public DiscordDriver(Mode mode){
    super(mode);
    this.mayuListener = new MayuListener();
    this.mayuListener.SetDriver(this);
  }

  // main event loop handlers

  @Override
  protected void HandleFailure(){
    System.out.println("You miscounted! Count is resetting back to 1");
  }

  @Override
  protected void HandleSuccess(){
    System.out.println("Success!");
  }

  @Override
  public void Run(){
    var token = System.getenv("TOKEN");
    if(token == null){
      System.out.println("invalid token, exiting");
    }
    else{
      var intents = EnumSet.of(
        GatewayIntent.GUILD_MESSAGES, 
        GatewayIntent.MESSAGE_CONTENT
      );

      JDABuilder.createLight(token, intents)
                .addEventListeners(this.mayuListener)
                .build();
    }
  }
}
