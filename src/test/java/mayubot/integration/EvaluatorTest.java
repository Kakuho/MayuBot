package mayubot.integration;

import mayubot.Evaluator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class EvaluatorTest{
  @Test
  public void EvaluatorGetsResetted(){
    var evaluator = new Evaluator();

    var result = evaluator.Process("1 * 1");
    assertTrue(result.IsIntegral());
    assertTrue(result.GetInt() == 1);

    result = evaluator.Process("2 * 2");
    assertTrue(result.IsIntegral());
    assertTrue(result.GetInt() == 4);

    result = evaluator.Process("3 * 3");
    assertTrue(result.IsIntegral());
    assertTrue(result.GetInt() == 9);

    result = evaluator.Process("factorial(3) * 3");
    assertTrue(result.IsIntegral());
    assertTrue(result.GetInt() == 18);
  }
}
