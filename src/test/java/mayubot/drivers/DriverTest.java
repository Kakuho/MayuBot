package mayubot.drivers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

interface G{
  public default long GetMeBum(){
    return 10;
  }
}

class A implements G{

}

public final class DriverTest{
  @Test
  public void InterfaceTest(){
    var a = new A();
    assertTrue(a.GetMeBum() == 10);
  }
}
