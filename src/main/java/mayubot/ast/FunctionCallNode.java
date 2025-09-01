package mayubot.ast;

public class FunctionCallNode implements AbstractNode{
  private final String name;
  private final AbstractNode expression;
  private static final FunctionCallTable ftable = new FunctionCallTable();

  public FunctionCallNode(String functionname, AbstractNode expression){
    if(!ftable.Contains(functionname.toLowerCase().strip())){
      throw new IllegalArgumentException("FunctionCallNode(): " + 
          "Unknown function name given as argument");
    }
    this.name = functionname;
    this.expression = expression;
  }

  @Override
  public NodeType GetType(){ return NodeType.FunctionCall;}

  @Override
  public EvaluationResult GetResult(){
    return ftable.Get(name.toLowerCase().strip()).Invoke(expression);
  }
}
