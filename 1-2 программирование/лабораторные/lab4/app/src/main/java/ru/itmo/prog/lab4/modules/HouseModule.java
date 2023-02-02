package ru.itmo.prog.lab4.modules;

import com.google.inject.AbstractModule;
import ru.itmo.prog.lab4.models.places.House;

public class HouseModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(String.class).toInstance(House.DEFAULT_NAME);
  }
}
