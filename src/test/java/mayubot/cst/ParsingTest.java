package mayubot.cst;

import mayubot.lex.Token;
import mayubot.lex.TokenType;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public final class ParsingTest{
  
  @Test
  public void ParseLiterals(){
    ArrayList<Token> tokens = new ArrayList<Token>(
        List.of(
          new Token(10),
          new Token(3.5)
        )
    );
    var parser = new Parser(tokens);
    var intnode = parser.ParseIntegralNode();
    assertTrue(intnode instanceof IntegralNode);

    var floatnode = parser.ParseFloatingNode();
    assertTrue(floatnode instanceof FloatingNode);
  }

  @Test
  public void ParseB(){
    ArrayList<Token> tokens = new ArrayList<Token>(
        List.of(
          new Token(10),
          new Token(3.5)
        )
    );
    var parser = new Parser(tokens);

    var bnode1 = parser.ParseB();
    assertTrue(bnode1 instanceof BNode);
    assertTrue(bnode1.GetChildren().get(0) instanceof IntegralNode);

    var bnode2 = parser.ParseB();
    assertTrue(bnode2 instanceof BNode);
    assertTrue(bnode2.GetChildren().get(0) instanceof FloatingNode);
  }

  @Test
  public void ParseF(){
    ArrayList<Token> tokens = new ArrayList<Token>(
        List.of(
          // 2^2
          new Token(2),
          new Token(TokenType.Raise),
          new Token(2)
        )
    );

    var parser = new Parser(tokens);

    var fnode = parser.ParseF();
    assertTrue(fnode instanceof FNode);
    assertTrue(fnode.GetChildren().get(0) instanceof BNode);
    assertTrue(fnode.GetChildren().get(1) instanceof BNode);
  }

  @Test
  public void ParseT(){
    ArrayList<Token> tokens = new ArrayList<Token>(
        List.of(
          // 2 * 2
          new Token(2),
          new Token(TokenType.Multiply),
          new Token(2),
          new Token(TokenType.Eof)
        )
    );

    var parser = new Parser(tokens);

    var tnode = parser.ParseT();
    assertTrue(tnode instanceof TNode);
    assertTrue(tnode.GetChildren().size() == 2);
    assertTrue(tnode.GetChildren().get(0) instanceof FNode);
    assertTrue(tnode.GetChildren().get(1) instanceof TNode);

    // the child
    var childTnode = tnode.GetChildren().get(1);
    assertTrue(childTnode.GetChildren().size() == 1);
    assertTrue(childTnode.GetChildren().get(0) instanceof FNode);
  }

  @Test
  public void ParseTMultChain(){
    ArrayList<Token> tokens = new ArrayList<Token>(
        List.of(
          // 2 * 2 * 2 * 2 
          new Token(2),
          new Token(TokenType.Multiply),
          new Token(2),
          new Token(TokenType.Multiply),
          new Token(2),
          new Token(TokenType.Multiply),
          new Token(2),
          new Token(TokenType.Eof)
        )
    );
    var parser = new Parser(tokens);
    var tnode = parser.ParseT();
    CstPrinter.PrintRoot(tnode);
  }
  
  @Test
  public void StrangeCase(){
    ArrayList<Token> twoTimesTwoTokens = new ArrayList<Token>(
        List.of(
          // 2 * 2
          new Token(2),
          new Token(TokenType.Multiply),
          new Token(2),
          new Token(TokenType.Eof)
        )
    );

    var parser = new Parser(twoTimesTwoTokens);

    var tnode = parser.ParseT();
    assertTrue(tnode instanceof TNode);
    assertTrue(tnode.GetChildren().size() == 2);
    assertTrue(tnode.GetChildren().get(0) instanceof FNode);
    assertTrue(tnode.GetChildren().get(1) instanceof TNode);

    // the child
    var childTnode = tnode.GetChildren().get(1);
    assertTrue(childTnode.GetChildren().size() == 1);
    assertTrue(childTnode.GetChildren().get(0) instanceof FNode);

    ArrayList<Token> fourTimesFourTokens = new ArrayList<Token>(
        List.of(
          // 2 * 2
          new Token(4),
          new Token(TokenType.Multiply),
          new Token(4),
          new Token(TokenType.Eof)
        )
    );

    parser.Reset();
    parser.SetTokens(fourTimesFourTokens);

    tnode = parser.ParseT();
    assertTrue(tnode instanceof TNode);
    assertTrue(tnode.GetChildren().size() == 2);
    assertTrue(tnode.GetChildren().get(0) instanceof FNode);
    assertTrue(tnode.GetChildren().get(1) instanceof TNode);

    // the child
    childTnode = tnode.GetChildren().get(1);
    assertTrue(childTnode.GetChildren().size() == 1);
    assertTrue(childTnode.GetChildren().get(0) instanceof FNode);
  }
}
