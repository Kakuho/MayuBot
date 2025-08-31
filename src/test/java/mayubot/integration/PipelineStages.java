package mayubot.integration;

import mayubot.lex.Lexer;
import mayubot.lex.TokenType;
import mayubot.lex.Token;
import mayubot.cst.Parser;
import mayubot.cst.CstPrinter;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PipelineStagesTests{
  @Test
  public void TestLexingAndParsing(){
    String input = "102 * 3049";
    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 4);
    assertTrue(tokens.get(0).GetType() == TokenType.Integral);
    assertTrue(tokens.get(0).GetIntegral() == 102);

    assertTrue(tokens.get(1).GetType() == TokenType.Multiply);


    assertTrue(tokens.get(2).GetType() == TokenType.Integral);
    assertTrue(tokens.get(2).GetIntegral() == 3049);


    var parser = new Parser(tokens);
    var tnode = parser.ParseT();

    CstPrinter.PrintRoot(tnode);

    var astroot = tnode.GetAst();
    var evalresult = astroot.GetResult();


    assertTrue(evalresult.IsIntegral());
    System.out.println(evalresult.GetInt());
  }

  @Test
  public void TestResettingParserLexer(){
    String input = "2 * 2";

    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 4);

    var parser = new Parser(tokens);
    var tnode = parser.ParseT();

    var astroot = tnode.GetAst();
    var evalresult = astroot.GetResult();

    assertTrue(evalresult.IsIntegral());
    assertTrue(evalresult.GetInt() == 4);

    lexer.Reset();
    tokens = lexer.Process("4 * 4");
    assertTrue(tokens.size() == 4);

    parser.Reset();
    parser.SetTokens(tokens);
    tnode = parser.ParseT();

    astroot = tnode.GetAst();
    evalresult = astroot.GetResult();

    assertTrue(evalresult.IsIntegral());
    assertTrue(evalresult.GetInt() == 16);
  }

  @Test
  public void TestStart(){
    String input = "2 + 5 * 3";

    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 6);

    var parser = new Parser(tokens);
    var cstRoot = parser.Parse();

    var astroot = cstRoot.GetAst();
    var evalresult = astroot.GetResult();

    assertTrue(evalresult.IsIntegral());
    assertTrue(evalresult.GetInt() == 17);
  }

}
