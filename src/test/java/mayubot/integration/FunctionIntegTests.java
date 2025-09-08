package mayubot.integration;

import mayubot.lex.Lexer;
import mayubot.lex.TokenType;
import mayubot.lex.Token;
import mayubot.cst.Parser;
import mayubot.DebugPrinter;
import mayubot.cst.CstPrinter;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FunctionIntegTests{

  @Test
  public void Factorial4Test(){
    //    "factorial"
    // FC
    //    4
    String input = "factorial(4)";
    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 5);

    assertTrue(tokens.get(0).GetType() == TokenType.Identifier);
    DebugPrinter.Print(tokens.get(0).GetIdentifier());
    assertTrue(tokens.get(0).GetIdentifier().equals("factorial"));    // in java, operator== compares references, not
                                                                      // values

    assertTrue(tokens.get(1).GetType() == TokenType.LeftBracket);

    assertTrue(tokens.get(2).GetType() == TokenType.Integral);
    assertTrue(tokens.get(2).GetIntegral() == 4);

    assertTrue(tokens.get(3).GetType() == TokenType.RightBracket);

    assertTrue(tokens.get(4).GetType() == TokenType.Eof);

    var parser = new Parser(tokens);
    var root = parser.Parse();

    var astroot = root.GetAst();
    var evalresult = astroot.GetResult();

    assertTrue(evalresult.IsIntegral());
    assertTrue(evalresult.GetInt() == 24);
  }

  @Test
  public void FactorialbigTest(){
    //      "factorial"
    //    FC
    //      4
    //  +
    //      2
    //    *
    //      3
    String input = "factorial(4) + 2 * 3";
    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    /*
    assertTrue(tokens.size() == 5);

    assertTrue(tokens.get(0).GetType() == TokenType.Identifier);
    DebugPrinter.Print(tokens.get(0).GetIdentifier());
    assertTrue(tokens.get(0).GetIdentifier().equals("factorial"));    // in java, operator== compares references, not
                                                                      // values
    assertTrue(tokens.get(1).GetType() == TokenType.LeftBracket);

    assertTrue(tokens.get(2).GetType() == TokenType.Integral);
    assertTrue(tokens.get(2).GetIntegral() == 4);

    assertTrue(tokens.get(3).GetType() == TokenType.RightBracket);

    assertTrue(tokens.get(4).GetType() == TokenType.Eof);
    */

    var parser = new Parser(tokens);
    var root = parser.Parse();

    var astroot = root.GetAst();
    var evalresult = astroot.GetResult();

    assertTrue(evalresult.IsIntegral());
    assertTrue(evalresult.GetInt() == 30);
  }

}
