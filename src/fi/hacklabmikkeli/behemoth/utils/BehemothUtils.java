package fi.hacklabmikkeli.behemoth.utils;

public class BehemothUtils {
  
  public static int resolveMotorSpeed(int baseValue, int speedValue) {
    speedValue -= baseValue;

    if (speedValue < 500) {
      return 500 - speedValue;
    } else {
      return speedValue - 500;
    }
  }
}
