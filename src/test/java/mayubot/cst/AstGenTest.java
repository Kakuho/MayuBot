package mayubot.cst;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import mayubot.lex.TokenType;

public final class AstGenTest{
  @Test
  public void GenerateArithmetics(){
    var integral = new IntegralNode(100);
    var intast = integral.GetAst();
    assertTrue(intast instanceof mayubot.ast.IntegerNode);

    var floating = new FloatingNode(100.5);
    var floatast = floating.GetAst();
    assertTrue(floatast instanceof mayubot.ast.FloatingNode);
  }

  @Test
  public void GenerateBnode(){
    var bnodeIntegral = new BNode(new IntegralNode(100));
    var bintast = bnodeIntegral.GetAst();
    assertTrue(bintast instanceof mayubot.ast.IntegerNode);

    var bnodeFloating = new BNode(new FloatingNode(100.5));
    var bfloatast = bnodeFloating.GetAst();
    assertTrue(bfloatast instanceof mayubot.ast.FloatingNode);
  }

  @Test
  public void GenerateFnode(){
    var fnode = new FNode(
      new BNode(
        new IntegralNode(100)
      ),
      new BNode(
        new FloatingNode(100)
      )
    );

    var ast = fnode.GetAst();
    assertTrue(ast instanceof mayubot.ast.OpNode);


    var fnodeSingle = new FNode(
      new BNode(
        new FloatingNode(100)
      )
    );
    var singleast = fnodeSingle.GetAst();
    assertTrue(singleast instanceof mayubot.ast.FloatingNode);
  }

  @Test
  public void GenerateTnode(){
    // 2 * 2
    var tnode = new TNode(
      TokenType.Multiply,
      new FNode(
        new BNode(
          new IntegralNode(2)
        )
      ),
      new TNode(
        new FNode(
          new BNode(
            new IntegralNode(2)
          )
        )
      )
    );

    var ast = tnode.GetAst();
    assertTrue(ast instanceof mayubot.ast.OpNode);
  }

  @Test
  public void GenerateTnodeMultiplyChain(){
    // 2 * 2 * 2 * 2
  }
}
