package it.tommaso.playground.bingo;

import static it.tommaso.playground.bingo.ColumnUtil.getColumnNumber;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Row {

  private static final Logger LOGGER = LoggerFactory.getLogger(Row.class);

  private static final int MAX_CAPACITY = 5;

  private final int id;
  private final int[] values = {-1, -1, -1, -1, -1, -1, -1, -1, -1};

  public Row(final int id) {
    this.id = id;
  }

  public boolean hasSpace() {
    return Arrays.stream(values).filter(i -> i != -1).count() < MAX_CAPACITY;
  }

  public boolean addNumber(final int number) {
    LOGGER.debug("Trying to add {} to the Row number {}", number, id);
    if (!hasSpace()) {
      LOGGER.debug("row is already full {}", this);
      return false;
    }
    if (values[getColumnNumber(number) - 1] != -1) {
      LOGGER.debug("There is already a number from the same column {}", this);
      return false;
    }
    values[getColumnNumber(number) - 1] = number;
    return true;
  }

  public int[] getValues() {
    return values;
  }

  @Override
  public String toString() {
    return Arrays.toString(values).replaceAll("-1", " ");
  }
}
