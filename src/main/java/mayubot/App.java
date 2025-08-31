package mayubot;

import mayubot.drivers.CliDriver;
import mayubot.drivers.DiscordDriver;

public class App {
  public static void main(String[] args) {
    /*
    var driver = new CliDriver(CliDriver.Mode.Standard);
    driver.Run();
    */
    var driver = new DiscordDriver();
  }
}
