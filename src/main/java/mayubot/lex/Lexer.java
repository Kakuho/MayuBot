package mayubot.lex;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class Lexer{
  private class DigitSpan{
    // digit inclusive
    int start;
    int end;

    public int Length(){
      return end - start;
    }

    public DigitSpan(int start, int end){
      this.start = start;
      this.end = end;
    }
  }

  private class Span{
    // digit inclusive
    int start;
    int end;

    public int Length(){
      return end - start;
    }

    public Span(int start, int end){
      this.start = start;
      this.end = end;
    }
  }

  private String input;
  private int currentIndex;

  public Lexer(){
    this.input = "";
    this.currentIndex = 0;
  }

  public Lexer(String input){
    this.input = input;
    this.currentIndex = 0;
  }

  public void Reset(){
    this.input = "";
    this.currentIndex = 0;
  }

  public List<Token> Process(String expression){
    this.input = expression;
    return Process();
  }

  public List<Token> Process(){
    List<Token> tokens = new ArrayList<Token>();
    while(currentIndex < input.length()){
      char currentCh = input.charAt(currentIndex);
      if(IsDigit(currentCh)){
        var token = ProcessDigit(currentIndex);
        if(token.isPresent()){
          tokens.add(token.get());
        }
        else{
          throw new RuntimeException("Lexer.Process(): Digit branch failed to lex");
        }
      }
      else if(IsWhitespace(currentCh)){
        SkipWhitespace();
      }
      else{
        var token = ProcessNonDigit(currentCh);
        if(token.isPresent()){
          tokens.add(token.get());
        }
        else{
          throw new RuntimeException("Lexer.Process(): Unknown non digit");
        }
      }
    }
    tokens.add(new Token(TokenType.Eof));
    return tokens;
  }


  public Optional<Token> ProcessNonDigit(char ch){
    switch(ch){
      case '+': 
        currentIndex++;
        return Optional.of(new Token(TokenType.Plus));
      case '-': 
        currentIndex++;
        return Optional.of(new Token(TokenType.Minus));
      case '/': 
        currentIndex++;
        return Optional.of(new Token(TokenType.Divide));
      case '*': 
        currentIndex++;
        return Optional.of(new Token(TokenType.Multiply));
      case '^': 
        currentIndex++;
        return Optional.of(new Token(TokenType.Raise));
      default:
        return Optional.empty();
    }
  }

  public void SkipWhitespace(){
    var span = WhitespaceSpanAt(currentIndex);
    currentIndex += span.Length() + 1;
  }

  public Span WhitespaceSpanAt(int startIndex){
    int index = startIndex;
    while(IsWhitespace(input.charAt(index))){
      if(index + 1 == input.length()){
        break;
      }
      else if(!IsWhitespace(input.charAt(index+1))){
        break;
      }
      else{
        index++;
      }
    }
    return new Span(startIndex, index);
  }

  public int NextNonWhiteSpaceIndex(){
    int nonwhiteindex = currentIndex;
    char ch = input.charAt(nonwhiteindex);
    while(IsWhitespace(ch) && (nonwhiteindex < input.length() - 1)){
      /*

      */
      nonwhiteindex++;
      if(nonwhiteindex + 1 < input.length()){
        ch = input.charAt(nonwhiteindex);
        break;
      }
    }
    return nonwhiteindex;
  }

  public Optional<Token> ProcessDigit(int startIndex){
    var currentSpan = DigitSpanAt(startIndex);
    if(currentSpan.end == input.length() -1){
      return ProcessIntegral(currentSpan);
    }
    else{
      if(input.charAt(currentSpan.end+1) == '.'){
        // try processing double
        var fractionalSpan = DigitSpanAt(currentSpan.end + 2);
        String floatingrep = input.substring(currentSpan.start, fractionalSpan.end +1);
        UpdateIndexBy(currentSpan);
        UpdateIndexBy(fractionalSpan);
        currentIndex += 1;
        return Optional.of(new Token(Double.parseDouble(floatingrep)));
      }
      else{
        return ProcessIntegral(currentSpan);
      }
    }
  }

  public Optional<Token> ProcessIntegral(DigitSpan span){
    if(span.start == span.end){
      char integralrep = input.charAt(span.start);
      UpdateIndexBy(span);
      return Optional.of(new Token(Character.getNumericValue(integralrep)));
    }
    else{
      String integralrep = input.substring(span.start, span.end+1);
      UpdateIndexBy(span);
      return Optional.of(new Token(Long.valueOf(integralrep)));
    }
  }

  public DigitSpan DigitSpanAt(int start){
    // 012345678
    // 342.3456 
    int index = start;
    while(IsDigit(input.charAt(index))){
      if(index + 1 == input.length()){
        break;
      }
      else if(!IsDigit(input.charAt(index+1))){
        break;
      }
      else{
        index++;
      }
    }
    return new DigitSpan(start, index);
  }

  public void UpdateIndexBy(DigitSpan span){
    currentIndex += span.Length() + 1;
  }

  public boolean IsDigit(char ch){
    switch(ch){
      case '0': case '1': case '2': 
      case '3': case '4': case '5':
      case '6': case '7': case '8':
      case '9': 
        return true;
      default:
        return false;
    }
  }

  public boolean IsWhitespace(char ch){
    switch(ch){
      case ' ': case '\t': case '\n': 
      case '\r': case '\f':
        return true;
      default:
        return false;
    }
  }
}
