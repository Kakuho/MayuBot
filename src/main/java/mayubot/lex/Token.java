package mayubot.lex;

import java.util.Optional;

public class Token{
  // remember in java everyting is in heap
  private TokenType type;
  // this is my attempt of making a union in java
  private Optional<Double> doubleVal    = Optional.empty();
  private Optional<Long> intVal         = Optional.empty();
  private Optional<String> identVal     = Optional.empty();
  
  public Token(long val){
    this.intVal = Optional.of(val);
    this.type = TokenType.Integral;
  }
  
  public Token(double val){
    this.doubleVal = Optional.of(val);
    this.type = TokenType.Floating;
  }

  public Token(String val){
    this.identVal = Optional.of(val);
    this.type = TokenType.Identifier;
  }

  public Token(TokenType type){
    this.type = type;
  }

  public TokenType GetType(){ return type;}
  public long GetIntegral(){ return intVal.get();}
  public double GetFloating(){ return doubleVal.get();}
  public String GetIdentifier(){ return identVal.get();}
}
