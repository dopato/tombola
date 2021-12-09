package it.tommaso.playground.bingo;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Ticket {

  private static final int MAX_NUMBERS = 15;

  private int id;
  private final Map<Integer, Column> sortedColumns = new TreeMap<>();
  private final Map<Integer, Row> sortedRows = new TreeMap<>();

  public Ticket(final int id) {
    this.id = id;
  }

  public void printRows() {

    assignFullColumn();

    assign1FreeSpotColumnWithPermutation1_2__2_3__1_3();

    assign2FreeSpotColumn();
  }

  private void assignFullColumn() {
    sortedColumns.values().stream().filter(column -> column.size() == 3).map(Column::sort)
        .map(Column::pollFirst).forEach(numberToAdd -> IntStream.range(1, 3).anyMatch(
            index -> {
              Row row = sortedRows.computeIfAbsent(index + id, i -> new Row(index + id));
              if (row.hasSpace()) {
                return row.addNumber(numberToAdd);
              }
              return false;
            }
        ));
  }

  private void assign2FreeSpotColumn() {
    sortedColumns.values().stream().filter(column -> column.size() == 1).map(Column::pollFirst)
        .forEach(
            numberToAdd -> IntStream.range(1, 4).anyMatch(
                index -> {
                  Row row = sortedRows.computeIfAbsent(index + id, i -> new Row(index + id));
                  if (row.hasSpace()) {
                    return row.addNumber(numberToAdd);
                  }
                  return false;
                }
            ));
  }

  private void assign1FreeSpotColumnWithPermutation1_2__2_3__1_3() {
    sortedColumns.values().stream().filter(column -> column.size() == 2).map(Column::sort)
        .map(Column::pollFirst).forEach(
            numberToAdd -> IntStream.range(1, 3).anyMatch(
                index -> {
                  Row row = sortedRows.computeIfAbsent(index + id, i -> new Row(index + id));
                  if (row.hasSpace()) {
                    return row.addNumber(numberToAdd);
                  }
                  return false;
                }
            ));

    sortedColumns.values().stream().filter(column -> column.size() == 2).map(Column::sort)
        .map(Column::pollFirst).forEach(
            numberToAdd -> IntStream.range(2, 4).anyMatch(
                index -> {
                  Row row = sortedRows.computeIfAbsent(index + id, i -> new Row(index + id));
                  if (row.hasSpace()) {
                    return row.addNumber(numberToAdd);
                  }
                  return false;
                }
            ));

    sortedColumns.values().stream().filter(column -> column.size() == 2).map(Column::sort)
        .map(Column::pollFirst).forEach(
            numberToAdd -> IntStream.range(1, 4).filter(index -> index % 2 == 1).anyMatch(
                index -> {
                  Row row = sortedRows.computeIfAbsent(index + id, i -> new Row(index + id));
                  if (row.hasSpace()) {
                    return row.addNumber(numberToAdd);
                  }
                  return false;
                }
            ));
  }

  public boolean hasSpace() {
    return sortedColumns.values().stream().mapToLong(Column::size).sum()
        < MAX_NUMBERS;
  }

  public Map<Integer, Column> getSortedColumns() {
    return sortedColumns;
  }

  public Map<Integer, Row> getSortedRows() {
    return sortedRows;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return
        "\n" +
            sortedRows.values();
  }
}
