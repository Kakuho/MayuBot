package mayubot.util;

public class Math{
  public class OperandTypeException{};

  public static long Factorial(long num){
    long val = 1;
    for(long i = 1; i <= num; i++){
      val *= i;
    }
    return val;
  }

  public static long Sum(long num){
    long val = 0;
    for(long i = 0; i <= num; i++){
      val += i;
    }
    return val;
  }
}
