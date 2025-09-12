package mayubot.drivers;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

class MayuListener extends ListenerAdapter{
  private DiscordDriver driver;

  public MayuListener(){
  }

  public void SetDriver(DiscordDriver driver){
    this.driver = driver;
  } 

  // Slash command Handlers... maybe best to be extracted out as static methods

  public void HandleEcho(SlashCommandInteractionEvent event){
    var content = event.getOption("message").getAsString();
    var replymessage = String.format("Mayubot Echo: %s", content);
    event.reply(replymessage).queue();
  }

  public void HandleLongest(SlashCommandInteractionEvent event){
    var replymessage = String.format("Longest Count: %d", driver.LongestCount());
    event.reply(replymessage).queue();
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    if(event.getName().equals("echo")){
      HandleEcho(event);
    }
    else if(event.getName().equals("longest")){
      HandleLongest(event);
    }
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event){

    if(event.getAuthor().isBot()){
      return;
    }
    if(!event.getChannel().getName().equals("mayubot")){
      return;
    }
    else{
      // do the event callback
      var channel = event.getChannel();

      var message = event.getMessage().getContentRaw();
      // Evaluate the message using an evaluator
      try{
        long num = driver.GetNumber(message);
        if(driver.NumberSuccess(num)){
          channel.sendMessage(String.format("Nyanderful!")).queue(); 
        }
        else{
          channel.sendMessage(String.format("Maybe I've spun too hard... Next count is %d", driver.NextCount())).queue(); 
        }
      }
      catch(RuntimeException e){
        return;
      }
    }
  }
}
