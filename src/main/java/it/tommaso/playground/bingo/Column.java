package it.tommaso.playground.bingo;

import java.util.Arrays;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Column {

  private static final Logger LOGGER = LoggerFactory.getLogger(Column.class);

  private static final int MAX_CAPACITY = 3;

  private final int id;
  private final int[] values = new int[MAX_CAPACITY];

  public Column(final int id) {
    this.id = id;
  }

  public boolean hasSpace() {
    return Arrays.stream(values).filter(value -> value != 0).count() < MAX_CAPACITY;
  }

  public boolean isEmpty() {
    return Arrays.stream(values).filter(value -> value == 0).count() == MAX_CAPACITY;
  }

  public boolean addNumber(final int number) {
    LOGGER.debug("Trying to add {} to the Column number {}", number, id);
    if (!hasSpace()) {
      LOGGER.debug("column is already full {}", this);
      return false;
    }
    return IntStream.range(0, 3).anyMatch(index -> {
      if (values[index] == 0) {
        values[index] = number;
        return true;
      }
      return false;
    });
  }

  public long size() {
    return Arrays.stream(values).filter(value -> value != 0).count();
  }

  public Column sort() {
    Arrays.sort(values);
    return this;
  }

  public int pollFirst() {
    for (int index = 0; index < 3; index++) {
      if (values[index] != 0) {
        int toReturn = values[index];
        values[index] = 0;
        return toReturn;
      }
    }
    return -1;
  }

  public int getId() {
    return id;
  }
}
