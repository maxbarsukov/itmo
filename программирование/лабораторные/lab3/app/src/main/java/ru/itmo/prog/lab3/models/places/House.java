package ru.itmo.prog.lab3.models.places;

import ru.itmo.prog.lab3.interfaces.HasCases;

public class House extends Place implements HasCases {
  public static final String DEFAULT_NAME = "Дом";

  public House() {
    super(DEFAULT_NAME);
  }

  public House(String name) {
    super(name);
  }

  @Override
  public String dativeCase() {
    return "дому";
  }

  @Override
  public String genitiveCase() {
    return "дома";
  }

  @Override
  public String toString() {
    return "House{" +
      "name='" + getName() + '\'' +
      '}';
  }
}
