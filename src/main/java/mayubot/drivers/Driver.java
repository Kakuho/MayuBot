package mayubot.drivers;

import mayubot.Evaluator;

abstract class Driver{
  class DriverState{
    private Mode mode;
    private long currentCount;
    private long priorCount;

    public DriverState(Mode mode){
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
      default:
        throw new RuntimeException("Driver.DriverState(Mode): " +
            "unknown mode is used");
      }
    }

    public Mode GetMode(){
      return mode;
    }

    public void SetMode(Mode val){
      this.mode = val;
    }

    public Evaluator GetEvaluator(){
      return evaluator;
    }

    public long GetCurrentCount(){
      return currentCount;
    }

    public void SetCurrentCount(long val){
      currentCount = val;
    }

    public long GetPriorCount(){
      return priorCount;
    }

    public void SetPriorCount(long val){
      priorCount = val;
    }
  }

  private Evaluator evaluator;
  private DriverState state;

  public Driver(){
    this.evaluator = new Evaluator();
    this.state = new DriverState(Mode.Standard);
  }

  public Driver(Mode mode){
    this.evaluator = new Evaluator();
    this.state = new DriverState(mode);
  }

  protected void ResetCount(long initial){
    state.SetCurrentCount(initial);
  }

  protected void RestPriorCount(long initial){ 
    state.SetPriorCount(initial);
  }

  protected long GetNumber(String expression){
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

  // shared number checking code

  protected boolean NumberSuccess(long num){
    switch(state.GetMode()){
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
  
  protected boolean StandardCheck(long num){
    if(num == state.GetCurrentCount() + 1){
      state.SetCurrentCount(state.GetCurrentCount() + 1);
      return true;
    }
    else{
      ResetCount(1);
      return false;
    }
  }

  protected boolean EvenCheck(long num){
    if(num == state.GetCurrentCount() + 2){
      state.SetCurrentCount(state.GetCurrentCount() + 2);
      return true;
    }
    else{
      ResetCount(2);
      return false;
    }
  }

  protected boolean FibonacciCheck(long num){
    if(num == state.GetCurrentCount()){
      long tmp = state.GetPriorCount();
      state.SetPriorCount(state.GetCurrentCount());
      state.SetCurrentCount(state.GetCurrentCount() + tmp);
      return true;
    }
    else{
      ResetCount(1);
      RestPriorCount(1);
      return false;
    }
  }

  protected abstract void HandleFailure();
  protected abstract void HandleSuccess();
  public abstract void Run();
}
