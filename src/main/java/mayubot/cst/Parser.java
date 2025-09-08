package mayubot.cst;

import mayubot.DebugPrinter;
import mayubot.lex.Token;
import mayubot.lex.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.lang.RuntimeException;

public class Parser{
  private int index = 0;
  private List<Token> tokens;

  public Parser(){
  }

  public Parser(ArrayList<Token> tokens){
    this.tokens = tokens;     // java move semantics lolol
    tokens = null;
  }

  public Parser(List<Token> tokens){
    this.tokens = tokens;     // java move semantics lolol
    tokens = null;
  }

  public void Reset(){ 
    this.index = 0; 
    this.tokens = null;
  }

  public void SetTokens(List<Token> tokens){
    this.tokens = tokens;
  }

  private Token CurrentToken(){
    return tokens.get(index);
  }

  private boolean MatchTokenType(TokenType type){
    if(CurrentToken().GetType() != type){
      return false;
    }
    else{
      index++;
      return true;
    }
  }

  public ConcreteNode ParseFunctionName(){
    var current = CurrentToken();
    if(current.GetType() != TokenType.Identifier){
      throw new RuntimeException("Parser.ParseFunctionName(): " +
          "current token is not an identifier");
    }
    else{
      index++;
      return new FunctionNameNode(current.GetIdentifier());
    }
  }

  public ConcreteNode ParseFunctionCall(){
    var nameNode = ParseFunctionName();
    if(nameNode == null){
      throw new RuntimeException("Parser.ParseFunctionCall(): " +
          "trying to parse function name returns null");
    }
    if(!MatchTokenType(TokenType.LeftBracket)){
      throw new RuntimeException("Parser.ParseFunctionCall(): " +
          "trying to parse left bracket fail");
    }
    var expressionNode = ParseE();
    if(expressionNode == null){
      throw new RuntimeException("Parser.ParseFunctionCall(): " +
          "trying to parse expression node fail");
    }
    if(!MatchTokenType(TokenType.RightBracket)){
      throw new RuntimeException("Parser.ParseFunctionCall(): " +
          "trying to parse right bracket fail");
    }
    return new FunctionCallNode((FunctionNameNode)nameNode, (ENode)expressionNode);
  }

  public ConcreteNode ParseIntegralNode(){
    var current = CurrentToken();
    if(current.GetType() != TokenType.Integral){
      throw new RuntimeException("Parser.ParseIntegralNode(): current token is not an integeral");
    }
    else{
      index++;
      //DebugPrinter.Print(String.format("Sucessfully parse integral node, index = %d", index));
      return new IntegralNode(current.GetIntegral());
    }
  }

  public ConcreteNode ParseFloatingNode(){
    var current = CurrentToken();
    if(current.GetType() != TokenType.Floating){
      throw new RuntimeException("Parser.ParseFloatingNode(): current token is not a floating");
    }
    else{
      index++;
      return new FloatingNode(current.GetFloating());
    }
  }

  public ConcreteNode ParseB(){
    var current = CurrentToken();
    switch(current.GetType()){
      case Integral: 
        return new BNode((IntegralNode)ParseIntegralNode());
      case Floating:
        return new BNode((FloatingNode)ParseFloatingNode());
      case Identifier:
        return new BNode((FunctionCallNode)ParseFunctionCall());
      default: 
        throw new RuntimeException("Parser.ParseB(): current token is neither integral or floating");
    }
  }

  public ConcreteNode ParseF(){
    var firstb = ParseB();
    if(firstb == null){
      throw new RuntimeException("Parser.ParseF(): Failed to parse first B node");
    }
    var lookahead = CurrentToken();
    if(lookahead.GetType() == TokenType.Raise){
      index++;
      var secondb = (BNode)ParseB();
      if(secondb == null){
        throw new RuntimeException("Parser.ParseF(): Failed to parse second B node given operator");
      }
      return new FNode((BNode)firstb, (BNode)secondb);
    }
    else{
      return new FNode((BNode)firstb);
    }
  }

  public ConcreteNode ParseT(){
    var fnode = ParseF();
    if(fnode == null){
      throw new RuntimeException("Parser.ParseT(): Failed to parse first F node");
    }
    var lookahead = CurrentToken();
    if(lookahead.GetType() == TokenType.Divide || lookahead.GetType() == TokenType.Multiply){
      var operator = lookahead.GetType();
      index++;
      var tnode = ParseT();
      if(tnode == null){
        throw new RuntimeException("Parser.ParseT(): Failed to parse Aux T node given operator");
      }
      return new TNode(operator, (FNode)fnode, (TNode)tnode);
    }
    else{
      return new TNode((FNode)fnode);
    }
  }

  public ConcreteNode ParseE(){
    //DebugPrinter.Print("Inside ParseE");
    var tnode = ParseT();
    if(tnode == null){
      throw new RuntimeException("Parser.ParseE(): Failed to parse first T node");
    }
    //DebugPrinter.Print("valid T node constructed");
    var lookahead = CurrentToken();
    if(lookahead.GetType() == TokenType.Plus || lookahead.GetType() == TokenType.Minus){
      var operator = lookahead.GetType();
      index++;
      var enode = ParseE();
      if(enode == null){
        throw new RuntimeException("Parser.ParseE(): Failed to parse Aux E node given operator");
      }
      return new ENode(operator, (TNode)tnode, (ENode)enode);
    }
    else{
      return new ENode((TNode)tnode);
    }
  }

  public ConcreteNode Parse(){
    return ParseE();
  }
}
