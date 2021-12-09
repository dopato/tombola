package it.tommaso.playground.bingo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StripGeneratorTest {

  private static NumberExtractor numberExtractor = new RandomNumberExtractor();
  private static StripGenerator stripGenerator = new StripGenerator(numberExtractor);

  @BeforeAll
  static void setUp() {
    stripGenerator.printStrips();
  }

  @Test
  @DisplayName("Generate a strip of 6 tickets")
  void strip6Tickets() {
    assertEquals(6, stripGenerator.getTickets().size());

  }

  @Test
  @DisplayName("A bingo ticket consists of 3 rows")
  void ticket3Rows() {
    stripGenerator.getTickets()
        .forEach(ticket -> assertEquals(3, ticket.getSortedRows().values().size()));
  }

  @Test
  @DisplayName("A bingo ticket consists of 9 columns and for every row 5 must be number and 4 blank"
      + " spaces")
  void ticket9Columns() {
    stripGenerator.getTickets()
        .stream().map(ticket -> ticket.getSortedRows().values())
        .forEach(rows -> rows.forEach(row -> {
          assertEquals(9, row.getValues().length);
          assertEquals(5, Arrays.stream(row.getValues()).filter(i -> i != -1).count());
          assertEquals(4, Arrays.stream(row.getValues()).filter(i -> i == -1).count());
        }));
  }


  @Test
  @DisplayName("Each ticket column consists of one, two or three numbers and never three blanks.")
  void never3Blanks() {
    stripGenerator.getTickets()
        .stream().map(ticket -> ticket.getSortedRows().values())
        .forEach(rows -> IntStream.range(0, 9).forEach(index -> assertTrue(
            rows.stream().filter(row -> row.getValues()[index] == -1).count() < 3)));
  }


  @Test
  @DisplayName("The first column contains numbers from 1 to 9 (only nine),\n"
      + "  The second column numbers from 10 to 19 (ten), the third, 20 to 29 and so on up until\n"
      + "  The last column, which contains numbers from 80 to 90 (eleven).")
  void multiplesOfTensInProperColumn() {
    stripGenerator.getTickets()
        .stream().map(ticket -> ticket.getSortedRows().values())
        .forEach(rows -> IntStream.range(0, 9).forEach(
            index -> rows.stream().filter(row -> row.getValues()[index] != -1)
                .forEach(row -> assertTrue(row.getValues()[index] <= (index + 1) * 10))));
  }

  @Test
  @DisplayName("Numbers in the ticket columns are ordered from top to bottom (ASC)")
  void numbersInColumnAreOrderedASC() {
    final Set<Integer> numberSet = new HashSet<>();
    stripGenerator.getTickets()
        .stream().map(ticket -> ticket.getSortedRows().values())
        .forEach(rows -> IntStream.range(0, 9).forEach(
            index -> rows.stream().filter(row -> row.getValues()[index] != -1)
                .forEach(row1 -> numberSet.add(row1.getValues()[index]))));
    assertEquals(numberSet, new TreeSet<>(numberSet));
  }

  @Test
  @DisplayName("90 numbers consumed with no duplicates")
  void _90UniqueNumbers() {
    assertFalse(numberExtractor.areNumberToAssign());
    final Set<Integer> numberSet = new HashSet<>();
    stripGenerator.getTickets()
        .stream().map(ticket -> ticket.getSortedRows().values())
        .forEach(rows -> IntStream.range(0, 9).forEach(
            index -> rows.stream().filter(row -> row.getValues()[index] != -1)
                .forEach(row1 -> numberSet.add(row1.getValues()[index]))));
    assertEquals(90, numberSet.size());
  }

}
