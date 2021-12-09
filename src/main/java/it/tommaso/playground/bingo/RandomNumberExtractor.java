package it.tommaso.playground.bingo;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class RandomNumberExtractor implements NumberExtractor {

  private static final int MAX_CAPACITY = 90;

  private final ArrayList<Integer> numbers = new ArrayList<>(MAX_CAPACITY);

  private final Random random = ThreadLocalRandom.current();

  public RandomNumberExtractor() {
    IntStream.range(1, 91).forEach(numbers::add);
  }

  @Override
  public int extractNumberToAssign() {
    int randomNumber = getNextInt();
    return numbers.remove(randomNumber);
  }

  @Override
  public boolean areNumberToAssign() {
    return !numbers.isEmpty();
  }

  private int getNextInt() {
    return random.nextInt(numbers.size());
  }
}
