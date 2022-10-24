package ru.itmo.prog.lab3.models.things;

import ru.itmo.prog.lab3.interfaces.HasCases;

import java.util.Objects;

public abstract class Place implements HasCases {
  private String name;

  public Place(String name) {
    this.name = name;
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
    Place place = (Place) o;
    return Objects.equals(name, place.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
