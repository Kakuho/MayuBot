package mayubot.ast;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public final class FunctionTest{
  @Test
  public void BasicFactorial(){
    //    "factorial"
    // FC
    //    4
    var root = new FunctionCallNode("factorial", new IntegerNode(4));
    var result = root.GetResult();
    assertTrue(result.IsIntegral());
    assertTrue(result.GetInt() == 24);
  }

  @Test
  public void FactorialOperand(){
    //      "factorial"
    //    FC
    //      4
    //  + 
    //      2
    //    *
    //      3
    var root = new OpNode(
      Operator.Plus,
      new FunctionCallNode(
        "factorial", 
        new IntegerNode(4)
      ),
      new OpNode(
        Operator.Multiply,
        new IntegerNode(2),
        new IntegerNode(3)
      )
    );
    var result = root.GetResult();
    assertTrue(result.IsIntegral());
    assertTrue(result.GetInt() == 30);
  }
}
