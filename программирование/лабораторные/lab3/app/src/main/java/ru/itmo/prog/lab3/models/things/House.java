package ru.itmo.prog.lab3.models.things;

import ru.itmo.prog.lab3.interfaces.HasCases;

public class House extends Place implements HasCases {
  public House() {
    super("дом");
  }

  @Override
  public String dativeCase() {
    return "дому";
  }

  @Override
  public String genitiveCase() {
    return "дома";
  }
}
