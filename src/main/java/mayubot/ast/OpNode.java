package mayubot.ast;

import mayubot.ast.AbstractNode;
import mayubot.ast.IntegerNode;
import mayubot.ast.EvaluationResult;

import java.lang.Math;

public class OpNode implements AbstractNode{
  private Operator op;
  private AbstractNode lhs;
  private AbstractNode rhs;
  private final AbstractNode.NodeType nodeType = AbstractNode.NodeType.OpNode;

  public OpNode(Operator op, AbstractNode lhs, AbstractNode rhs){
    this.op = op;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public AbstractNode.NodeType GetType(){
    return nodeType;
  }

  private EvaluationResult Calcuate(EvaluationResult lhs, EvaluationResult rhs){
    // it is here where you need to take care of arithmetic conversions
    if(EvaluationResult.SameType(lhs, rhs)){
      return HandleSameType(lhs, rhs);
    }
    else{
      return HandleDifferentTypes(lhs, rhs);
    }
  }

  private EvaluationResult HandleSameType(EvaluationResult lhs, EvaluationResult rhs){
    boolean bothIntegral = lhs.IsIntegral() && rhs.IsIntegral();
    boolean bothFloating = lhs.IsFloating() && rhs.IsFloating();
    if(bothIntegral){
      long lhsval = lhs.GetInt();
      long rhsval = rhs.GetInt();
      switch(op){
        case Plus: return new EvaluationResult(lhsval + rhsval);
        case Sub: return new EvaluationResult(lhsval - rhsval);
        case Multiply: return new EvaluationResult(lhsval * rhsval);
        case Div: return new EvaluationResult(lhsval / rhsval);
        case Pow: return new EvaluationResult((long)Math.pow(lhsval, rhsval));
        default: return null;
      }
    }
    else if(bothFloating){
      double lhsval = lhs.GetFloating();
      double rhsval = rhs.GetFloating();
      switch(op){
        case Plus: return new EvaluationResult(lhsval + rhsval);
        case Sub: return new EvaluationResult(lhsval - rhsval);
        case Multiply: return new EvaluationResult(lhsval * rhsval);
        case Div: return new EvaluationResult(lhsval / rhsval);
        case Pow: return new EvaluationResult(Math.pow(lhsval, rhsval));
        default: return null;
      }
    }
    else{
      // tempted to throw a runtime error exception here
      return null;
    }
  }

  private EvaluationResult HandleDifferentTypes(
    EvaluationResult lhs, 
    EvaluationResult rhs
  ){
    if(lhs.IsIntegral() && rhs.IsFloating()){
      long lhsval = lhs.GetInt();
      double rhsval = rhs.GetFloating();
      double lhsvalConverted = (double)lhsval;
      switch(op){
        case Plus: return new EvaluationResult(lhsvalConverted + rhsval);
        case Sub: return new EvaluationResult(lhsvalConverted - rhsval);
        case Multiply: return new EvaluationResult(lhsvalConverted * rhsval);
        case Div: return new EvaluationResult(lhsvalConverted / rhsval);
        case Pow: return new EvaluationResult(Math.pow(lhsvalConverted, rhsval));
        default: return null;
      }
    }
    else if(lhs.IsFloating() && rhs.IsIntegral()){
      double lhsval = lhs.GetFloating();
      long rhsval = rhs.GetInt();
      double rhsvalConverted = (double)rhsval;
      switch(op){
        case Plus: return new EvaluationResult(lhsval + rhsvalConverted);
        case Sub: return new EvaluationResult(lhsval - rhsvalConverted);
        case Multiply: return new EvaluationResult(lhsval * rhsvalConverted);
        case Div: return new EvaluationResult(lhsval / rhsvalConverted);
        case Pow: return new EvaluationResult(Math.pow(lhsval, rhsvalConverted));
        default: return null;
      }
    }
    else{
      return null;
    }
  }

  public EvaluationResult Evaluate(){
    EvaluationResult lhsval, rhsval;
    if(lhs instanceof OpNode){
      var lhsOp = (OpNode)lhs;
      lhsval = lhsOp.Evaluate();
    }
    else{
      lhsval = lhs.GetResult();
    }
    if(rhs instanceof OpNode){
      var rhsOp = (OpNode)rhs;
      rhsval = rhsOp.Evaluate();
    }
    else{
      rhsval = rhs.GetResult();
    }
    return Calcuate(lhsval, rhsval);
  }

  public EvaluationResult GetResult(){
    return Evaluate();
  }
}
