package ru.itmo.prog.lab4.models.common;

import ru.itmo.prog.lab4.models.people.Person;

import java.util.Objects;

public abstract class UnknownEntity {
  public enum Direction {
    UP,
    DOWN,
    NONE;

    public String toString() {
      return switch (this) {
        case UP -> "вверх";
        case DOWN -> "вниз";
        case NONE -> "неизвестно куда";
      };
    }
  }

  private String name;

  public UnknownEntity(String name) {
    this.name = name;
  }

  public String pull(Person personToPull, Direction direction) {
    // Неизвестная Сущность всегда неожиданна
    return getName() + " неожиданно потянула " + personToPull.possessivePronoun() + ' ' + direction.toString();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UnknownEntity that = (UnknownEntity) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return "UnknownEntity{" +
      "name='" + name + '\'' +
      '}';
  }
}
