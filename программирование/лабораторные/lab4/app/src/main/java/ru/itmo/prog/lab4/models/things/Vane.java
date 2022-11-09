package ru.itmo.prog.lab4.models.things;

import ru.itmo.prog.lab4.interfaces.things.Seized;

public class Vane implements Seized {
  public String getName() {
    return "флюгер";
  }

  public String description() {
    return "показывал направление ветра";
  }
}
