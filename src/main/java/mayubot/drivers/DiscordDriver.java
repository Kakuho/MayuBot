package mayubot.drivers;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.EnumSet;

public class DiscordDriver extends Driver{
  private MayuListener mayuListener;
  private JDA jdaSession;
  private TextChannel mayubotChannel;

  public DiscordDriver(){
    super();
    this.mayuListener = new MayuListener();
    this.mayuListener.SetDriver(this);
  }

  public DiscordDriver(Mode mode, ResetMode resetmode){
    super(mode, resetmode);
    this.mayuListener = new MayuListener();
    this.mayuListener.SetDriver(this);
  }

  // Discord Interactions

  private void SendStartupMessage(){
    // we need to reach jda.connected first...
    var channelList = jdaSession.getTextChannels();
    if(channelList.size() == 0){
      System.out.println("zero text channels?");
    }
    for(var channel: channelList){
      if(channel.getName().equals("mayubot")){
        mayubotChannel = channel;
        break;
      }
    }
    if(mayubotChannel == null){
      throw new RuntimeException("Cannot find channel with name mayubot");
    }
    mayubotChannel.sendMessage(
      String.format("Mayubot online. Next count is: %d", CurrentCount() + 1)
    ).queue();
  }

  private void AddSlashCommands(){
    jdaSession.updateCommands().addCommands(
      Commands.slash("echo", "Repeats messages back to you.")
        .addOption(OptionType.STRING, "message", "The message to repeat.")
        .addOption(OptionType.INTEGER, "times", "The number of times to repeat the message.")
        .addOption(OptionType.BOOLEAN, "ephemeral", "Whether or not the message should be sent as an ephemeral message."),

      Commands.slash("longest", "Query the longest counting streak.")
    ).queue();
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

      jdaSession = JDABuilder.createLight(token, intents)
                  .addEventListeners(this.mayuListener)
                  .build();

      try{
        jdaSession.awaitReady();
      }
      catch(InterruptedException e){
        System.out.println("Thread interrupted during jda.awaitReady()");
        throw new RuntimeException();
      }
      catch(IllegalStateException e){
        System.out.println("illegal state reached during jda.awaitReady()");
        throw new RuntimeException();
      }

      SendStartupMessage();
      AddSlashCommands();
    }
  }

}
