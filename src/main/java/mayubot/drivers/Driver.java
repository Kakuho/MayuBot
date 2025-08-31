package mayubot.drivers;

public interface Driver{
  public enum Mode{Standard, Fibonacci, Even}
  public abstract long GetCurrentCount();
  public abstract long GetPriotCount();
}
