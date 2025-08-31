package mayubot.drivers;

import mayubot.Evaluator;
import mayubot.ast.EvaluationResult;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;

class MayuListener extends ListenerAdapter{
  public enum Mode{Standard, Fibonacci, Even}
  private Mode mode;
  private Evaluator evaluator;
  private long currentCount;
  private long priorCount;

  public MayuListener(Mode mode){
    this.evaluator = new Evaluator();
    this.mode = mode;

    switch(mode){
      case Standard:
      case Fibonacci:
        this.currentCount = 1;
        this.priorCount = 1;
        break;
      case Even:
        this.currentCount = 2;
        this.priorCount = 2;
        break;
    }
  }

  private long GetNumber(String expression){
    var result = evaluator.Process(expression);
    if(result.IsFloating()){
      return (long)result.GetFloating();
    }
    else if(result.IsIntegral()){
      return result.GetInt();
    }
    else{
      throw new RuntimeException("Driver.GetNumber(): " +
          "Failed to get a integral or floating from the expression");
    }
  }

  private boolean NumberSuccess(long num){
    switch(mode){
      case Standard:
        return StandardCheck(num);
      case Fibonacci:
        return FibonacciCheck(num);
      case Even:
        return EvenCheck(num);
      default:
        return false;
    }
  }

  private boolean StandardCheck(long num){
    if(num == currentCount +1){
      currentCount = currentCount+1;
      return true;
    }
    else{
      ResetCount(1);
      return false;
    }
  }

  private boolean EvenCheck(long num){
    if(num == currentCount + 2){
      currentCount = currentCount + 2;
      return true;
    }
    else{
      ResetCount(2);
      return false;
    }
  }

  private boolean FibonacciCheck(long num){
    if(num == currentCount){
      long tmp = priorCount;
      priorCount = currentCount;
      currentCount += tmp;
      return true;
    }
    else{
      ResetCount(1);
      RestPriorCount(1);
      return false;
    }
  }

  private void ResetCount(long initial){ currentCount = initial; }
  private void RestPriorCount(long initial){ priorCount = initial;}

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
        long num = GetNumber(message);
        if(NumberSuccess(num)){
          channel.sendMessage(String.format("Nyanderful!")).queue(); 
        }
        else{
          channel.sendMessage(String.format("Maybe I've spun too hard... Resseting count back to 1")).queue(); 
        }
      }
      catch(RuntimeException e){
        return;
      }
    }
  }
}
