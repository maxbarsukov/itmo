package ru.itmo.prog.lab3.models.things;

import ru.itmo.prog.lab3.interfaces.HasCases;

public class Roof extends Place implements HasCases {
  public Roof() {
    super("Крыша");
  }

  @Override
  public String dativeCase() {
    return "крышу";
  }

  @Override
  public String genitiveCase() {
    return "крыши";
  }
}
