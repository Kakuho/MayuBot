package mayubot.ast;

import mayubot.ast.AbstractNode;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

// FunctionCallTable is a look up table for functions.

interface FunctionCallHandler{
  EvaluationResult Invoke(AbstractNode expr);
}

class FunctionCallTable{
  private HashMap<String, FunctionCallHandler> map;

  public FunctionCallTable(){
    this.map = new HashMap<String, FunctionCallHandler>();
    InitialiseMap();
  }

  private void InitialiseMap(){
    map.put("factorial", FunctionCallTable::HandleFactorial);
    map.put("sum", FunctionCallTable::HandleSum);
    map.put("ceil", FunctionCallTable::HandleCeiling);
    map.put("floor", FunctionCallTable::HandleFloor);
  }

  public FunctionCallHandler Get(String functionName){
    return map.get(functionName);
  }

  public List<String> GetFunctionNames(){
    return new ArrayList<String>(map.keySet());
  }

  public boolean Contains(String fname){
    if(map.containsKey(fname)){
      return true;
    }
    else{
      return false;
    }
  }

  // The function handlers are static methods because they do not need direct 
  // access to the private data fields

  public static EvaluationResult HandleFactorial(AbstractNode expr){
    var result = expr.GetResult();
    if(result.IsFloating()){
      return new EvaluationResult(-1);
    }
    var factorialval = mayubot.util.Math.Factorial(result.GetInt());
    return new EvaluationResult(factorialval);
  }

  public static EvaluationResult HandleSum(AbstractNode expr){
    var result = expr.GetResult();
    if(result.IsFloating()){
      return new EvaluationResult(-1);
    }
    var factorialval = mayubot.util.Math.Factorial(result.GetInt());
    return new EvaluationResult(factorialval);
  }

  public static EvaluationResult HandleFloor(AbstractNode expr){
    var result = expr.GetResult();
    if(result.IsFloating()){
      var floorval = java.lang.Math.floor(result.GetFloating());
      return new EvaluationResult(floorval);
    }
    else{
      var floorval = java.lang.Math.floor(result.GetInt());
      return new EvaluationResult(floorval);
    }
  }

  public static EvaluationResult HandleCeiling(AbstractNode expr){
    var result = expr.GetResult();
    if(result.IsFloating()){
      var ceilval = java.lang.Math.ceil(result.GetFloating());
      return new EvaluationResult(ceilval);
    }
    else{
      var ceilval = java.lang.Math.floor(result.GetInt());
      return new EvaluationResult(ceilval);
    }
  }
}
