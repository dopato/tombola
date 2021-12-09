package it.tommaso.playground.bingo;

public final class ColumnUtil {

  private ColumnUtil() {
    //util class not instantiable
  }

  public static int getColumnNumber(final int number) {
    if (number < 10) {
      return 1;
    }
    if (number == 90) {
      return 9;
    }
    return number / 10 + 1;
  }
}
