package mayubot.ast;

import mayubot.ast.EvaluationResult;

public interface AbstractNode{
  public enum NodeType{
    OpNode, IntegerNode, FloatNode, FunctionCall
  }

  public abstract NodeType GetType();
  public abstract EvaluationResult GetResult();
}
