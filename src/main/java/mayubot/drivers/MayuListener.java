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

  public void HandleHelp(SlashCommandInteractionEvent event){
    var aboutMessage = "Help for grammar:\n\n"
      + "```The discord bot implements the following grammar:\n"
      + "E -> T + E | T - E | T\n"
      + "T -> F / T | F * T | F\n"
      + "F -> B^B | B\n"
      + "B -> ( E ) | int | float | function-call\n"
      + "function-call -> function-name(E) \n"
      + "function-name -> factorial | sum | floor | ceiling```";
    event.reply(aboutMessage).queue();
  }

  public void HandleAbout(SlashCommandInteractionEvent event){
    var aboutMessage = "Discord bot made by Garbage Monkey\nI hope you enjoy it!";
    event.reply(aboutMessage).queue();
  }

  public void HandleCallableFunction(SlashCommandInteractionEvent event){
    var aboutMessage = "The discord bot supports the following functions: ```factorial, sum, floor, ceil```";
    event.reply(aboutMessage).queue();
  }

  public void HandleCountMode(SlashCommandInteractionEvent event){
    var mode = event.getOption("mode").getAsString();
    if(mode.toLowerCase().equals("even")){
      driver.SetCountingMode(Mode.Even);
    }
    else if(mode.toLowerCase().equals("standard")){
      driver.SetCountingMode(Mode.Standard);
    }
    else{
      event.reply("sorry that mode is unrecognised!").queue();
    }
    var message = String.format("Mayubot's Coutning Mode has been set to: %s! Next count is %d", mode.toLowerCase(), driver.NextCount());
    event.reply(message).queue();
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    if(event.getName().equals("echo")){
      HandleEcho(event);
    }
    else if(event.getName().equals("longest")){
      HandleLongest(event);
    }
    else if(event.getName().equals("help")){
      HandleHelp(event);
    }
    else if(event.getName().equals("about")){
      HandleAbout(event);
    }
    else if(event.getName().equals("callable_functions")){
      HandleCallableFunction(event);
    }
    else if(event.getName().equals("count_mode")){
      HandleCountMode(event);
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
