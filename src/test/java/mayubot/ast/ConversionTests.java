package mayubot.ast;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public final class ConversionTests{
  @Test
  public void BasicAdd(){
    //    3.5
    //  +
    //    2
    var root = new OpNode(Operator.Plus, new FloatingNode(3.5), new IntegerNode(2));
    var result = root.GetResult();
    assertTrue(result.IsFloating());
    assertTrue(DoubleComparer.Compare(result.GetFloating(), 5.5));
  }
}
