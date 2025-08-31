package mayubot.lex;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class LexingTests{
  @Test
  public void StringLengthTest(){
    String sample = "Kuromi";
    assertTrue(sample.length() == 6);

    String value = "123456";
    long yaya = Long.valueOf(value);
    assertTrue(yaya == 123456);
  }

  @Test
  public void LiteralsIntegralUnit(){
    String input = "9";
    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 2);
    assertTrue(tokens.get(0).GetType() == TokenType.Integral);
    assertTrue(tokens.get(0).GetIntegral() == 9);
  }

  @Test
  public void LiteralsIntegral(){
    String input = "123456";
    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 2);
    assertTrue(tokens.get(0).GetType() == TokenType.Integral);
    assertTrue(tokens.get(0).GetIntegral() == 123456);
  }

  @Test
  public void LiteralsFloating(){
    String input = "342.3456 ";
    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 2);
    assertTrue(tokens.get(0).GetType() == TokenType.Floating);
    assertTrue(tokens.get(0).GetFloating() == 342.3456);
  }

  @Test
  public void IntAddInt(){
    String input = "102 + 3049";
    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 4);
    assertTrue(tokens.get(0).GetType() == TokenType.Integral);
    assertTrue(tokens.get(0).GetIntegral() == 102);

    assertTrue(tokens.get(1).GetType() == TokenType.Plus);

    assertTrue(tokens.get(2).GetType() == TokenType.Integral);
    assertTrue(tokens.get(2).GetIntegral() == 3049);
  }

  @Test
  public void FloatMultipleFloat(){
    String input = "1.2345 * 33.576";

    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 4);
    assertTrue(tokens.get(0).GetType() == TokenType.Floating);
    assertTrue(tokens.get(0).GetFloating() == 1.2345);

    assertTrue(tokens.get(1).GetType() == TokenType.Multiply);

    assertTrue(tokens.get(2).GetType() == TokenType.Floating);
    assertTrue(tokens.get(2).GetFloating() == 33.576);
  }

  @Test
  public void StrangeCase(){
    String input = "2 * 2";

    var lexer = new Lexer(input);
    List<Token> tokens = lexer.Process();
    assertTrue(tokens.size() == 4);
    assertTrue(tokens.get(0).GetType() == TokenType.Integral);
    assertTrue(tokens.get(0).GetIntegral() == 2);

    assertTrue(tokens.get(1).GetType() == TokenType.Multiply);

    assertTrue(tokens.get(2).GetType() == TokenType.Integral);
    assertTrue(tokens.get(2).GetIntegral() == 2);

    lexer.Reset();
    tokens = lexer.Process("4 * 4");

    assertTrue(tokens.size() == 4);
    assertTrue(tokens.get(0).GetType() == TokenType.Integral);
    assertTrue(tokens.get(0).GetIntegral() == 4);

    assertTrue(tokens.get(1).GetType() == TokenType.Multiply);

    assertTrue(tokens.get(2).GetType() == TokenType.Integral);
    assertTrue(tokens.get(2).GetIntegral() == 4);
  }


  @Test
  public void FailingMessage(){
    String input = "im gonna fail!";
    var lexer = new Lexer(input);
  }

}
