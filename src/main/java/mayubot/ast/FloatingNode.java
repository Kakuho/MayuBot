package mayubot.ast;

import mayubot.ast.EvaluationResult;

public class FloatingNode implements AbstractNode{
  private final double val;

  public FloatingNode(double val){
    this.val = val;
  }

  public double GetVal(){ return val;}

  public NodeType GetType(){ return NodeType.IntegerNode;}

  public EvaluationResult GetResult(){
    return new EvaluationResult(val);
  }
}
