package mayubot.ast;

import mayubot.ast.EvaluationResult;

public class FloatingNode implements AbstractNode{
  private final double val;

  public FloatingNode(double val){
    this.val = val;
  }

  public double GetVal(){ return val;}

  @Override
  public NodeType GetType(){ return NodeType.IntegerNode;}

  @Override
  public EvaluationResult GetResult(){
    return new EvaluationResult(val);
  }
}
