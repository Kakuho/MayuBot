package mayubot;

import mayubot.drivers.CliDriver;
import mayubot.drivers.DiscordDriver;
import mayubot.drivers.Mode;
import mayubot.drivers.ResetMode;

public class App {
  public static void main(String[] args) {
    /*
    var driver = new CliDriver(Mode.Standard);
    driver.Run();
    */

    var driver = new DiscordDriver(Mode.Standard, ResetMode.Probabilistic);
    driver.Run();
  }
}
