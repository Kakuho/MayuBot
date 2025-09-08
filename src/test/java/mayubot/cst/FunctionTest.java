package mayubot.cst;

import mayubot.lex.Token;
import mayubot.lex.TokenType;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public final class FunctionTest{
  
  @Test
  public void ParseSimpleFactorial(){
    ArrayList<Token> tokens = new ArrayList<Token>(
        List.of(
          new Token("factorial"),
          new Token(TokenType.LeftBracket),
          new Token(10),
          new Token(TokenType.RightBracket)
        )
    );
    var parser = new Parser(tokens);
    var functionCall = parser.ParseFunctionCall();
    assertTrue(functionCall instanceof FunctionCallNode);
    assertTrue(functionCall.GetChildren().size() == 2);

    var nameNode = functionCall.GetChildren().get(0);
    assertTrue(nameNode instanceof FunctionNameNode);

    var eNode = functionCall.GetChildren().get(1);
    assertTrue(eNode instanceof ENode);
  }
}
