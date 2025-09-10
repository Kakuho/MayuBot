package mayubot.drivers;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

class MayuListener extends ListenerAdapter{
  private DiscordDriver driver;

  public void SetDriver(DiscordDriver driver){
    this.driver = driver;
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
