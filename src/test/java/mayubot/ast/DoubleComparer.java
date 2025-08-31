package mayubot.ast;

import java.lang.Math;

class DoubleComparer{
  private static double epsilon = 0.005;
  public static boolean Compare(double val1, double val2){
    if(Math.abs(val1 - val2) < epsilon){
      return true;
    }
    else{
      return false;
    }
  }
}
