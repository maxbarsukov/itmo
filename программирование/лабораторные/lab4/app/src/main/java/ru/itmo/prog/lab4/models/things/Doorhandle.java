package ru.itmo.prog.lab4.models.things;

public class Doorhandle extends Thing {
  @Override
  public String dativeCase() {
    return "дверной ручке";
  }

  @Override
  public String genitiveCase() {
    return "дверной ручки";
  }

  @Override
  public String toString() {
    return "Дверная ручка";
  }
}
