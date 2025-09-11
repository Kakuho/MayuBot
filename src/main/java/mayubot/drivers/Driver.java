package mayubot.drivers;

import mayubot.Evaluator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.Random;

abstract class Driver{
  class DriverState{
    private Mode mode;
    private ResetMode resetMode;
    private long currentCount;         // these 3 are saved on reset and startup
    private long priorCount;
    private long longestCount;
    private long nextCount;

    public DriverState(Mode mode){
      this.mode = mode;
      InitialiseCounts(mode);
      this.resetMode = ResetMode.Normal;
    }

    public DriverState(Mode mode, ResetMode resetMode){
      this.mode = mode;
      InitialiseCounts(mode);
      this.resetMode = resetMode;
    }

    private void InitialiseCounts(Mode mode){
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

    public ResetMode GetResetMode(){
      return resetMode;
    }

    public void SetResetMode(ResetMode val){
      this.resetMode = val;
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

    public long GetLongestCount(){
      return longestCount;
    }

    public void SetLongestCount(long val){
      longestCount = val;
    }

    public long GetNextCount(){
      return nextCount;
    }

    public void SetNextCount(long val){
      nextCount = val;
    }
  }

  private Random rng;
  private Evaluator evaluator;
  private DriverState state;
  private Path valuesFile = Paths.get("./values.tmp"); 

  public Driver(){
    this.rng = new Random();
    this.evaluator = new Evaluator();
    this.state = new DriverState(Mode.Standard);
    if(Files.exists(valuesFile)){
      ReadLastValueFile();
    }
  }

  public Driver(Mode mode){
    this.rng = new Random();
    this.evaluator = new Evaluator();
    this.state = new DriverState(mode);
    if(Files.exists(valuesFile)){
      ReadLastValueFile();
    }
  }

  public Driver(Mode mode, ResetMode resetMode){
    this.rng = new Random();
    this.evaluator = new Evaluator();
    this.state = new DriverState(mode, resetMode);
    if(Files.exists(valuesFile)){
      ReadLastValueFile();
    }
  }

  public long CurrentCount(){
    return state.GetCurrentCount();
  }

  public long NextCount(){
    return state.GetNextCount();
  }

  private void ReadLastValueFile(){
    //  the structure of values.tmp is 
    //    current_count
    //    prior_count
    //    longest_count
    try(BufferedReader reader = Files.newBufferedReader(valuesFile)){
      String currentLine = reader.readLine();
      state.SetCurrentCount(Long.parseLong(currentLine));

      String priorLine = reader.readLine();
      state.SetPriorCount(Long.parseLong(priorLine));

      String longestLine = reader.readLine();
      state.SetLongestCount(Long.parseLong(longestLine));
    } 
    catch(IOException x){
        System.err.format("IOException: %s%n", x);
    }
  }

  private void WriteLastValueFile(){
    // see ReadLastValueFile
    try(BufferedWriter writer = Files.newBufferedWriter(valuesFile, StandardOpenOption.CREATE)){
      writer.write(Long.toString(state.GetCurrentCount()) + '\n');
      writer.write(Long.toString(state.GetPriorCount()) + '\n');
      writer.write(Long.toString(state.GetLongestCount()) + '\n');
    } 
    catch(IOException x){
        System.err.format("IOException: %s%n", x);
    }
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

  private void CheckLongestCount(){
    if(state.GetLongestCount() < state.GetCurrentCount()){
      state.SetLongestCount(state.GetCurrentCount());
    }
  }

  protected boolean NumberSuccess(long num){
    boolean success = false;
    switch(state.GetMode()){
      case Standard:
        success = StandardCheck(num);
        break;
      case Fibonacci:
        success = FibonacciCheck(num);
        break;
      case Even:
        success = EvenCheck(num);
        break;
      default:
        success = false;
        break;
    }
    if(success){
      WriteLastValueFile();
    }
    return success;
  }
  
  protected boolean StandardCheck(long num){
    if(num == state.GetCurrentCount() + 1){
      state.SetCurrentCount(state.GetCurrentCount() + 1);
      state.SetPriorCount(state.GetCurrentCount() - 1);
      return true;
    }
    else{
      CheckLongestCount();
      StandardReset();
      return false;
    }
  }

  private void StandardReset(){
    if(state.GetResetMode() == ResetMode.Normal){
      ResetCount(0);
    }
    else if(state.GetResetMode() == ResetMode.Probabilistic){
      ResetCount(rng.nextLong(0, state.GetLongestCount()));
      state.SetNextCount(state.GetCurrentCount() + 1);
    }
  }

  protected boolean EvenCheck(long num){
    if(num == state.GetCurrentCount() + 2){
      state.SetCurrentCount(state.GetCurrentCount() + 2);
      state.SetPriorCount(state.GetCurrentCount() - 2);
      return true;
    }
    else{
      CheckLongestCount();
      EvenReset();
      return false;
    }
  }

  private void EvenReset(){
    if(state.GetResetMode() == ResetMode.Normal){
      ResetCount(0);
    }
    else if(state.GetResetMode() == ResetMode.Probabilistic){
      ResetCount(rng.nextLong(0, state.GetLongestCount()));
      state.SetNextCount(state.GetCurrentCount() + 1);
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
      CheckLongestCount();
      FibonacciReset();
      return false;
    }
  }

  private void FibonacciReset(){
    if(state.GetResetMode() == ResetMode.Normal){
      ResetCount(1);
      RestPriorCount(1);
    }
    else if(state.GetResetMode() == ResetMode.Probabilistic){
      // have to generate all fibonacci numbers from 0 to longest...
      // and select a random one
      //ResetCount(rng.nextLong(0, state.GetLongestCount()));
      //state.SetNextCount(state.GetCurrentCount() + 1);
    }
  }

  protected abstract void HandleFailure();
  protected abstract void HandleSuccess();
  public abstract void Run();
}
