package ru.itmo.prog.lab3.models.things;

import ru.itmo.prog.lab3.models.Action;
import ru.itmo.prog.lab3.models.people.PersonGroup;
import ru.itmo.prog.lab3.interfaces.ShortiesContainer;
import ru.itmo.prog.lab3.models.people.Shorty;

import java.util.Objects;

public class Gazebo extends Place implements ShortiesContainer {
  public PersonGroup people;

  public Gazebo(PersonGroup people) {
    super("Беседка");
    this.people = people;
  }

  public String findShorty(Shorty shorty) {
    if (checkShorty(shorty)) {
      return String.join(" ", shorty.getName(), Action.WAS.getDescription(shorty), "там");
    } else {
      return shorty.genitiveCase() + " и там не было";
    }
  }

  @Override
  public boolean checkShorty(Shorty shorty) {
    return people.contains(shorty);
  }

  @Override
  public String dativeCase() {
    return "беседке";
  }

  @Override
  public String genitiveCase() {
    return "беседки";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Gazebo gazebo = (Gazebo) o;
    return Objects.equals(people, gazebo.people);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), people);
  }

  @Override
  public String toString() {
    return "Gazebo{" +
      "people=" + people +
      '}';
  }
}
