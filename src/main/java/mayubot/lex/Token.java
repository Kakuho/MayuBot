package mayubot.lex;

import java.util.Optional;

public class Token{
  // remember in java everyting is in heap
  private TokenType type;
  private Optional<Double> doubleVal;
  private Optional<Long> intVal;
  
  public Token(long val){
    this.intVal = Optional.of(val);
    this.type = TokenType.Integral;

    this.doubleVal = Optional.empty();
  }
  
  public Token(double val){
    this.doubleVal = Optional.of(val);
    this.type = TokenType.Floating;

    this.intVal = Optional.empty();
  }

  public Token(TokenType type){
    this.type = type;

    this.intVal = Optional.empty();
    this.doubleVal = Optional.empty();
  }

  public TokenType GetType(){ return type;}

  public long GetIntegral(){ return intVal.get();}
  public double GetFloating(){ return doubleVal.get();}
}
