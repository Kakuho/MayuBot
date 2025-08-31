package mayubot.ast;

import java.util.Optional;

public class EvaluationResult{
  public enum ResultType{Integral, Floating};
  private ResultType resultType;
  private Optional<Long> intResult;
  private Optional<Double> floatResult;

  public EvaluationResult(long intVal){
    this.intResult = Optional.of(intVal);
    this.floatResult = Optional.empty();
    this.resultType = ResultType.Integral;
  }

  public EvaluationResult(double floatVal){
    this.intResult = Optional.empty();
    this.floatResult = Optional.of(floatVal);
    this.resultType = ResultType.Floating;
  }

  public boolean IsIntegral(){ return resultType == ResultType.Integral;}
  public boolean IsFloating(){ return resultType == ResultType.Floating;}

  public long GetInt(){ return intResult.get().intValue();}
  public double GetFloating(){ return floatResult.get().floatValue();}

  public static boolean SameType(EvaluationResult lhs, EvaluationResult rhs){
    if(lhs.resultType != rhs.resultType){
      return false;
    }
    else{
      return true;
    }
  }
}
