package mayubot.drivers;

import mayubot.Evaluator;
import mayubot.ast.EvaluationResult;

import java.util.Scanner;
import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.List;

public class CliDriver{
  public enum Mode{Standard, Fibonacci, Even}
  private Mode mode;
  private Evaluator evaluator;
  private long currentCount;
  private long priorCount;
  private Scanner sc;

  public CliDriver(Mode mode){
    this.evaluator = new Evaluator();
    this.sc = new Scanner(System.in);
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

  private String GetUserInput(){
    String inputstr = sc.nextLine();
    return inputstr;
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
          "Failed to get a integral or floating from teh expression");
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

  private void DoEnd(){
    System.out.println("Ending the Counting Game!");
  }

  private void DoFailure(){
    System.out.println("You miscounted! Count is resetting back to 1");
  }

  private void DoSuccess(){
    System.out.println("Success!");
  }

  public void Run(){
    while(true){
      var inputString = GetUserInput();
      if(inputString.equals("E")){
        DoEnd();
        break;  
      }
      long num = GetNumber(inputString);
      System.out.println(String.format("Debug: Entered Number Parsed as %d", num));
      if(!NumberSuccess(num)){
        DoFailure();
      }
      else{
        DoSuccess();
      }
    }
  }
}
