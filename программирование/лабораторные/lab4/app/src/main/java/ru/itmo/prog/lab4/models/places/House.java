package ru.itmo.prog.lab4.models.places;

import com.google.inject.Inject;
import ru.itmo.prog.lab4.interfaces.HasCases;

public class House extends Place implements HasCases {
  public static final String DEFAULT_NAME = "Дом";

  @Inject
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
