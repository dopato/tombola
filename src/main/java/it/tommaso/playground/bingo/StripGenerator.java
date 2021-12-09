package it.tommaso.playground.bingo;

import static it.tommaso.playground.bingo.ColumnUtil.getColumnNumber;
import static java.lang.String.format;

import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StripGenerator {

  private final NumberExtractor numberExtractor;

  private static final Logger LOGGER = LoggerFactory.getLogger(StripGenerator.class);

  private final List<Ticket> tickets = List.of(new Ticket(100), new Ticket(200),
      new Ticket(300),
      new Ticket(400), new Ticket(500),
      new Ticket(600));


  public StripGenerator(final NumberExtractor numberExtractor) {
    this.numberExtractor = numberExtractor;
  }

  public void printStrips() {
    while (numberExtractor.areNumberToAssign()) {
      int numberToAssign = numberExtractor.extractNumberToAssign();
      if (!fitsInAnEmptyColumn(numberToAssign)) {
        if (!fitsInFreeSpotColumn(1, numberToAssign)) {
          if (!fitsInFreeSpotColumn(2, numberToAssign)) {
            trySwapping(numberToAssign);
          }
        }
      }
    }
    tickets.forEach(Ticket::printRows);
    LOGGER.info("Strip {}", tickets);
  }

  private boolean fitsInAnEmptyColumn(final int numberToAssign) {
    return tickets.stream().filter(Ticket::hasSpace).map(ticket -> ticket.getSortedColumns()
            .computeIfAbsent(ticket.getId() + getColumnNumber(numberToAssign),
                i -> new Column(ticket.getId() + getColumnNumber(numberToAssign))))
        .filter(Column::isEmpty).anyMatch(column -> column.addNumber(numberToAssign));
  }

  private boolean fitsInFreeSpotColumn(final int numberOfSpots, final int numberToAssign) {
    return tickets.stream().filter(Ticket::hasSpace).map(ticket -> ticket.getSortedColumns()
            .computeIfAbsent(ticket.getId() + getColumnNumber(numberToAssign),
                i -> new Column(ticket.getId() + getColumnNumber(numberToAssign))))
        .filter(column -> column.size() == numberOfSpots)
        .anyMatch(column -> column.addNumber(numberToAssign));
  }

  private void trySwapping(final int numberToAssign) {

    Integer columnIdToInsert = tickets.stream().filter(Ticket::hasSpace)
        .map(ticket -> ticket.getSortedColumns().values()).flatMap(Collection::stream)
        .filter(column -> column.size() == 1).findAny()
        .map(Column::getId).orElseThrow(() -> new RuntimeException(
            "could not find a spot from an incomplete ticket in a column < 3"));

    Integer removedFromCompleteTicket = tickets.stream().filter(ticket -> !ticket.hasSpace())
        .filter(ticket -> ticket.getSortedColumns()
            .computeIfAbsent(ticket.getId() + getColumnNumber(numberToAssign),
                i -> new Column(ticket.getId() + getColumnNumber(numberToAssign))).hasSpace())
        .map(ticket -> ticket.getSortedColumns()
            .computeIfAbsent(ticket.getId() + columnIdToInsert % 100,
                i -> new Column(ticket.getId() + columnIdToInsert % 100)))
        .filter(column -> column.size() > 1).findAny()
        .map(Column::pollFirst).orElseThrow(() -> new RuntimeException(
            "could not remove from an incomplete ticket in a full column"));

    if (tickets.stream().filter(Ticket::hasSpace).map(ticket -> ticket.getSortedColumns()
            .computeIfAbsent(ticket.getId() + getColumnNumber(numberToAssign),
                i -> new Column(ticket.getId() + getColumnNumber(numberToAssign))))
        .filter(Column::hasSpace)
        .noneMatch(column -> column.addNumber(numberToAssign))) {

      throw new RuntimeException(
          format("SWAP: Could not assign number %s after making space for it", numberToAssign));
    }
    LOGGER.debug("numberToAssign {} added in the swap", numberToAssign);

    if (tickets.stream().filter(Ticket::hasSpace).map(ticket -> ticket.getSortedColumns()
            .computeIfAbsent(ticket.getId() + getColumnNumber(removedFromCompleteTicket),
                i -> new Column(ticket.getId() + getColumnNumber(removedFromCompleteTicket))))
        .filter(Column::hasSpace)
        .noneMatch(column -> column.addNumber(removedFromCompleteTicket))) {

      throw new RuntimeException(
          format("SWAP: Could not assign number %s removed from the complete ticket",
              numberToAssign));
    }
    LOGGER.debug("removedFromCompleteTicket {} added in the swap", removedFromCompleteTicket);
  }

  public List<Ticket> getTickets() {
    return tickets;
  }
}
