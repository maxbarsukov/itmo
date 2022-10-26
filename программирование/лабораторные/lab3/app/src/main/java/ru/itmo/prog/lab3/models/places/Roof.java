package ru.itmo.prog.lab3.models.places;

import ru.itmo.prog.lab3.interfaces.HasCases;

public class Roof extends Place implements HasCases {
  public static final String DEFAULT_NAME = "Крыша";

  public Roof() {
    super(DEFAULT_NAME);
  }

  public Roof(String name) {
    super(name);
  }

  @Override
  public String dativeCase() {
    return "крышу";
  }

  @Override
  public String genitiveCase() {
    return "крыши";
  }

  @Override
  public String toString() {
    return "Roof{" +
      "name='" + getName() + '\'' +
      '}';
  }
}
