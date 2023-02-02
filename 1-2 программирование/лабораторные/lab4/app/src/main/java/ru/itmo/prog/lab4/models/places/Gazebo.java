package ru.itmo.prog.lab4.models.places;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import ru.itmo.prog.lab4.models.common.Action;
import ru.itmo.prog.lab4.models.people.Group;
import ru.itmo.prog.lab4.interfaces.ShortiesContainer;
import ru.itmo.prog.lab4.models.people.Shorty;

import java.util.Objects;

public class Gazebo extends Place implements ShortiesContainer {
  private final Group<Shorty> shortiesGroup;

  public static final String DEFAULT_NAME = "Беседка";

  public Gazebo(Group<Shorty> shortiesGroup) {
    super(DEFAULT_NAME);
    this.shortiesGroup = shortiesGroup;
  }

  @Inject(optional=true)
  public Gazebo(@Named("GazeboName") String name, Group<Shorty> shortiesGroup) {
    super(name);
    this.shortiesGroup = shortiesGroup;
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
    return shortiesGroup.contains(shorty);
  }

  @Override
  public void addShorty(Shorty shorty) {
    shortiesGroup.addMember(shorty);
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
    return Objects.equals(shortiesGroup, gazebo.shortiesGroup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), shortiesGroup);
  }

  @Override
  public String toString() {
    return "Gazebo{" +
      "shortiesGroup=" + shortiesGroup +
      '}';
  }
}
