package ru.itmo.prog.lab3.models.places;

import ru.itmo.prog.lab3.interfaces.Climbable;
import ru.itmo.prog.lab3.interfaces.HasCases;
import ru.itmo.prog.lab3.models.common.Action;
import ru.itmo.prog.lab3.models.people.Person;

import java.util.Objects;

public class Downpipe extends Place implements Climbable, HasCases {
  private static final int MAX_TIME_FOR_FAST_CLIMB_UP = 10;
  public static final String DEFAULT_NAME = "Водосточная труба";

  private final int timeToClimbUp;

  public Downpipe(int timeToClimbUp) {
    super(DEFAULT_NAME);
    this.timeToClimbUp = timeToClimbUp;
  }

  public Downpipe(String name, int timeToClimbUp) {
    super(name);
    this.timeToClimbUp = timeToClimbUp;
  }

  @Override
  public String beClimbedBy(Person person) {
    return String.join(" ", person.getName(), getAdjective(), Action.CLIMBED_UP.getDescription(person), dativeCase());
  }

  private String getAdjective() {
    if (timeToClimbUp < MAX_TIME_FOR_FAST_CLIMB_UP) {
      return "мгновенно";
    } else {
      return "медленно";
    }
  }

  public String genitiveCase() {
    return "водосточной трубы";
  }

  public String dativeCase() {
    return "водосточной трубе";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Downpipe downpipe = (Downpipe) o;
    return timeToClimbUp == downpipe.timeToClimbUp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeToClimbUp);
  }

  @Override
  public String toString() {
    return "Downpipe{" +
      "timeToClimbUp=" + timeToClimbUp +
      '}';
  }
}
