package mayubot;

import mayubot.lex.Lexer;
import mayubot.cst.Parser;

import mayubot.ast.AbstractNode;
import mayubot.ast.EvaluationResult;
import mayubot.ast.OpNode;

// this is an encapsulation of the entire pipeline

public class Evaluator{
  private Lexer lexer;
  private Parser parser;

  public Evaluator(){
    this.lexer = new Lexer();
    this.parser = new Parser();
  }

  public void Reset(){
    lexer.Reset();
    parser.Reset();
  }

  public EvaluationResult Process(String message){
    Reset();
    var tokens = lexer.Process(message);
    parser.SetTokens(tokens);
    var cstRoot = parser.Parse();
    var astRoot = cstRoot.GetAst();
    return astRoot.GetResult();
  }
}
