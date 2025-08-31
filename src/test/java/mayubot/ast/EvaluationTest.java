package mayubot.ast;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public final class EvaluationTest{
  @Test
  public void IntegerPlusInteger(){
    var root = new OpNode(Operator.Plus, new IntegerNode(10), new IntegerNode(10));
    var result = root.GetResult();
    assertTrue(result.IsIntegral());
    assertTrue(result.GetInt() == 20);
  }

  @Test
  public void FloatingPlusFloating(){
    var threepointfive = new FloatingNode(3.5);
    var result1 = threepointfive.GetResult();
    assertTrue(result1.IsFloating());
    assertTrue(result1.GetFloating() == 3.5);

    var sixpointnine = new FloatingNode(6.9);
    var result2 = sixpointnine.GetResult();
    assertTrue(result2.IsFloating());
    assertTrue(DoubleComparer.Compare(result2.GetFloating(), 6.9));

    var root = new OpNode(Operator.Plus, new FloatingNode(3.5), new FloatingNode(6.98));
    var result = root.GetResult();
    assertTrue(result.IsFloating());
    assertTrue(DoubleComparer.Compare(result.GetFloating(), 10.48));
  }

  @Test
  public void HierarchalInt(){
    //  tests hierarchal ints
    //      4
    //    *
    //      3
    //  +
    //    2
    var lhstree = new OpNode(Operator.Multiply, new IntegerNode(3), new IntegerNode(4));
    assertTrue(lhstree.GetResult().IsIntegral());
    assertTrue(lhstree.GetResult().GetInt() == 12);

    var root = new OpNode(Operator.Plus, lhstree, new IntegerNode(2));
    var result = root.GetResult();
    assertTrue(result.IsIntegral());
    assertTrue(result.GetInt() == 14);
  }

  @Test
  public void HierarchalFloats(){
    //  tests hierarchal floats
    //      4.334
    //    *
    //      3.67
    //  +
    //    2.5
    var lhstree = new OpNode(Operator.Multiply, new FloatingNode(4.334), new FloatingNode(3.67));
    assertTrue(lhstree.GetResult().IsFloating());
    assertTrue(DoubleComparer.Compare(lhstree.GetResult().GetFloating(), 15.90578));

    var root = new OpNode(Operator.Plus, lhstree, new FloatingNode(2.5));
    var result = root.GetResult();
    assertTrue(result.IsFloating());
    assertTrue(DoubleComparer.Compare(result.GetFloating(), 18.40578));
  }

}
