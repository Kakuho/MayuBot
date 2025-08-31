package mayubot.ast;

import mayubot.ast.EvaluationResult;

public class IntegerNode implements AbstractNode{
  private final long val;

  public IntegerNode(long val){
    this.val = val;
  }

  public long GetVal(){ return val;}

  public NodeType GetType(){ return NodeType.IntegerNode;}

  public EvaluationResult GetResult(){
    return new EvaluationResult(val);
  }
}
