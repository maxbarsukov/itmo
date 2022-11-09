package ru.itmo.prog.lab4.models.events;

import ru.itmo.prog.lab4.lib.events.interfaces.Event;
import ru.itmo.prog.lab4.lib.events.EventException;
import ru.itmo.prog.lab4.lib.events.EventHandler;

import java.util.Objects;

public class OrderGiven implements Event {
  public static class Handler extends EventHandler<OrderGiven> {
    @Override
    public void handle(OrderGiven event) throws EventException {
      if (event.getTimeToExecute() == TimeToExecute.VERY_HIGH) {
        throw new EventException("Никто не хочет выполнять приказ");
      }
    }

    public String description(OrderGiven event) {
      if (event.getTimeToExecute() == TimeToExecute.HIGH) {
        return "Приказ долго и нехотя исполнили";
      }
      return "Приказ моментально исполнили";
    }
  }

  public enum TimeToExecute {
    VERY_LOW,
    LOW,
    HIGH,
    VERY_HIGH,
  }

  private final TimeToExecute timeToExecute;

  public OrderGiven(TimeToExecute timeToExecute) {
    this.timeToExecute = timeToExecute;
  }

  public TimeToExecute getTimeToExecute() {
    return timeToExecute;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderGiven that = (OrderGiven) o;
    return timeToExecute == that.timeToExecute;
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeToExecute);
  }

  @Override
  public String toString() {
    return "OrderGiven{" +
      "timeToExecute=" + timeToExecute +
      '}';
  }
}
