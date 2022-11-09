package ru.itmo.prog.lab4.models.places;

import com.google.inject.Inject;

public class Workshop extends Place {
  public static final String DEFAULT_NAME = "Мастерская";

  @Inject
  public Workshop(String name) {
    super(name);
  }

  @Override
  public String dativeCase() {
    return "мастерскую";
  }

  @Override
  public String genitiveCase() {
    return "мастерской";
  }

  @Override
  public String toString() {
    return getName();
  }
}
