package mayubot.drivers;

import mayubot.Evaluator;
import mayubot.ast.EvaluationResult;

import java.util.Scanner;
import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.List;

public final class CliDriver extends Driver{
  private Scanner sc;

  public CliDriver(){
    super();
  }

  public CliDriver(Mode mode){
    super(mode);
    sc = new Scanner(System.in);
  }

  private String GetUserInput(){
    String inputstr = sc.nextLine();
    return inputstr;
  }

  // main loop handlers

  private void HandleEnd(){
    System.out.println("Ending the Counting Game!");
  }

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
    while(true){
      var inputString = GetUserInput();
      if(inputString.equals("E")){
        HandleEnd();
        break;  
      }
      long num = GetNumber(inputString);
      System.out.println(String.format("Debug: Entered Number Parsed as %d", num));
      if(!NumberSuccess(num)){
        HandleFailure();
      }
      else{
        HandleSuccess();
      }
    }
  }
}
